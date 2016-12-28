package services.application

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
	private val REGULAR_ROLE_NAME:String = "Regular"
	private val REGULAR_ROLE_SYMBOL:String = "regular"
	
	dataInitialize()
	initConfiguration()
	
	def dataInitialize() = {
		val roles:java.util.List[UserRole] = UserRole.finder.all()
		val users:java.util.List[User] = User.finder.all()
		
		// create admin role:
		if (roles.size == 0) {
			val adminUserRole:UserRole = new UserRole()
			adminUserRole.name = ADMIN_ROLE_NAME
			adminUserRole.symbol = ADMIN_ROLE_SYMBOL
			adminUserRole.save()
			
			val regularUserRole:UserRole = new UserRole()
			regularUserRole.name = REGULAR_ROLE_NAME
			regularUserRole.symbol = REGULAR_ROLE_SYMBOL
			regularUserRole.save()
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
	
	def initConfiguration() = {
		val entries = List(
			List("SMTP Configuration", "E-mail", "smtp.email", "text", "", ""),
			List("SMTP Configuration", "Sender name", "smtp.sender", "text", "", ""),
			List("SMTP Configuration", "Login", "smtp.login", "text", "", ""),
			List("SMTP Configuration", "Password", "smtp.password", "text", "", ""),
			List("SMTP Configuration", "Host", "smtp.host", "text", "", ""),
			List("SMTP Configuration", "SSL", "smtp.ssl", "boolean", "", ""),
			List("SMTP Configuration", "Port", "smtp.port", "number", "", "")
		)
		
		entries.foreach { entry =>
			if (ConfigurationEntry.finder.where().eq("symbol", entry(2)).findUnique() == null) {
				val configEntry = new ConfigurationEntry()
				configEntry.sectionName = entry(0)
				configEntry.name = entry(1)
				configEntry.symbol = entry(2)
				configEntry.typeId = entry(3)
				configEntry.typeConfiguration = entry(4)
				configEntry.value = entry(5)
				configEntry.save()
			}
		}
	}
}