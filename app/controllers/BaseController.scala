package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.mvc.Request
import scala.concurrent.Future
import play.api.mvc.Result
import javax.inject.Inject
import services.users.AuthenticationService
import services.users.UserService
import play.api.libs.json.Json
import play.api.mvc.AnyContent
import play.api.mvc.ActionBuilder
import play.api.libs.json.Writes

class BaseController @Inject() (authenticationService: AuthenticationService, usersService: UserService) extends Controller {

	val SESSION_USER_KEY: String = "userId"	
	
	object SecuredAction extends ActionBuilder[Request] {
		def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
			val userSessionId: Option[String] = request.session.get(SESSION_USER_KEY)
			
			if (authenticationService.isLoggedInAndIsAdmin(userSessionId)) {
				block(request)
			} else {
				Future.successful {
					forbidden("Access denied")
				}
			}
		}
	}
	
	def ok[Z](obj: Z)(implicit tjs: Writes[Z]):Result = {
		Ok(Json.toJson(obj)).as(JSON)
	}
	
	def okEmpty:Result = {
		Ok("")
	}
		
	def forbidden(error: String):Result = {
		Forbidden(
			Json.obj(
				"error" -> error
			)
		).as(JSON)
	}
	
	def notFound(error: String):Result = {
		NotFound(
			Json.obj(
				"error" -> error
			)
		).as(JSON)
	}
	
	def badRequest(error: String):Result = {
		BadRequest(
			Json.obj(
				"error" -> error
			)
		).as(JSON)
	}
	
}