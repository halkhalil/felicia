package controllers.input

import play.api.libs.json.Format
import play.api.libs.json.JsObject
import play.api.libs.json.JsValue
import play.api.libs.json.JsString
import play.api.libs.json.JsNull
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsResult
import java.util.Date
import models.PaymentMethod
import play.api.libs.json.JsBoolean
import play.api.libs.json.JsArray

case class InvoiceUpdateInput(
	buyerIsCompany: Boolean,
	buyerName: String,
	buyerAddress: String,
	buyerZip: String,
	buyerCity: String,
	buyerCountry: String,
	buyerTaxId: String,
	paymentMethod: PaymentMethod
)

object InvoiceUpdateInput {
	implicit object InvoiceInputFormat extends Format[InvoiceUpdateInput] {
		def writes(invoiceInput: InvoiceUpdateInput): JsValue = {
			val paymentMethod: JsValue = if (invoiceInput.paymentMethod != null) JsString(invoiceInput.paymentMethod.id.toString()) else JsNull
			
			JsObject(Seq(
				"buyerIsCompany" -> JsBoolean(invoiceInput.buyerIsCompany),
				"buyerName" -> JsString(invoiceInput.buyerName),
				"buyerAddress" -> JsString(invoiceInput.buyerAddress),
				"buyerZip" -> JsString(invoiceInput.buyerZip),
				"buyerCity" -> JsString(invoiceInput.buyerCity),
				"buyerCountry" -> JsString(invoiceInput.buyerCountry),
				"buyerTaxId" -> JsString(invoiceInput.buyerTaxId),
				"paymentMethod" -> paymentMethod
			))
		}
		
		def reads(json: JsValue): JsResult[InvoiceUpdateInput] = {
			JsSuccess(new InvoiceUpdateInput(
				(json \ "buyerIsCompany").as[Boolean],
				(json \ "buyerName").as[String],
				(json \ "buyerAddress").as[String],
				(json \ "buyerZip").as[String],
				(json \ "buyerCity").as[String],
				(json \ "buyerCountry").as[String],
				(json \ "buyerTaxId").as[String],
				PaymentMethod.finder.where().eq("id", (json \ "paymentMethod").as[Int]).findUnique()
			))
		}
    }
}