package controllers.viewinput

import models.invoice.Invoice
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
import services.translations.TranslationService

class InvoiceViewInput(
		val baseUrl: String,
		externalTemplatesService: ExternalTemplatesService,
		currenciesService: CurrenciesService,
		translationService: TranslationService,
		val invoice: Invoice,
		val targetCurrency: String,
		language: String
	) {
	val targetCurrencyUpperCase = targetCurrency.toUpperCase()
	private val locale = new Locale(language, language)
	private val quantityFormatter = NumberFormat.getIntegerInstance(locale)
	quantityFormatter.setMaximumFractionDigits(2)
	private val pricesFormatter = NumberFormat.getIntegerInstance(locale)
	pricesFormatter.setMinimumFractionDigits(2)
	pricesFormatter.setMaximumFractionDigits(2)
	private val curencyRateFormatter = NumberFormat.getIntegerInstance(locale)
	curencyRateFormatter.setMinimumFractionDigits(4)
	curencyRateFormatter.setMaximumFractionDigits(4)
	
	val paymentDescription: String = externalTemplatesService.get(
		"payments.".concat(invoice.paymentMethod.symbol).concat("-description-html-").concat(language.toLowerCase())
	).map { template =>
		template
	}.getOrElse("")
	
	val paymentName: String = externalTemplatesService.get(
		"payments.".concat(invoice.paymentMethod.symbol).concat("-name-").concat(language.toLowerCase())
	).map { template =>
		template
	}.getOrElse(invoice.paymentMethod.name)
	
	def quantity(invoicePart: InvoicePart): String = {
		quantityFormatter.format(BigDecimal(invoicePart.quantity) / InvoicePart.Multipler)
	}
	
	def unitPrice(invoicePart: InvoicePart): String = {
		pricesFormatter.format(BigDecimal(invoicePart.unitPrice) / InvoicePart.Multipler).concat(" ").concat(invoice.currency)
	}
	
	def total(invoicePart: InvoicePart): String = {
		pricesFormatter.format(BigDecimal(invoicePart.total) / InvoicePart.Multipler).concat(" ").concat(invoice.currency)
	}
	
	val invoiceTotal: String = {
		pricesFormatter.format(BigDecimal(invoice.total) / Invoice.Multipler).concat(" ").concat(invoice.currency)
	}
	
	val currencyRate: Option[CurrencyRate] = currenciesService.getFromPreviousDay(invoice.issueDate, targetCurrency, invoice.currency)
	
	val currencyRateValue: Option[BigDecimal] = {
		if (targetCurrencyUpperCase == invoice.currency.toUpperCase())
			Some(BigDecimal(1))
		else
			currencyRate.map { currencyRate => 
				Some(BigDecimal(currencyRate.rate) / CurrencyRate.MultiplerValue)
			}.getOrElse(None)
	}
	
	val currencyRateFormatted: String = {
		currencyRate.map { currencyRate =>
			curencyRateFormatter.format(BigDecimal(currencyRate.rate) / CurrencyRate.MultiplerValue)
		}.getOrElse("")
	}
	
	val invoiceTotalConvertedValue: Option[BigDecimal] = {
		currencyRateValue.map { rateValue =>
			Some(BigDecimal(invoice.total) / Invoice.Multipler * rateValue)
		}.getOrElse(None)
	}
	
	val invoiceTotalConverted: Option[String] = {
		invoiceTotalConvertedValue.map { amount =>
			Some(pricesFormatter.format(amount).concat(" ").concat(targetCurrencyUpperCase))
		}.getOrElse(None)
	}
	
	def translation(invoicePart: InvoicePart, field: String): String = {
		translationService.get("InvoicePart", invoicePart.id, field, language).map { translationValue =>
			translationValue.value
		}.getOrElse("")
	}
	
}