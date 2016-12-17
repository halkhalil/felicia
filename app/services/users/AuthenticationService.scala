package services.users

import java.security.MessageDigest
import javax.inject._
import play.api.libs.Codecs
import models.User
import play.api.Configuration
import java.util.Date
import models.user.UserSession

@Singleton
class AuthenticationService @Inject() (configuration: Configuration) {
	private val md = MessageDigest.getInstance("SHA-1")
	
	def authenticate(login: String, password: String):Option[User] = {
		val user: User = User.finder.where().eq("login", login).findUnique()
		var passwordProposal: String = Codecs.sha1(md.digest(password.getBytes))
		
		if (user != null && user.password == passwordProposal) Some(user) else None
	}
	
	def storeLoggedInUser(user: User, ipAddress: String):String = {
		val idComponents: String = user.id + configuration.underlying.getString("play.crypto.secret") + new Date().getTime().toString()
		val sessionId: String = Codecs.sha1(md.digest((idComponents).getBytes))
		
		val userSession:UserSession = new UserSession()
		userSession.user = user
		userSession.sessionId = sessionId
		userSession.ip = ipAddress
		userSession.added = new Date()
		userSession.save()
		
		sessionId
	}
	
	def isLoggedInAndIsAdmin(userSessionId:Option[String]):Boolean = {
		userSessionId.map { userSessionId => 
			val userSession:UserSession = UserSession.finder.where().eq("sessionId", userSessionId).findUnique()
			
			return userSession != null && userSession.user != null && userSession.user.role.symbol == "admin"
		}
		
		false
	}
}