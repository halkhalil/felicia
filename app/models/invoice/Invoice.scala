package models.invoice

import java.util._;
import javax.persistence._;
import javax.validation.constraints._;
import scala.collection.JavaConverters._

import com.avaje.ebean.Model;
import play.data.format._;
import play.data.validation._;
import models.user.UserSession
import play.api.libs.json.Json
import play.api.libs.json._
import models.PaymentMethod
import play.api.data.format.Formats
import models.User
import java.text.SimpleDateFormat

@Entity
class Invoice extends Model {
	@Id
	var id:Int = _
	
	@NotNull
	@Column(unique = true)
	var publicId: String = _
	
	@NotNull
	var publicIdNumber: Int = _
	
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
	
	var buyerEmail: String = _
	
	var buyerPhone: String = _
	
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
		val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
		
		def writes(invoice: Invoice): JsValue = {
			val paymentMethod: Int = if (invoice.paymentMethod != null) invoice.paymentMethod.id else 0
			
			val invoiceSeq = Seq(
				"id" -> JsNumber(invoice.id),
				"publicId" -> JsString(invoice.publicId),
				"buyerIsCompany" -> JsBoolean(invoice.buyerIsCompany),
				"buyerName" -> JsString(invoice.buyerName),
				"buyerTaxId" -> JsString(invoice.buyerTaxId),
				"buyerCity" -> JsString(invoice.buyerCity),
				"buyerCountry" -> JsString(invoice.buyerCountry),
				"buyerAddress" -> JsString(invoice.buyerAddress),
				"buyerZip" -> JsString(invoice.buyerZip),
				"buyerEmail" -> JsString(invoice.buyerEmail),
				"buyerPhone" -> JsString(invoice.buyerPhone),
				"total" -> JsNumber(invoice.total),
				"currency" -> JsString(invoice.currency),
				"issueDate" -> JsString(format.format(invoice.issueDate)),
				"orderDate" -> JsString(format.format(invoice.orderDate)),
				"dueDate" -> JsString(format.format(invoice.dueDate)),
				"currency" -> JsString(invoice.currency),
				"placeOfIssue" -> JsString(invoice.placeOfIssue),
				"total" -> JsNumber(invoice.total),
				"paymentMethod" -> JsNumber(paymentMethod),
				"currency" -> JsString(invoice.currency),
				"sellerName" -> JsString(invoice.sellerName),
				"sellerAddress" -> JsString(invoice.sellerAddress),
				"sellerZip" -> JsString(invoice.sellerZip),
				"sellerCity" -> JsString(invoice.sellerCity),
				"sellerCountry" -> JsString(invoice.sellerCountry),
				"sellerTaxId" -> JsString(invoice.sellerTaxId),
				"parts" -> Json.toJson(invoice.invoiceParts.asScala.toList)
			)
			
			JsObject(invoiceSeq)
		}
        
		def reads(json: JsValue): JsResult[Invoice] = {
			JsSuccess(new Invoice())
		}
	}

	def finder: Model.Finder[Long, Invoice] = new Model.Finder[Long, Invoice](classOf[Invoice]);
}