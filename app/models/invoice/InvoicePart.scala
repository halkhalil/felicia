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
	var quantity: Float = _
	
	@NotNull
	var unitPrice: Int = _
	
	@NotNull
	var total: Int = _
	
	@ManyToOne()
	var invoice: Invoice = _
}