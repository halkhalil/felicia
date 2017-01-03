package services

import play.api.libs.json._
import javax.inject._
import models.user.UserSession

@Singleton
class ApplicationService @Inject() (paymentMethodsService: PaymentMethodsService)  {
	
	def frontEndConfiguration(userSessionId: Option[String]): JsValue = {
		val data: JsObject = Json.obj(
			"paymentMethods" -> paymentMethodsService.getAll(),
			"currencies" -> currencies,
			"units" -> units,
			"invoices" -> invoices
		)
		
		userSessionId.map { sessionId =>
			val userSession: UserSession = UserSession.finder.where().eq("sessionId", sessionId).findUnique()
			
			if (userSession != null && userSession.user != null) {
				return data + (
					"user" -> Json.toJson(userSession.user)
				)
			}
		}
		
		data
	}
	
	val currencies:List[String] = List(
		"AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", 
		"BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", 
		"BWP", "BYR", "BZD", "CAD", "CDF", "CHF", "CLF", "CLP", "CNY", "COP", "CRC", 
		"CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ETB", "EUR", "FJD", 
		"FKP", "GBP", "GEL", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", 
		"HRK", "HTG", "HUF", "IDR", "ILS", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", 
		"JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", 
		"LAK", "LBP", "LKR", "LRD", "LSL", "LTL", "LVL", "LYD", "MAD", "MDL", "MGA", 
		"MKD", "MMK", "MNT", "MOP", "MRO", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", 
		"NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", 
		"PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", 
		"SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD", "STD", "SVC", "SYP", "SZL", 
		"THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", 
		"USD", "UYU", "UZS", "VEF", "VND", "VUV", "WST", "XAF", "XCD", "XDR", "XOF", 
		"XPF", "YER", "ZAR", "ZMK", "ZWL"
	)
	
	val units: List[String] = List(
		"item", "hour", "month", "year"
	)
	
	val invoices: JsObject = {
		Json.obj(
			"minYear" -> 2016,
			"maxYear" -> 2017
		)
	}
}