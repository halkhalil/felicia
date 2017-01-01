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
import models.User
import play.api.mvc.WrappedRequest
import play.api.mvc.ActionTransformer
import play.api.mvc.ActionFilter

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
	
	object UserAction extends ActionBuilder[UserRequest] with ActionTransformer[Request, UserRequest] {
		def transform[A](request: Request[A]) = Future.successful {
			val userSessionId: Option[String] = request.session.get(SESSION_USER_KEY)
			
			new UserRequest(authenticationService.loggedInUser(userSessionId), request)
		}
	}
	
	object AuthorizationCheckAction extends ActionFilter[UserRequest] {
		def filter[A](input: UserRequest[A]) = Future.successful {
			if (!input.user.nonEmpty)
				Some(forbidden("Not authorized"))
			else
				None
		}
	}
	
	class UserRequest[A](val user: Option[User], request: Request[A]) extends WrappedRequest[A](request)
	
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