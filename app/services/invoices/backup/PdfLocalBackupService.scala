package services.invoices.backup

import javax.inject.Singleton
import play.Logger
import javax.inject.Inject
import play.api.Configuration
import java.nio.file.Paths
import java.nio.file.Files
import services.invoices.PdfService
import java.io.ByteArrayOutputStream
import services.InvoicesService
import java.util.Calendar
import models.invoice.Invoice

@Singleton
class PdfLocalBackupService @Inject() (configuration: Configuration, pdfService: PdfService, invoicesService: InvoicesService) {
	val RecentInvoicesAmount: Int = 100
	
	def run = {
		Logger.info("Starting local PDF invoices backup")
		
		val backupPath: String = configuration.getString("felicia.invoices.backupPath").getOrElse("backup/invoices")
		val path = Paths.get(backupPath)
		Files.createDirectories(path)
		
		val variants: List[Map[String, String]] = List(
			Map("language" -> "pl", "currency" -> "PLN"),
			Map("language" -> "en", "currency" -> "USD")
		)
		
		invoicesService.getRecent(RecentInvoicesAmount).foreach { invoice =>
			variants.foreach { variant =>
				val outputStream: ByteArrayOutputStream = pdfService.getInvoiceAsPdf(invoice, variant("currency"), variant("language"))
				val fileName: String = pdfService.getInvoicePdfFileName(invoice, variant("language"))
				val subDirectory: String = backupPath.concat("/").concat(invoiceDirectoryName(invoice))
				
				Files.createDirectories(Paths.get(subDirectory))
				Files.write(Paths.get(subDirectory.concat("/").concat(fileName)), outputStream.toByteArray())
			}
		}
		
		Logger.info("Backup complete")
	}
	
	private def invoiceDirectoryName(invoice: Invoice): String = {
		val calendar: Calendar = Calendar.getInstance()
		calendar.setTime(invoice.issueDate)
		val year: Int = calendar.get(Calendar.YEAR)
		val month: Int = calendar.get(Calendar.MONTH) + 1
		
		year.toString().concat("-").concat(if (month < 10) "0" else "").concat(month.toString())
	}
	
}