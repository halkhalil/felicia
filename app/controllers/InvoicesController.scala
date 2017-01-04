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
import services.InvoicesService
import controllers.input.InvoiceInput
import play.api.libs.json.JsValue
import models.User

@Singleton
class InvoicesController @Inject() (authenticationService: AuthenticationService, usersService: UserService, invoicesService: InvoicesService) 
	extends BaseController(authenticationService, usersService) {
	
	def index(year: Int, month: Int) = (UserAction andThen AuthorizationCheckAction) { request =>
		ok(invoicesService.getAll(year, month))
	}
	
	def get(id: Int) = (UserAction andThen AuthorizationCheckAction) { request =>
		invoicesService.get(id).map { invoice =>
			ok(invoice)
		}.getOrElse { 
			notFound("Invoice does not exist") 
		}
	}
	
	def deleteLast(year: Int) = (UserAction andThen AuthorizationCheckAction) { request =>
		invoicesService.getLast(year).map { invoice =>
			ok(invoicesService.delete(invoice))
		}.getOrElse {
			notFound("There are no invoices")
		}
	}

	def create = (UserAction andThen AuthorizationCheckAction)(BodyParsers.parse.json) { request =>
		val invoiceInputResult = request.body.validate[InvoiceInput]
		
		invoiceInputResult.fold(
			errors => {
				badRequest("JSON parsing error: " + JsError.toJson(errors))
			},
			invoiceInput => {
				invoicesService.validationErrorOnCreate(invoiceInput).map { error =>
					badRequest(error)
				}.getOrElse {
					ok(invoicesService.create(invoiceInput, request.user.get))
				}
			}
		)
	}
}