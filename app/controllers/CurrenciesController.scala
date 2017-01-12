package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.ApplicationService
import play.api.libs.json.Json
import services.users.AuthenticationService
import scala.collection.JavaConverters._
import services.users.UserService
import play.api.libs.json.JsError
import services.currencies.CurrenciesService
import services.supporting.DatesService

@Singleton
class CurrenciesController @Inject() (
		authenticationService: AuthenticationService, usersService: UserService, currenciesService: CurrenciesService, datesService: DatesService
	) extends BaseController(authenticationService, usersService) {
	
	def index(year: Int, month: Int, day: Int) = SecuredAction { request =>
		ok(currenciesService.getAll(datesService.date(year, month, day)))
	}
	
}