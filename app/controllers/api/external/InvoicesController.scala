package controllers.api.external

import javax.inject._
import play.api._
import play.api.mvc._
import services.ApplicationService
import play.api.libs.json.Json
import services.users.AuthenticationService
import scala.collection.JavaConverters._
import services.users.UserService
import play.api.libs.json.JsError
import services.InvoicesService
import controllers.input.InvoiceInput
import play.api.libs.json.JsValue
import models.User
import controllers.input.InvoiceUpdateInput
import controllers.BaseController

@Singleton
class InvoicesController @Inject() (authenticationService: AuthenticationService, usersService: UserService, invoicesService: InvoicesService) 
	extends BaseController(authenticationService, usersService) {

	def createExternal = BasicSecuredAction(BodyParsers.parse.json) { request =>
		val invoiceInputResult = request.body.validate[InvoiceInput]
		
		invoiceInputResult.fold(
			errors => {
				badRequest("JSON parsing error: " + JsError.toJson(errors))
			},
			invoiceInput => {
				invoicesService.validationErrorOnCreate(invoiceInput).map { error =>
					badRequest(error)
				}.getOrElse {
					ok(invoicesService.create(invoiceInput, usersService.externalApiUser))
				}
			}
		)
	}

}