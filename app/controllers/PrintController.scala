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

@Singleton
class PrintController @Inject() (
		implicit val invoicesService: InvoicesService,
		implicit val currenciesService: CurrenciesService,
		authenticationService: AuthenticationService, 
		usersService: UserService,
		val messagesApi: MessagesApi,
		externalTemplatesService: ExternalTemplatesService
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
		implicit val lang = Lang(language) // this implicit value is passed to the template and used by Messages objects
		
		val pdf: Pdf = Pdf(new PdfConfig {
			orientation := Landscape
			pageSize := "A4"
			marginTop := "2cm"
			marginBottom := "2cm"
			marginLeft := "2cm"
			marginRight := "2cm"
		})
		
		val page: String = views.html.print.invoices.monthReport(
			InvoicesMonthlyViewInput(baseUrl(request), year, month, targetCurrency, language)
		).toString()
		
		val outputStream: ByteArrayOutputStream = new ByteArrayOutputStream
		pdf.run(page, outputStream)
		
		Result(
			header = ResponseHeader(200, Map.empty),
			body = HttpEntity.Strict(ByteString.fromArray(outputStream.toByteArray()), Some("application/pdf"))
		).withHeaders(
			"Content-disposition" -> "attachment; filename=".concat(invoicesReportPdfFileName(year, month))
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
	* Returns PDF invoice file as an attachment.
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
	
	private def invoicesReportPdfFileName(year: Int, month: Int)(implicit lang: Lang): String = {
		Messages("invoices.report.monthly.filename.pdf")
			.concat("-")
			.concat(year.toString())
			.concat("-")
			.concat(if (month < 10) "0" else "")
			.concat(month.toString())
			.concat(".pdf")
	}
	
}
