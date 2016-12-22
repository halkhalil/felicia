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
import controllers.input.UserUpdateInput
import play.api.libs.json.JsError
import controllers.input.UserCreateInput

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
		val userUpdateInputResult = request.body.validate[UserUpdateInput]
		
		userUpdateInputResult.fold(
			errors => {
				badRequest("JSON parsing error: " + JsError.toJson(errors))
			},
			userUpdateInput => {
				usersService.validationErrorOnUpdate(userUpdateInput).map { error =>
					badRequest(error)
				}.getOrElse {
					usersService.get(id).map { user =>
						ok(usersService.update(user, userUpdateInput))
					}.getOrElse {
						notFound("User does not exists") 
					}
				}
			}
		)
	}
	
	def create = SecuredAction(BodyParsers.parse.json) { request =>
		val userInputResult = request.body.validate[UserCreateInput]
		
		userInputResult.fold(
			errors => {
				badRequest("JSON parsing error: " + JsError.toJson(errors))
			},
			userCreateInput => {
				usersService.validationErrorOnCreate(userCreateInput).map { error =>
					badRequest(error)
				}.getOrElse {
					ok(usersService.create(userCreateInput))
				}
			}
		)
	}
	
	def delete(id: Int) = SecuredAction { request =>
		usersService.get(id).map { user =>
			ok(usersService.delete(user))
		}.getOrElse {
			notFound("User does not exists") 
		}
	}
	
}