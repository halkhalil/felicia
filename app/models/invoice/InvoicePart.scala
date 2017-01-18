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

@Entity
class InvoicePart extends Model {
	@Id
	var id:Int = _
	
	@NotNull
	@Column(columnDefinition = "TEXT")
	var name: String = _
	
	@NotNull
	var unit: String = _
	
	@NotNull
	var quantity: Int = _
	
	@NotNull
	var unitPrice: Int = _
	
	@NotNull
	var total: Int = _
	
	@ManyToOne()
	var invoice: Invoice = _
}

object InvoicePart {
	
	val Multipler: BigDecimal = BigDecimal(100)
	
	implicit object InvoicePartFormat extends Format[InvoicePart] {
		def writes(invoicePart: InvoicePart): JsValue = {
			val invoiceSeq = Seq(
				"id" -> JsNumber(invoicePart.id),
				"name" -> JsString(invoicePart.name),
				"unit" -> JsString(invoicePart.unit),
				"quantity" -> JsNumber(invoicePart.quantity),
				"unitPrice" -> JsNumber(invoicePart.unitPrice),
				"total" -> JsNumber(invoicePart.total)
			)
			
			JsObject(invoiceSeq)
		}
        
		def reads(json: JsValue): JsResult[InvoicePart] = {
			JsSuccess(new InvoicePart())
		}
	}
	
	def finder: Model.Finder[Long, InvoicePart] = new Model.Finder[Long, InvoicePart](classOf[InvoicePart]);
}