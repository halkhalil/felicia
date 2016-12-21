package services.application

import java.util.List
import models._
import javax.inject.Singleton
import scala.collection.JavaConversions._
import utils.StringUtils

@Singleton
class SetupService {
	private val ADMIN_LOGIN:String = "admin"
	private val ADMIN_NAME:String = "Administrator"
	private val ADMIN_PASSWORD:String = "admin"
	private val ADMIN_ROLE_NAME:String = "Administrator"
	private val ADMIN_ROLE_SYMBOL:String = "admin"
	
	dataInitialize()
	
	def dataInitialize() = {
		val roles:List[UserRole] = UserRole.finder.all()
		val users:List[User] = User.finder.all()
		
		// create admin role:
		if (roles.size == 0) {
			val adminUserRole:UserRole = new UserRole()
			adminUserRole.name = ADMIN_ROLE_NAME
			adminUserRole.symbol = ADMIN_ROLE_SYMBOL
			adminUserRole.save()
		}
		
		// create admin user:
		if (users.size == 0) {
			val adminUserRole:UserRole = UserRole.finder.where().eq("symbol", ADMIN_LOGIN).findUnique()
			val adminUser:User = new User()
			adminUser.login = ADMIN_LOGIN
			adminUser.name = ADMIN_NAME
			adminUser.password = StringUtils.sha1(ADMIN_PASSWORD)
			adminUser.role = adminUserRole
			adminUser.save()
		}
	}
}