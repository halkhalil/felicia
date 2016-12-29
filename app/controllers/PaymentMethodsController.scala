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
import services.PaymentMethodsService
import controllers.input.PaymentMethodInput

@Singleton
class PaymentMethodsController @Inject() (authenticationService: AuthenticationService, usersService: UserService, paymentMethodsService: PaymentMethodsService) 
	extends BaseController(authenticationService, usersService) {

	def index = SecuredAction { request =>
		ok(paymentMethodsService.getAll())
	}
	
	def get(id: Int) = SecuredAction { request =>
		paymentMethodsService.get(id).map { paymentMethod =>
			ok(paymentMethod)
		}.getOrElse { 
			notFound("Payment method does not exist") 
		}
	}
	
	def save(id: Int) = SecuredAction(BodyParsers.parse.json) { request =>
		val paymentMethodInputResult = request.body.validate[PaymentMethodInput]
		
		paymentMethodInputResult.fold(
			errors => {
				badRequest("JSON parsing error: " + JsError.toJson(errors))
			},
			paymentMethodInput => {
				paymentMethodsService.validationErrorOnUpdate(paymentMethodInput).map { error =>
					badRequest(error)
				}.getOrElse {
					paymentMethodsService.get(id).map { paymentMethod =>
						ok(paymentMethodsService.update(paymentMethod, paymentMethodInput))
					}.getOrElse {
						notFound("Payment method does not exist") 
					}
				}
			}
		)
	}
	
	def create = SecuredAction(BodyParsers.parse.json) { request =>
		val paymentMethodInputResult = request.body.validate[PaymentMethodInput]
		
		paymentMethodInputResult.fold(
			errors => {
				badRequest("JSON parsing error: " + JsError.toJson(errors))
			},
			paymentMethodInput => {
				paymentMethodsService.validationErrorOnCreate(paymentMethodInput).map { error =>
					badRequest(error)
				}.getOrElse {
					ok(paymentMethodsService.create(paymentMethodInput))
				}
			}
		)
	}
	
	def delete(id: Int) = SecuredAction { request =>
		paymentMethodsService.get(id).map { paymentMethod =>
			ok(paymentMethodsService.delete(paymentMethod))
		}.getOrElse {
			notFound("Payment method does not exist") 
		}
	}
}