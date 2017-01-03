package models.invoice

import java.util._;
import javax.persistence._;
import javax.validation.constraints._;

import com.avaje.ebean.Model;
import play.data.format._;
import play.data.validation._;
import models.user.UserSession
import play.api.libs.json.Json
import play.api.libs.json._
import models.PaymentMethod
import play.api.data.format.Formats
import models.User

@Entity
class Invoice extends Model {
	@Id
	var id:Int = _
	
	@NotNull
	@Column(unique = true)
	var publicId: String = _
	
	@NotNull
	var currency: String = _
	
	@NotNull
	var total: Int = _
	
	@NotNull
	var sellerName: String = _
	
	@NotNull
	var sellerAddress: String = _
	
	@NotNull
	var sellerZip: String = _
	
	@NotNull
	var sellerCity: String = _
	
	@NotNull
	var sellerCountry: String = _
	
	@NotNull
	var sellerTaxId: String = _
	
	@NotNull
	var buyerIsCompany: Boolean = _
	
	@NotNull
	var buyerName: String = _
	
	@NotNull
	var buyerAddress: String = _
	
	@NotNull
	var buyerZip: String = _
	
	@NotNull
	var buyerCity: String = _
	
	@NotNull
	var buyerCountry: String = _
	
	var buyerTaxId: String = _
	
	@NotNull
	@ManyToOne()
	var paymentMethod: PaymentMethod = _
	
	@NotNull
	var placeOfIssue: String = _
	
	@NotNull
	@Column(columnDefinition = "DATE")
	var issueDate: Date = _
	
	@NotNull
	@Column(columnDefinition = "DATE")
	var orderDate: Date = _
	
	@NotNull
	@Column(columnDefinition = "DATE")
	var dueDate: Date = _

	@OneToMany(mappedBy = "invoice")
	var invoiceParts: List[InvoicePart] = _
	
	@Column(columnDefinition = "TEXT")
	var additionalDetails: String = _
	
	@NotNull
	@ManyToOne()
	var creator: User = _
}

object Invoice {
	implicit object InvoiceFormat extends Format[Invoice] {
		def writes(invoice: Invoice): JsValue = {
			val invoiceSeq = Seq(
				"id" -> JsNumber(invoice.id),
				"publicId" -> JsString(invoice.publicId),
				"buyerName" -> JsString(invoice.buyerName),
				"buyerCity" -> JsString(invoice.buyerCity),
				"buyerCountry" -> JsString(invoice.buyerCountry),
				"buyerAddress" -> JsString(invoice.buyerAddress),
				"total" -> JsNumber(invoice.total),
				"currency" -> JsString(invoice.currency)
			)
			
			JsObject(invoiceSeq)
		}
        
		def reads(json: JsValue): JsResult[Invoice] = {
			JsSuccess(new Invoice())
		}
	}

	def finder:Model.Finder[Long, Invoice] = new Model.Finder[Long, Invoice](classOf[Invoice]);
}