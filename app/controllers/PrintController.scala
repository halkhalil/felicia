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
import models.invoice.InvoicePart
import services.templates.ExternalTemplatesService

@Singleton
class PrintController @Inject() (
		invoicesService: InvoicesService, currenciesService: CurrenciesService, authenticationService: AuthenticationService, 
		usersService: UserService, val messagesApi: MessagesApi, externalTemplatesService: ExternalTemplatesService
	)
	extends BaseController(authenticationService, usersService) with I18nSupport {

	/**
	* Renders invoices monthly report.
	*/
	def invoicesReport(year: Int, month: Int, targetCurrency: String, language: String) = (UserAction andThen AuthorizationCheckAction) { request =>
		implicit val lang = Lang(language) // this implicit value is passed to the template and used by Messages objects
		val targetCurrencyUpperCase = targetCurrency.toUpperCase()
		val invoices: List[Invoice] = invoicesService.getAll(year, month)
		val locale = new Locale(language, language)
		val pricesFormatter = NumberFormat.getIntegerInstance(locale)
		pricesFormatter.setMinimumFractionDigits(2)
		pricesFormatter.setMaximumFractionDigits(2)
		val curencyRateFormatter = NumberFormat.getIntegerInstance(locale)
		curencyRateFormatter.setMinimumFractionDigits(4)
		curencyRateFormatter.setMaximumFractionDigits(4)
		
		def currencyRate(invoice: Invoice): Option[BigDecimal] = {
			if (targetCurrencyUpperCase == invoice.currency.toUpperCase())
				Some(BigDecimal(1))
			else
				currenciesService.getFromPreviousDay(invoice.issueDate, targetCurrency, invoice.currency).map { currencyRate => 
					Option(BigDecimal(currencyRate.rate) / CurrencyRate.MultiplerValue)
				}.getOrElse(None)
		}
		
		def currencyRateAsString(invoice: Invoice): String = {
			currencyRate(invoice).map { rateValue =>
				curencyRateFormatter.format(rateValue)
			}.getOrElse("--")
		}
		
		def amountConverted(invoice: Invoice): Option[BigDecimal] = {
			currencyRate(invoice).map { rateValue =>
				Some(BigDecimal(invoice.total) / Invoice.Multipler * rateValue)
			}.getOrElse(None)
		}
		
		def amountConvertedAsString(invoice: Invoice): String = {
			amountConverted(invoice).map { amount =>
				pricesFormatter.format(amount).concat(" ").concat(targetCurrencyUpperCase)
			}.getOrElse("--")
		}
		
		def amountAsString(invoice: Invoice): String = {
			pricesFormatter.format(BigDecimal(invoice.total) / Invoice.Multipler).concat(" ").concat(invoice.currency)
		}
		
		def totalReportAmountAsString(invoices: List[Invoice]): String = {
			pricesFormatter.format(
				invoices.map { invoice =>
					amountConverted(invoice).map { amount =>
						amount
					}.getOrElse(BigDecimal(0))
				}.sum
			).concat(" ").concat(targetCurrencyUpperCase)
		}

		Ok(
			views.html.print.invoices.monthReport(
				invoices, year, month, targetCurrencyUpperCase, amountAsString, currencyRateAsString, amountConvertedAsString, totalReportAmountAsString(invoices)
			)
		)
	}
	
	/**
	* Renders invoice view.
	*/
	def invoice(id: Int, targetCurrency: String, language: String) = (UserAction andThen AuthorizationCheckAction) { request =>
		implicit val lang = Lang(language) // this implicit value is passed to the template and used by Messages objects
		val targetCurrencyUpperCase = targetCurrency.toUpperCase()
		val invoiceOption: Option[Invoice] = invoicesService.get(id)
		val locale = new Locale(language, language)
		val pricesFormatter = NumberFormat.getIntegerInstance(locale)
		pricesFormatter.setMinimumFractionDigits(2)
		pricesFormatter.setMaximumFractionDigits(2)
		val quantityFormatter = NumberFormat.getIntegerInstance(locale)
		quantityFormatter.setMaximumFractionDigits(2)
		val curencyRateFormatter = NumberFormat.getIntegerInstance(locale)
		curencyRateFormatter.setMinimumFractionDigits(4)
		curencyRateFormatter.setMaximumFractionDigits(4)
		
		
		def quantityAsString(invoicePart: InvoicePart): String = {
			quantityFormatter.format(BigDecimal(invoicePart.quantity) / InvoicePart.Multipler)
		}
		
		def unitPriceAsString(invoicePart: InvoicePart): String = {
			pricesFormatter.format(BigDecimal(invoicePart.unitPrice) / InvoicePart.Multipler).concat(" ").concat(invoiceOption.get.currency)
		}
		
		def totalAsString(invoicePart: InvoicePart): String = {
			pricesFormatter.format(BigDecimal(invoicePart.total) / InvoicePart.Multipler).concat(" ").concat(invoiceOption.get.currency)
		}
		
		def invoiceTotalAsString(invoice: Invoice): String = {
			pricesFormatter.format(BigDecimal(invoice.total) / Invoice.Multipler).concat(" ").concat(invoice.currency)
		}
		
		def currencyRate(invoice: Invoice): Option[BigDecimal] = {
			if (targetCurrencyUpperCase == invoice.currency.toUpperCase())
				Some(BigDecimal(1))
			else
				currenciesService.getFromPreviousDay(invoice.issueDate, targetCurrency, invoice.currency).map { currencyRate => 
					Option(BigDecimal(currencyRate.rate) / CurrencyRate.MultiplerValue)
				}.getOrElse(None)
		}
		
		def currencyRateAsString(invoice: Invoice): Option[String] = {
			currencyRate(invoice).map { rateValue =>
				Some(curencyRateFormatter.format(rateValue))
			}.getOrElse(None)
		}
		
		def currencyRateDateAsDate(invoice: Invoice): Option[Date] = {
			currenciesService.getFromPreviousDay(invoice.issueDate, targetCurrency, invoice.currency).map { currencyRate => 
				Option(currencyRate.day)
			}.getOrElse(None)
		}
		
		def invoiceTotalConverted(invoice: Invoice): Option[BigDecimal] = {
			currencyRate(invoice).map { rateValue =>
				Some(BigDecimal(invoice.total) / Invoice.Multipler * rateValue)
			}.getOrElse(None)
		}
		
		def invoiceTotalConvertedAsString(invoice: Invoice): Option[String] = {
			invoiceTotalConverted(invoice).map { amount =>
				Some(pricesFormatter.format(amount).concat(" ").concat(targetCurrencyUpperCase))
			}.getOrElse(None)
		}
		
		invoiceOption.map { invoice =>
			val paymentSupplementary: Option[String] = externalTemplatesService.get(
				"payments.".concat(invoice.paymentMethod.symbol).concat("-").concat(language.toLowerCase())
			).map { template =>
				Some(template)
			}.getOrElse(None)
			
			Ok(
				views.html.print.invoices.invoice(
					invoice, targetCurrencyUpperCase, quantityAsString, unitPriceAsString,
					totalAsString, invoiceTotalAsString(invoice), invoiceTotalConvertedAsString(invoice), currencyRateAsString(invoice),
					currencyRateDateAsDate(invoice), paymentSupplementary
				)
			)
		}.getOrElse(notFound("Invoice does not exist"))
	}

}
