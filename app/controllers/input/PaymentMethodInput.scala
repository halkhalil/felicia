package controllers.input

import play.data.format._;
import play.data.validation._;
import play.api.libs.json._

case class PaymentMethodInput(name: String, symbol: String)

object PaymentMethodInput {
	implicit object PaymentMethodInputFormat extends Format[PaymentMethodInput] {
		def writes(paymentMethodInput: PaymentMethodInput): JsValue = {
			JsObject(Seq(
				"name" -> JsString(paymentMethodInput.name),
				"symbol" -> JsString(paymentMethodInput.symbol)
			))
		}
		
		def reads(json: JsValue): JsResult[PaymentMethodInput] = {
			JsSuccess(new PaymentMethodInput(
				(json \ "name").as[String],
				(json \ "symbol").as[String]
			))
		}
    }
}