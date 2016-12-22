package services.users

import javax.inject._
import models.User
import scala.collection.JavaConverters._
import controllers.input.UserInput
import utils.StringUtils
import controllers.input.UserCreateInput

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
		if (userExisting != null) return Some("This login is already occupied")

		None
	}
	
	def create(userCreateInput: UserCreateInput):User = {
		val user:User = new User()
		
		user.login = userCreateInput.login
		user.name = userCreateInput.name
		user.password = StringUtils.sha1(userCreateInput.password)
		
		user.save()
		
		user
	}
	
	def validationError(userInput: UserInput):Option[String] = {
		if (userInput.name.trim().length() == 0) return Some("Name cannot be empty")

		None
	}
	
	def update(user:User, userInput: UserInput):User = {
		user.name = userInput.name
		if (userInput.password.length() > 0) user.password = StringUtils.sha1(userInput.password)
		
		user.save()
		
		user
	}
}