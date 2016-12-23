package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.ApplicationService
import play.api.libs.json.Json
import services.users.AuthenticationService
import services.users.UserService

@Singleton
class HomeController @Inject() (application: ApplicationService, authenticationService: AuthenticationService, usersService: UserService) extends BaseController(authenticationService, usersService) {

	/**
	* Renders page at "/".
	*/
	def index = Action { request =>
		Ok(
			views.html.index(
				Json.stringify(
					application.frontEndConfiguration(request.session.get(SESSION_USER_KEY))
				)
			)
		)
	}

}
