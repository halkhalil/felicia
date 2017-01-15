package controllers

import javax.inject._
import java.util.Locale
import java.text.NumberFormat
import play.api._
import play.api.mvc._
import services.ApplicationService
import play.api.libs.json.Json
import services.users.AuthenticationService
import services.users.UserService
import services.InvoicesService
import services.currencies.CurrenciesService
import java.util.Date
import models.invoice.Invoice
import models.CurrencyRate
import play.api.i18n.I18nSupport
import play.api.i18n.Messages.Implicits._
import play.api.i18n.MessagesApi
import play.api.i18n.Lang
import play.Logger

@Singleton
class PrintController @Inject() 
	(invoicesService: InvoicesService, currenciesService: CurrenciesService, authenticationService: AuthenticationService, usersService: UserService, val messagesApi: MessagesApi)
	extends BaseController(authenticationService, usersService) with I18nSupport {

	/**
	* Renders invoices monthly report.
	*/
	def invoicesReport(year: Int, month: Int, targetCurrency: String, language: String) = (UserAction andThen AuthorizationCheckAction) { request =>
		implicit val lang = Lang(language) // this implicit value is passed to the template and used by Messages objects
		val invoices: List[Invoice] = invoicesService.getAll(year, month)
		val locale = new Locale(language, language)
		val formatter = NumberFormat.getIntegerInstance(locale)
		formatter.setMinimumFractionDigits(2)
		formatter.setMaximumFractionDigits(2)
		
		def currencyRate(invoice: Invoice): Option[BigDecimal] = {
			if (targetCurrency.toUpperCase() == invoice.currency.toUpperCase())
				Some(BigDecimal(1))
			else
				currenciesService.getFromPreviousDay(invoice.issueDate, targetCurrency, invoice.currency).map { currencyRate => 
					Option(BigDecimal(currencyRate.rate) / CurrencyRate.MultiplerValue)
				}.getOrElse {
					None
				}
		}
		
		def currencyRateAsString(invoice: Invoice): String = {
			currencyRate(invoice).map { rateValue =>
				rateValue.toString()
			}.getOrElse {
				"--"
			}
		}
		
		def amountConverted(invoice: Invoice): Option[BigDecimal] = {
			currencyRate(invoice).map { rateValue =>
				Some(BigDecimal(invoice.total) / BigDecimal(100) * rateValue)
			}.getOrElse {
				None
			}
		}
		
		def amountConvertedAsString(invoice: Invoice): String = {
			amountConverted(invoice).map { amount =>
				formatter.format(amount).concat(" ").concat(targetCurrency.toUpperCase())
			}.getOrElse {
				"--"
			}
		}
		
		def amountAsString(invoice: Invoice): String = {
			formatter.format(BigDecimal(invoice.total) / BigDecimal(100)).concat(" ").concat(invoice.currency)
		}
		
		def totalReportAmount(invoices: List[Invoice]): String = {
			formatter.format(
				invoices.map { invoice =>
					amountConverted(invoice).map { amount =>
						amount
					}.getOrElse {
						BigDecimal(0)
					}
				}.sum
			).concat(" ").concat(targetCurrency.toUpperCase())
		}

		Ok(
			views.html.print.invoices.monthReport(
				invoices, year, month, targetCurrency.toUpperCase(), amountAsString, currencyRateAsString, amountConvertedAsString, totalReportAmount(invoices)
			)
		)	
	}

}
