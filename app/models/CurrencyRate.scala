package models

import java.util._;
import javax.persistence._;
import javax.validation.constraints._;
import com.avaje.ebean.Model;
import play.data.format._;
import play.data.validation._;
import play.api.libs.json.Format
import play.api.libs.json.JsValue
import play.api.libs.json.JsNumber
import play.api.libs.json.JsString
import play.api.libs.json.JsObject
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsResult
import java.text.SimpleDateFormat

@Entity
class CurrencyRate extends Model {
	
	@Id
	var id: Int = _
	
	@NotNull
	var source: String = _
	
	@NotNull
	var target: String = _
	
	@NotNull
	var rate: Float = _
	
	@NotNull
	@Column(columnDefinition = "DATE")
	var day: Date = _

}

object CurrencyRate {
	
	implicit object CurrencyRateFormat extends Format[CurrencyRate] {
		val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
		
		def writes(currencyRate: CurrencyRate): JsValue = {
			JsObject(Seq(
				"id" -> JsNumber(currencyRate.id),
				"source" -> JsString(currencyRate.source),
				"target" -> JsString(currencyRate.target),
				"rate" -> JsNumber(currencyRate.rate),
				"day" -> JsString(format.format(currencyRate.day))
			))
		}

		def reads(json: JsValue): JsResult[CurrencyRate] = {
			JsSuccess(new CurrencyRate())
		}
	}
	
	def finder: Model.Finder[Long, CurrencyRate] = new Model.Finder[Long, CurrencyRate](classOf[CurrencyRate])
	
	def apply(source: String, target: String, rate: Float, day: Date): CurrencyRate = {
		val currencyRate: CurrencyRate = new CurrencyRate
		currencyRate.source = source
		currencyRate.target = target
		currencyRate.rate = rate
		currencyRate.day = day
		
		currencyRate
	}
	
}