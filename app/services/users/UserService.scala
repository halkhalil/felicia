package services.users

import javax.inject._
import models.User
import scala.collection.JavaConverters._
import controllers.input.UserUpdateInput
import utils.StringUtils
import controllers.input.UserCreateInput
import models.user.UserSession
import models.UserRole

@Singleton
class UserService {
	
	def get(id: Int):Option[User] = {
		val user:User = User.finder.where().eq("id", id).findUnique()
		
		if (user != null) Some(user) else None
	}
	
	def getAll():List[User] = {
		User.finder.all().asScala.toList
	}
	
	def validationErrorOnCreate(userCreateInput: UserCreateInput):Option[String] = {
		if (userCreateInput.login.length() == 0) return Some("Login cannot be empty")
		if (userCreateInput.name.length() == 0) return Some("Name cannot be empty")
		if (userCreateInput.password.length() == 0) return Some("Password cannot be empty")
		
		// check for existing login:
		val userExisting:User = User.finder.where().eq("login", userCreateInput.login).findUnique()
		if (userExisting != null) return Some("Login is already occupied ")
		
		// check the role:
		val userRole:UserRole = UserRole.finder.where().eq("symbol", userCreateInput.role).findUnique()
		if (userRole == null) return Some("Role is invalid")

		None
	}
	
	def create(userCreateInput: UserCreateInput):User = {
		val user:User = new User()
		
		user.login = userCreateInput.login
		user.name = userCreateInput.name
		user.password = StringUtils.sha1(userCreateInput.password)
		user.role = UserRole.finder.where().eq("symbol", userCreateInput.role).findUnique()
		
		user.save()
		
		user
	}
	
	def validationErrorOnUpdate(userUpdateInput: UserUpdateInput):Option[String] = {
		if (userUpdateInput.name.trim().length() == 0) return Some("Name cannot be empty")
		if (userUpdateInput.role.trim().length() == 0) return Some("Role cannot be empty")

		val userRole:UserRole = UserRole.finder.where().eq("symbol", userUpdateInput.role).findUnique()
		if (userRole == null) return Some("Role is invalid")
		
		None
	}
	
	def update(user:User, userUpdateInput: UserUpdateInput):User = {
		user.name = userUpdateInput.name
		if (userUpdateInput.password.length() > 0) user.password = StringUtils.sha1(userUpdateInput.password)

		user.role = UserRole.finder.where().eq("symbol", userUpdateInput.role).findUnique()
		
		user.save()
		
		user
	}
	
	def delete(user:User):User = {
		UserSession.finder.where().eq("user", user).findList().asScala.toList.foreach  { session =>
			session.delete()
		}
		
		user.delete()
		
		user
	}
}