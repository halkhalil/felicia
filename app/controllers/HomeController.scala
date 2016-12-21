package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.ApplicationService
import play.api.libs.json.Json

/**
 * Main controller.
 */
@Singleton
class HomeController @Inject() (application: ApplicationService) extends Controller {

	/**
	* Renders page at "/".
	*/
	def index = Action { request =>
		Ok(
			views.html.index(
				Json.stringify(application.initialData(request.session.get("userId")))
			)
		)
	}

}
