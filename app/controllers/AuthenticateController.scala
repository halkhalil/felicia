package controllers

import play.api._
import play.api.mvc._
import javax.inject._
import play.api.libs.json._
import services.users.AuthenticationService
import models.User
import play.api.libs.Crypto

case class ErrorResponse(error: String)

@Singleton
class AuthenticateController @Inject() (authenticationService: AuthenticationService) extends Controller {
	
	private def badRequest(error: String):Result = {
		BadRequest(
			Json.obj(
				"error" -> error
			)
		).as(JSON)
	}
	
	private def okLogin(status: String, user: User):Result = {
		Ok(
			Json.obj(
				"status" -> status,
				"user" -> Json.obj(
					"login" -> user.login
				)
			)
		).as(JSON)
	}
	
	private def okLogout(status: String):Result = {
		Ok(
			Json.obj(
				"status" -> status
			)
		).as(JSON)
	}
	
	def login = Action { request =>
		val body: AnyContent = request.body

		body.asJson.map { json =>
			val login:String = (json \ "login").as[String]
			val password:String = (json \ "password").as[String]
			
			authenticationService.authenticate(login, password).map { user =>
				val userSessionId: String = authenticationService.storeLoggedInUser(user, request.remoteAddress)
				okLogin("OK", user).withSession("userId" -> userSessionId)
			}.getOrElse {
				badRequest("Invalid login or password")
			}
			
		}.getOrElse {
			badRequest("Expecting JSON request body")
  		}
	}
	
	def logout = Action { request =>
		okLogout("OK").withNewSession
	}
}