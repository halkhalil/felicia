package controllers

import play.api._
import play.api.mvc._
import javax.inject._
import play.api.libs.json._
import services.users.AuthenticationService
import models.User
import play.api.libs.Crypto
import services.users.UserService
import controllers.input.AuthenticationInput

case class ErrorResponse(error: String)

@Singleton
class AuthenticateController @Inject() (authenticationService: AuthenticationService, usersService: UserService) extends BaseController(authenticationService, usersService) {

	def login = Action(BodyParsers.parse.json) { request =>
		val authenticationInputResult = request.body.validate[AuthenticationInput]
		
		authenticationInputResult.fold(
			errors => {
				badRequest("JSON parsing error: " + JsError.toJson(errors))
			},
			authenticationInput => {
				authenticationService.authenticate(authenticationInput.login, authenticationInput.password).map { user =>
					val userSessionId: String = authenticationService.storeLoggedInUser(user, request.remoteAddress)
					okWithUser(user).withSession(SESSION_USER_KEY -> userSessionId)
				}.getOrElse {
					badRequest("Invalid login or password")
				}
			}
		)
	}
	
	def logout = Action {
		okEmpty.withNewSession
	}
	
	private def okWithUser(user: User):Result = {
		Ok(
			Json.obj(
				"user" -> Json.toJson(user)
			)
		).as(JSON)
	}
}