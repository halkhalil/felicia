package services.currencies

import javax.inject.Singleton
import services.currencies.suppliers.NbpSupplier
import javax.inject.Inject
import services.currencies.suppliers.Supplier
import scala.concurrent.ExecutionContext
import models.CurrencyRate
import play.Logger
import java.util.Date
import scala.collection.JavaConverters._

@Singleton
class CurrenciesService @Inject() (nbpSupplier: NbpSupplier)(implicit context: ExecutionContext) {
	
	def getAll(day: Date): List[CurrencyRate] = {
		CurrencyRate.finder.where()
			.eq("day", day)
			.orderBy("source asc")
			.findList().asScala.toList
	}
	
	def load = {
		Logger.info("Starting currency rates update")
		nbpSupplier.get().foreach { future =>
			future.map { list =>
				list.foreach { currencyInput =>
					getByCurrencyInput(currencyInput).map { currencyRate => 
						currencyRate.rate = currencyInput.rate
						currencyRate.save()
					}.getOrElse {
						CurrencyRate(currencyInput.source, currencyInput.target, currencyInput.rate, currencyInput.date).save()
					}
				}
			}
		}
	}
	
	def getByCurrencyInput(currencyInput: Supplier.CurrencyInput): Option[CurrencyRate] = {
		val currencyRate: CurrencyRate = CurrencyRate.finder.where()
			.eq("source", currencyInput.source)
			.eq("target", currencyInput.target)
			.eq("day", currencyInput.date)
			.findUnique()
			
		if (currencyRate != null) Some(currencyRate) else None
	}
	
}