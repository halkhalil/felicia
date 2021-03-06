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
class TranslationObject extends Model {
	
	@Id
	var id: Int = _
	
	@NotNull
	var name: String = _
	
}

object TranslationObject {
	
	def finder: Model.Finder[Long, TranslationObject] = new Model.Finder[Long, TranslationObject](classOf[TranslationObject]);
	
}