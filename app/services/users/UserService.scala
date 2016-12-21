package services.users

import javax.inject._
import models.User
import scala.collection.JavaConverters._
import controllers.input.UserInput
import utils.StringUtils

@Singleton
class UserService {
	
	def get(id: Int):Option[User] = {
		val user:User = User.finder.where().eq("id", id).findUnique()
		
		if (user != null) Some(user) else None
	}
	
	def getAll():List[User] = {
		User.finder.all().asScala.toList
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