package services.invoices

import javax.inject.Singleton
import java.io._
import models.invoice.Invoice
import controllers.viewinput.InvoiceViewInput
import javax.inject.Inject
import play.api.Configuration
import services.templates.ExternalTemplatesService
import services.translations.TranslationService
import io.github.cloudify.scala.spdf._
import services.currencies.CurrenciesService
import play.api.i18n.Lang
import play.api.i18n.Messages.Implicits._
import play.api.i18n.MessagesApi
import play.api.i18n.I18nSupport
import play.api.i18n.Messages
import controllers.viewinput.InvoicesMonthlyViewInput
import services.InvoicesService

@Singleton
class PdfService @Inject() (
	configuration: Configuration,
	externalTemplatesService: ExternalTemplatesService,
	translationService: TranslationService,
	implicit val invoicesService: InvoicesService,
	implicit val currenciesService: CurrenciesService,
	val messagesApi: MessagesApi
) extends I18nSupport {
	
	val baseUrl: String = configuration.underlying.getString("felicia.baseUrl")
	
	def getInvoiceAsPdf(invoice: Invoice, currency: String, language: String): ByteArrayOutputStream = {
		implicit val lang: Lang = Lang(language)
		
		val pdf: Pdf = Pdf(new PdfConfig {
			orientation := Portrait
			pageSize := "A4"
			marginTop := "2cm"
			marginBottom := "2cm"
			marginLeft := "2cm"
			marginRight := "2cm"
		})
		
		val pageSource: String = views.html.print.invoices.invoice(
			new InvoiceViewInput(baseUrl, externalTemplatesService, currenciesService, translationService, invoice, currency, language)
		).toString()
		
		val outputStream: ByteArrayOutputStream = new ByteArrayOutputStream
		pdf.run(pageSource, outputStream)
		
		outputStream
	}

	def getInvoicesReportAsPdf(year: Int, month: Int, currency: String, language: String): ByteArrayOutputStream = {
		implicit val lang: Lang = Lang(language)
		
		val pdf: Pdf = Pdf(new PdfConfig {
			orientation := Landscape
			pageSize := "A4"
			marginTop := "2cm"
			marginBottom := "2cm"
			marginLeft := "2cm"
			marginRight := "2cm"
		})
		
		val pageSource: String = views.html.print.invoices.monthReport(
			InvoicesMonthlyViewInput(baseUrl, year, month, currency, language)
		).toString()
		
		val outputStream: ByteArrayOutputStream = new ByteArrayOutputStream
		pdf.run(pageSource, outputStream)
		
		outputStream
	}
	
	def getInvoicePdfFileName(invoice: Invoice, language: String): String = {
		implicit val lang: Lang = Lang(language)
		
		Messages("invoice")
			.concat("-")
			.concat(if (invoice.publicIdNumber < 10) "00" else if (invoice.publicIdNumber < 100) "0" else "")
			.concat(invoice.publicId.replace('/', '-'))
			.concat("-")
			.concat(lang.code)
			.concat(".pdf")
	}
	
	def getInvoicesReportPdfFileName(year: Int, month: Int, language: String): String = {
		implicit val lang: Lang = Lang(language)
		
		Messages("invoices.report.monthly.filename.pdf")
			.concat("-")
			.concat(year.toString())
			.concat("-")
			.concat(if (month < 10) "0" else "")
			.concat(month.toString())
			.concat("-")
			.concat(lang.code)
			.concat(".pdf")
	}
	
}