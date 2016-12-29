package models

import java.util._;
import javax.persistence._;
import javax.validation.constraints._;

import com.avaje.ebean.Model;
import play.data.format._;
import play.data.validation._;
import models.user.UserSession
import play.api.libs.json.Json
import play.api.libs.json._

@Entity
class PaymentMethod extends Model {

	@Id
	var id:Int = _

	@NotNull
	var name:String = _

	@NotNull
	var symbol:String = _

}

object PaymentMethod {
	implicit object PaymentMethodFormat extends Format[PaymentMethod] {

		def writes(paymentMethod: PaymentMethod): JsValue = {
			val paymentMethodSeq = Seq(
         	"id" -> JsNumber(paymentMethod.id),
				"symbol" -> JsString(paymentMethod.symbol),
				"name" -> JsString(paymentMethod.name)
			)
         JsObject(paymentMethodSeq)
		}

      def reads(json: JsValue): JsResult[PaymentMethod] = {
			JsSuccess(new PaymentMethod())
   	}
	}
	
	def finder:Model.Finder[Long, PaymentMethod] = new Model.Finder[Long, PaymentMethod](classOf[PaymentMethod]);
}