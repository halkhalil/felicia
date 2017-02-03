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
import controllers.viewinput.InvoicesMonthlyViewInput
import services.translations.TranslationService
import services.invoices.PdfService

@Singleton
class PrintController @Inject() (
		implicit val invoicesService: InvoicesService,
		implicit val currenciesService: CurrenciesService,
		authenticationService: AuthenticationService, 
		usersService: UserService,
		translationService: TranslationService,
		val messagesApi: MessagesApi,
		externalTemplatesService: ExternalTemplatesService,
		pdfService: PdfService
	) extends BaseController(authenticationService, usersService) with I18nSupport {

	/**
	* Renders invoices monthly report.
	*/
	def invoicesReport(year: Int, month: Int, targetCurrency: String, language: String) = (UserAction andThen AuthorizationCheckAction) { request =>
		implicit val lang = Lang(language) // this implicit value is passed to the template and used by Messages objects
		
		Ok(
			views.html.print.invoices.monthReport(	
				InvoicesMonthlyViewInput(baseUrl(request), year, month, targetCurrency, language)
			)
		)
	}
	
	/**
	* Returns PDF invoices monthly report as an attachment.
	*/
	def invoicesReportPdf(year: Int, month: Int, targetCurrency: String, language: String) = (UserAction andThen AuthorizationCheckAction) { request =>
		val outputStream: ByteArrayOutputStream = pdfService.getInvoicesReportAsPdf(year, month, targetCurrency, language)
		val fileName: String = pdfService.getInvoicesReportPdfFileName(year, month, language)
		
		Result(
			header = ResponseHeader(200, Map.empty),
			body = HttpEntity.Strict(ByteString.fromArray(outputStream.toByteArray()), Some("application/pdf"))
		).withHeaders(
			"Content-disposition" -> "attachment; filename=".concat(fileName)
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
					new InvoiceViewInput(baseUrl(request), externalTemplatesService, currenciesService, translationService, invoice, targetCurrency, language)
				)
			)
		}.getOrElse(notFound("Invoice does not exist"))
	}
	
	/**
	* Returns PDF invoice file as an attachment.
	*/
	def invoicePdf(id: Int, targetCurrency: String, language: String) = (UserAction andThen AuthorizationCheckAction) { request =>
		invoicesService.get(id).map { invoice =>
			val outputStream: ByteArrayOutputStream = pdfService.getInvoiceAsPdf(invoice, targetCurrency, language)
			val fileName: String = pdfService.getInvoicePdfFileName(invoice, language)
			
			Result(
				header = ResponseHeader(200, Map.empty),
				body = HttpEntity.Strict(ByteString.fromArray(outputStream.toByteArray()), Some("application/pdf"))
			).withHeaders(
				"Content-disposition" -> "attachment; filename=".concat(fileName)
			)
		}.getOrElse(notFound("Invoice does not exist"))
	}

	private def baseUrl(request: Request[AnyContent]): String = "http".concat(if (request.secure) "s" else "").concat("://").concat(request.host)
	
}
