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
import services.application.ConfigurationService
import controllers.input.ConfigurationEntryInput

@Singleton
class ConfigurationController @Inject() (authenticationService: AuthenticationService, usersService: UserService, configurationService: ConfigurationService) 
	extends BaseController(authenticationService, usersService) {
	
	def index = SecuredAction { request =>
		ok(configurationService.getAll())
	}
	
	def save = SecuredAction(BodyParsers.parse.json) { request =>
		val entriesInputResult = request.body.validate[List[ConfigurationEntryInput]]
		
		entriesInputResult.fold(
			errors => {
				badRequest("JSON parsing error: " + JsError.toJson(errors))
			},
			entriesInput => {
				ok(configurationService.update(entriesInput))
			}
		)
		
	}
}