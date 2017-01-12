package services.currencies.suppliers

import scala.concurrent.Future
import java.util.Date

object Supplier {
	case class CurrencyInput(source: String, target: String, rate: Float, date: Date)
}

trait Supplier {
	
	def get(): List[Future[List[Supplier.CurrencyInput]]]
	
}