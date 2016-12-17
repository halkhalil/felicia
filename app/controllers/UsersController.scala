package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.ApplicationService
import play.api.libs.json.Json
import services.users.AuthenticationService
import models.User
import scala.collection.JavaConverters._

@Singleton
class UsersController @Inject() (authenticationService: AuthenticationService) extends Controller {
	def index = Action { request =>
		val userSessionId: Option[String] = request.session.get("userId")
		
		if (authenticationService.isLoggedInAndIsAdmin(userSessionId)) {
			val users:List[User] = User.finder.all().asScala.toList
			
			Ok(Json.toJson(users)).as(JSON)
		} else {
			forbidden("Access denied")
		}
	}
	
	private def forbidden(error: String):Result = {
		Forbidden(
			Json.obj(
				"error" -> error
			)
		).as(JSON)
	}
}