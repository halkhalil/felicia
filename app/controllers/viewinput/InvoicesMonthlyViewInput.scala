package controllers.viewinput

import models.invoice.Invoice
import services.InvoicesService
import java.util.Locale
import java.text.NumberFormat
import services.currencies.CurrenciesService
import models.CurrencyRate

object InvoicesMonthlyViewInput {

	def apply(baseUrl: String, year: Int, month: Int, targetCurrency: String, language: String)(implicit invoicesService: InvoicesService, currenciesService: CurrenciesService): InvoicesMonthlyViewInput = {
		new InvoicesMonthlyViewInput(baseUrl, year, month, targetCurrency, language)
	}

}

class InvoicesMonthlyViewInput(
		val baseUrl: String,
		val year: Int,
		val month: Int,
		val targetCurrency: String,
		val language: String
	)(
		implicit invoicesService: InvoicesService, currenciesService: CurrenciesService
	) {
	
	val locale = new Locale(language, language)
	val pricesFormatter = NumberFormat.getIntegerInstance(locale)
	pricesFormatter.setMinimumFractionDigits(2)
	pricesFormatter.setMaximumFractionDigits(2)
	val curencyRateFormatter = NumberFormat.getIntegerInstance(locale)
	curencyRateFormatter.setMinimumFractionDigits(4)
	curencyRateFormatter.setMaximumFractionDigits(4)
	
	val targetCurrencyUpperCase = targetCurrency.toUpperCase()
	val invoices: List[Invoice] = invoicesService.getAll(year, month)
	
	def amount(invoice: Invoice): String = {
		pricesFormatter.format(BigDecimal(invoice.total) / Invoice.Multipler).concat(" ").concat(invoice.currency)
	}
	
	def currencyRateValue(invoice: Invoice): Option[BigDecimal] = {
		if (targetCurrencyUpperCase == invoice.currency.toUpperCase())
			Some(BigDecimal(1))
		else
			currenciesService.getFromPreviousDay(invoice.issueDate, targetCurrency, invoice.currency).map { currencyRate => 
				Option(BigDecimal(currencyRate.rate) / CurrencyRate.MultiplerValue)
			}.getOrElse(None)
	}
	
	def currencyRate(invoice: Invoice): String = {
		currencyRateValue(invoice).map { rateValue =>
			curencyRateFormatter.format(rateValue)
		}.getOrElse("--")
	}
	
	def amountConvertedValue(invoice: Invoice): Option[BigDecimal] = {
		currencyRateValue(invoice).map { rateValue =>
			Some(BigDecimal(invoice.total) / Invoice.Multipler * rateValue)
		}.getOrElse(None)
	}
	
	def amountConverted(invoice: Invoice): String = {
		amountConvertedValue(invoice).map { amount =>
			pricesFormatter.format(amount).concat(" ").concat(targetCurrencyUpperCase)
		}.getOrElse("--")
	}
	
	val totalReportAmount: String = {
		pricesFormatter.format(
			invoices.map { invoice =>
				amountConvertedValue(invoice).map { amount =>
					amount
				}.getOrElse(BigDecimal(0))
			}.sum
		).concat(" ").concat(targetCurrencyUpperCase)
	}

}