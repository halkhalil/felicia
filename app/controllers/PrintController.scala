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
import controllers.viewinput.InvoiceViewInput
import io.github.cloudify.scala.spdf._
import java.io._
import java.net._
import play.api.http.HttpEntity
import akka.stream.scaladsl.FileIO
import akka.stream.scaladsl.Source
import akka.util.ByteString
import play.api.i18n.Messages
import java.util.Calendar

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

		invoicesService.get(id).map { invoice =>
			Ok(
				views.html.print.invoices.invoice(
					new InvoiceViewInput(baseUrl(request), externalTemplatesService, currenciesService, invoice, targetCurrency, language)
				)
			)
		}.getOrElse(notFound("Invoice does not exist"))
	}
	
	/**
	* Return PDF invoice file as an attachment.
	*/
	def invoicePdf(id: Int, targetCurrency: String, language: String) = (UserAction andThen AuthorizationCheckAction) { request =>
		implicit val lang = Lang(language) // this implicit value is passed to the template and used by Messages objects
		
		invoicesService.get(id).map { invoice =>
			val pdf: Pdf = Pdf(new PdfConfig {
				orientation := Portrait
				pageSize := "A4"
				marginTop := "2cm"
				marginBottom := "2cm"
				marginLeft := "2cm"
				marginRight := "2cm"
			})
			
			val page: String = views.html.print.invoices.invoice(
				new InvoiceViewInput(baseUrl(request), externalTemplatesService, currenciesService, invoice, targetCurrency, language)
			).toString()

			val outputStream: ByteArrayOutputStream = new ByteArrayOutputStream
			pdf.run(page, outputStream)
			
			Result(
				header = ResponseHeader(200, Map.empty),
				body = HttpEntity.Strict(ByteString.fromArray(outputStream.toByteArray()), Some("application/pdf"))
			).withHeaders(
				"Content-disposition" -> "attachment; filename=".concat(pdfFileName(invoice))
			)
		}.getOrElse(notFound("Invoice does not exist"))
	}

	private def baseUrl(request: Request[AnyContent]): String = "http".concat(if (request.secure) "s" else "").concat("://").concat(request.host)
	
	private def pdfFileName(invoice: Invoice)(implicit lang: Lang): String = {
		Messages("invoice")
			.concat("-")
			.concat(invoice.publicId.replace('/', '-'))
			.concat(".pdf")
	}
	
}
