package services.application

import java.util.List
import models._
import javax.inject.Singleton
import scala.collection.JavaConversions._

@Singleton
class SetupService {
	
	dataInitialize()
	
	def dataInitialize() = {
		val md = java.security.MessageDigest.getInstance("SHA-1")
		val roles:List[UserRole] = UserRole.finder.all()
		val users:List[User] = User.finder.all()
		
		// create admin role:
		if (roles.size == 0) {
			val adminUserRole:UserRole = new UserRole()
			adminUserRole.name = "Administrator"
			adminUserRole.symbol = "admin"
			adminUserRole.save()
		}
		
		// create admin user:
		if (users.size == 0) {
			val adminUserRole:UserRole = UserRole.finder.where().eq("symbol", "admin").findUnique()
			val adminUser:User = new User()
			adminUser.login = "admin"
			adminUser.password = play.api.libs.Codecs.sha1(md.digest("admin".getBytes))
			adminUser.role = adminUserRole
			adminUser.save()
		}
	}
}