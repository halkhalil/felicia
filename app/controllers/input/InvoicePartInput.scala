package controllers.input

import play.api.libs.json.Format
import play.api.libs.json.JsValue
import play.api.libs.json.JsResult
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import play.api.libs.json.JsArray

case class InvoicePartInput(name: List[TranslationInput], unit: String, quantity: Int, unitPrice: Int, total: Int)

object InvoicePartInput {
	implicit object InvoicePartInputFormat extends Format[InvoicePartInput] {
		def writes(invoicePartInput: InvoicePartInput): JsValue = {
			JsObject(Seq(
				"unit" -> JsString(invoicePartInput.unit),
				"quantity" -> JsNumber(invoicePartInput.quantity),
				"unitPrice" -> JsNumber(invoicePartInput.unitPrice),
				"total" -> JsNumber(invoicePartInput.total)
			))
		}

		def reads(json: JsValue): JsResult[InvoicePartInput] = {
			JsSuccess(new InvoicePartInput(
				(json \ "name").as[List[TranslationInput]],
				(json \ "unit").as[String],
				(json \ "quantity").as[Int],
				(json \ "unitPrice").as[Int],
				(json \ "total").as[Int]
			))
		}
	}
}