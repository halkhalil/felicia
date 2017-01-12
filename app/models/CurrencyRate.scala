package models

import java.util._;
import javax.persistence._;
import javax.validation.constraints._;
import com.avaje.ebean.Model;
import play.data.format._;
import play.data.validation._;

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