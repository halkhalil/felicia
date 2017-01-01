package services.users

import java.security.MessageDigest
import javax.inject._
import play.api.libs.Codecs
import models.User
import play.api.Configuration
import java.util.Date
import models.user.UserSession
import utils.StringUtils

@Singleton
class AuthenticationService @Inject() (configuration: Configuration) {
	
	def authenticate(login: String, password: String):Option[User] = {
		val user: User = User.finder.where().eq("login", login).findUnique()
		var passwordProposal: String = StringUtils.sha1(password)
		
		if (user != null && user.password == passwordProposal) Some(user) else None
	}
	
	def storeLoggedInUser(user: User, ipAddress: String):String = {
		val idComponents: String = user.id + configuration.underlying.getString("play.crypto.secret") + new Date().getTime().toString()
		val sessionId: String = StringUtils.sha1(idComponents)
		
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
	
	def loggedInUser(userSessionId: Option[String]): Option[User] = {
		userSessionId.map { userSessionId => 
			val userSession: UserSession = UserSession.finder.where().eq("sessionId", userSessionId).findUnique()
			
			if (userSession != null && userSession.user != null) Some(userSession.user) else None
		}.getOrElse {
			None
		}
	}
	
}