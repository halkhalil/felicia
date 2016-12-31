package controllers.input

import play.api.libs.json.Format
import play.api.libs.json.JsObject
import play.api.libs.json.JsValue
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsResult
import java.util.Date

case class InvoiceInput(
	buyerName: String,
	buyerAddress: String,
	buyerZip: String,
	buyerCity: String,
	buyerCountry: String,
	buyerTaxId: String,
	issueDate: Date,
	orderDate: Date,
	dueDate: Date 
)

object InvoiceInput {
	implicit object PaymentMethodInputFormat extends Format[InvoiceInput] {
		def writes(invoiceInput: InvoiceInput): JsValue = {
			JsObject(Seq(
				"buyerName" -> JsString(invoiceInput.buyerName),
				"buyerAddress" -> JsString(invoiceInput.buyerAddress),
				"buyerZip" -> JsString(invoiceInput.buyerZip),
				"buyerCity" -> JsString(invoiceInput.buyerCity),
				"buyerCountry" -> JsString(invoiceInput.buyerCountry),
				"buyerTaxId" -> JsString(invoiceInput.buyerTaxId),
				"issueDate" -> JsString(invoiceInput.issueDate.toString()),
				"orderDate" -> JsString(invoiceInput.orderDate.toString()),
				"dueDate" -> JsString(invoiceInput.dueDate.toString())
				
			))
		}
		
		def reads(json: JsValue): JsResult[InvoiceInput] = {
			JsSuccess(new InvoiceInput(
				(json \ "buyerName").as[String],
				(json \ "buyerAddress").as[String],
				(json \ "buyerZip").as[String],
				(json \ "buyerCity").as[String],
				(json \ "buyerCountry").as[String],
				(json \ "buyerTaxId").as[String],
				(json \ "issueDate").as[Date],
				(json \ "orderDate").as[Date],
				(json \ "dueDate").as[Date]
			))
		}
    }
}