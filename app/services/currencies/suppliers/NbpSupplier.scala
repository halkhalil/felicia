package services.currencies.suppliers

import scala.concurrent.Future
import scala.concurrent.duration._

import java.util.Date
import javax.inject.Singleton
import play.api.libs.ws.WSClient
import javax.inject.Inject
import play.api.libs.ws.WSRequest
import play.api.libs.ws.WSResponse

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.ExecutionContext
import play.api.libs.json.Format
import play.api.libs.json.JsObject
import play.api.libs.json.JsValue
import play.api.libs.json.JsNull
import play.api.libs.json.JsResult
import play.api.libs.json.JsSuccess

import play.api.Logger
import models.CurrencyRate

case class NbpRate(date: Date, rate: Double)
object NbpRate {
	implicit object NbpRateFormat extends Format[NbpRate] {
		def writes(nbpRate: NbpRate): JsValue = {
			JsNull
		}
		
		def reads(json: JsValue): JsResult[NbpRate] = {
			JsSuccess(new NbpRate(
				(json \ "effectiveDate").as[Date],
				(json \ "mid").as[Double]
			))
		}
	}
}

@Singleton
class NbpSupplier @Inject() (ws: WSClient)(implicit context: ExecutionContext) extends Supplier {
	var SourceCurrency: String = "PLN"
	val Currencies: List[String] = List("USD")
	val MaxValuesAtOnce: Int = 40
	
	def get(): List[Future[List[Supplier.CurrencyInput]]] = {
		Currencies.map { currency =>
			val endpoint: String =  s"http://api.nbp.pl/api/exchangerates/rates/a/$currency/last/$MaxValuesAtOnce/"
			val request: WSRequest = ws.url(endpoint)
			
			val complexRequest: WSRequest = request
				.withQueryString("format" -> "json")
				.withHeaders("Accept" -> "application/json")
				.withFollowRedirects(true)
				.withRequestTimeout(10000.millis)
			val futureResponse: Future[WSResponse] = complexRequest.get()
			
			val result: Future[JsResult[List[NbpRate]]] = futureResponse.map { response =>
				(response.json \ "rates").validate[List[NbpRate]]
			}
			
			result onFailure {
			  case t => {
			 	  Logger.error("Exception with NbpSupplier", t)
			  }
			}
			
			result.map { jsResult =>
				jsResult.get.map { rate =>
					val rateValue: Int = (BigDecimal(rate.rate) * CurrencyRate.MultiplerValue).setScale(0, BigDecimal.RoundingMode.HALF_UP).toInt
					new Supplier.CurrencyInput(SourceCurrency, currency, rateValue, rate.date)
				}
			}
		}
	}
}