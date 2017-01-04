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

case class InvoiceInput(
	buyerIsCompany: Boolean,
	buyerName: String,
	buyerAddress: String,
	buyerZip: String,
	buyerCity: String,
	buyerCountry: String,
	buyerTaxId: String,
	buyerEmail: String,
	buyerPhone: String,
	issueDate: Date,
	orderDate: Date,
	dueDate: Date,
	paymentMethod: PaymentMethod,
	parts: List[InvoicePartInput]
)

object InvoiceInput {
	implicit object InvoiceInputFormat extends Format[InvoiceInput] {
		def writes(invoiceInput: InvoiceInput): JsValue = {
			val paymentMethod: JsValue = if (invoiceInput.paymentMethod != null) JsString(invoiceInput.paymentMethod.id.toString()) else JsNull
			
			JsObject(Seq(
				"buyerIsCompany" -> JsBoolean(invoiceInput.buyerIsCompany),
				"buyerName" -> JsString(invoiceInput.buyerName),
				"buyerAddress" -> JsString(invoiceInput.buyerAddress),
				"buyerZip" -> JsString(invoiceInput.buyerZip),
				"buyerCity" -> JsString(invoiceInput.buyerCity),
				"buyerCountry" -> JsString(invoiceInput.buyerCountry),
				"buyerTaxId" -> JsString(invoiceInput.buyerTaxId),
				"buyerEmail" -> JsString(invoiceInput.buyerEmail),
				"buyerPhone" -> JsString(invoiceInput.buyerPhone),
				"issueDate" -> JsString(invoiceInput.issueDate.toString()),
				"orderDate" -> JsString(invoiceInput.orderDate.toString()),
				"dueDate" -> JsString(invoiceInput.dueDate.toString()),
				"paymentMethod" -> paymentMethod
			))
		}
		
		def reads(json: JsValue): JsResult[InvoiceInput] = {
			JsSuccess(new InvoiceInput(
				(json \ "buyerIsCompany").as[Boolean],
				(json \ "buyerName").as[String],
				(json \ "buyerAddress").as[String],
				(json \ "buyerZip").as[String],
				(json \ "buyerCity").as[String],
				(json \ "buyerCountry").as[String],
				(json \ "buyerTaxId").as[String],
				(json \ "buyerEmail").as[String],
				(json \ "buyerPhone").as[String],
				(json \ "issueDate").as[Date],
				(json \ "orderDate").as[Date],
				(json \ "dueDate").as[Date],
				PaymentMethod.finder.where().eq("id", (json \ "paymentMethod").as[Int]).findUnique(),
				(json \ "parts").as[List[InvoicePartInput]]
			))
		}
    }
}