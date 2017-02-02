package models.translations

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
class TranslationValue extends Model {
	
	@Id
	var id: Int = _
	
	@NotNull
	var objectId: Int = _
	
	@NotNull
	var field: String = _
	
	@NotNull
	var language: String = _
	
	@Column(columnDefinition = "TEXT")
	var value: String = _
	
	@ManyToOne()
	var objectType: TranslationObject = _

}

object TranslationValue {
	
	implicit object TranslationValueFormat extends Format[TranslationValue] {
		def writes(translationValue: TranslationValue): JsValue = {
			
			val invoiceSeq = Seq(
				"language" -> Json.toJson(translationValue.language),
				"value" -> JsString(translationValue.value)
			)
			
			JsObject(invoiceSeq)
		}
        
		def reads(json: JsValue): JsResult[TranslationValue] = {
			JsSuccess(new TranslationValue())
		}
	}

	def finder: Model.Finder[Long, TranslationValue] = new Model.Finder[Long, TranslationValue](classOf[TranslationValue]);

}