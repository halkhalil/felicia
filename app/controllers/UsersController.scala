package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.ApplicationService
import play.api.libs.json.Json
import services.users.AuthenticationService
import models.User
import scala.collection.JavaConverters._
import services.users.UserService
import controllers.input.UserInput
import play.api.libs.json.JsError

@Singleton
class UsersController @Inject() (authenticationService: AuthenticationService, usersService: UserService) extends BaseController(authenticationService, usersService) {
	
	def index = SecuredAction { request =>
		ok(usersService.getAll())
	}
	
	def get(id: Int) = SecuredAction { request =>
		usersService.get(id).map { user =>
			ok(user)
		}.getOrElse {
			notFound("User does not exists") 
		}
	}
	
	def save(id: Int) = SecuredAction(BodyParsers.parse.json) { request =>
		val userInputResult = request.body.validate[UserInput]
		
		userInputResult.fold(
			errors => {
				badRequest("JSON parsing error: " + JsError.toJson(errors))
			},
			userInput => {
				usersService.validationError(userInput).map { error =>
					badRequest(error)
				}.getOrElse {
					usersService.get(id).map { user =>
						ok(usersService.update(user, userInput))
					}.getOrElse {
						notFound("User does not exists") 
					}
				}
			}
		)
	}
	
}