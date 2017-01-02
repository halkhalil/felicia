package services

import javax.inject.Singleton
import controllers.input.InvoiceInput
import models.invoice.Invoice
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date
import models.ConfigurationEntry
import services.application.ConfigurationService
import models.User
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters._
import models.invoice.InvoicePart
import models.invoice.InvoicePart

@Singleton
class InvoicesService {

	def validationErrorOnCreate(invoiceInput: InvoiceInput): Option[String] = {
		if (invoiceInput.buyerName.trim().length() == 0) return Some("Buyer name cannot be empty")
		if (invoiceInput.buyerAddress.trim().length() == 0) return Some("Buyer address cannot be empty")
		if (invoiceInput.buyerCity.trim().length() == 0) return Some("Buyer city cannot be empty")
		if (invoiceInput.buyerZip.trim().length() == 0) return Some("Buyer zip code cannot be empty")
		if (invoiceInput.buyerCountry.trim().length() == 0) return Some("Buyer country cannot be empty")
		
		if (!checkIssueDate(invoiceInput.issueDate)) return Some("Issue date is incorrect because there are invoices that have greater issue date")
		
		if (invoiceInput.paymentMethod == null) return Some("Payment method is unknown")
		
		if (ConfigurationService.getTextNonEmpty("invoices.currency").isEmpty) return Some("Main currency was not defined")
		if (ConfigurationService.getTextNonEmpty("invoices.place").isEmpty) return Some("Place of issue was not defined")	
		if (ConfigurationService.getTextNonEmpty("company.name").isEmpty) return Some("Seller name was not defined")
		if (ConfigurationService.getTextNonEmpty("company.address").isEmpty) return Some("Seller address was not defined")
		if (ConfigurationService.getTextNonEmpty("company.zip").isEmpty) return Some("Seller zip code was not defined")
		if (ConfigurationService.getTextNonEmpty("company.city").isEmpty) return Some("Seller city was not defined")
		if (ConfigurationService.getTextNonEmpty("company.country").isEmpty) return Some("Seller country was not defined")
		if (ConfigurationService.getTextNonEmpty("company.tax.id").isEmpty) return Some("Seller tax ID was not defined")
		
		if (invoiceInput.parts.length == 0) return Some("Invoice parts array cannot be empty")
		
		None
	}
	
	def create(invoiceInput: InvoiceInput, creator: User): Invoice = {
		val invoice: Invoice = new Invoice()
		
		invoice.publicId = newPublicId(invoiceInput.issueDate)
		invoice.currency = ConfigurationService.getTextNonEmpty("invoices.currency").get
		invoice.buyerIsCompany = invoiceInput.buyerIsCompany
		invoice.buyerName = invoiceInput.buyerName
		invoice.buyerAddress = invoiceInput.buyerAddress
		invoice.buyerZip = invoiceInput.buyerZip
		invoice.buyerCity = invoiceInput.buyerCity
		invoice.buyerCountry = invoiceInput.buyerCountry
		invoice.buyerTaxId = invoiceInput.buyerTaxId
		
		invoice.sellerName = ConfigurationService.getTextNonEmpty("company.name").get
		invoice.sellerAddress = ConfigurationService.getTextNonEmpty("company.address").get
		invoice.sellerZip = ConfigurationService.getTextNonEmpty("company.zip").get
		invoice.sellerCity = ConfigurationService.getTextNonEmpty("company.city").get
		invoice.sellerCountry = ConfigurationService.getTextNonEmpty("company.country").get
		invoice.sellerTaxId = ConfigurationService.getTextNonEmpty("company.tax.id").get
		
		invoice.placeOfIssue = ConfigurationService.getTextNonEmpty("invoices.place").get
		invoice.issueDate = invoiceInput.issueDate
		invoice.orderDate = invoiceInput.orderDate
		invoice.dueDate = invoiceInput.dueDate
		invoice.paymentMethod = invoiceInput.paymentMethod
		
		var totalInvoice: Int = 0
		val parts: ListBuffer[InvoicePart] = new ListBuffer[InvoicePart]()
		invoiceInput.parts.foreach { invoicePartInput => 
			val invoicePart: InvoicePart = new InvoicePart()
			invoicePart.name = invoicePartInput.name
			invoicePart.quantity = invoicePartInput.quantity
			invoicePart.unit = invoicePartInput.unit
			invoicePart.total = invoicePartInput.total
			invoicePart.unitPrice = invoicePartInput.unitPrice
			invoicePart.invoice = invoice
			
			parts += invoicePart
			totalInvoice += invoicePartInput.total
		}
		invoice.total = totalInvoice
		invoice.creator = creator
		invoice.save()
		
		// save invoice parts:
		parts.foreach { invoicePart =>
			invoicePart.save()	
		}
		
		invoice
	}
	
	private def newPublicId(issueDate: Date): String = {
		val calendar: Calendar = Calendar.getInstance()
		calendar.setTime(issueDate)
		
		val year: Int = calendar.get(Calendar.YEAR);
		val month: Int = calendar.get(Calendar.MONTH) + 1
		
		val calendarStart: Calendar = Calendar.getInstance()
		calendarStart.set(Calendar.YEAR, year)
		calendarStart.set(Calendar.MONTH, 0)
		calendarStart.set(Calendar.DAY_OF_MONTH, 1)
		calendarStart.set(Calendar.HOUR_OF_DAY, 0)
		calendarStart.set(Calendar.MINUTE, 0)
		calendarStart.set(Calendar.SECOND, 0)
		calendarStart.set(Calendar.MILLISECOND, 0)
		
		val firstDay: Date = calendarStart.getTime()
		
		calendarStart.set(Calendar.YEAR, year + 1)
		val lastDay: Date = calendarStart.getTime()
		var totalInCurrentYear: Int = Invoice.finder.where().ge("issueDate", firstDay).lt("issueDate", lastDay).findRowCount()
		
		totalInCurrentYear += 1
		
		totalInCurrentYear + "/" + month + "/" + year
	}
	
	private def checkIssueDate(issueDate: Date): Boolean = {
		val calendar: Calendar = Calendar.getInstance()
		calendar.setTime(issueDate)
		
		val issueDateMonth = calendar.get(Calendar.MONTH) + 1
		
		calendar.add(Calendar.MONTH, 1)
		calendar.set(Calendar.DAY_OF_MONTH, 1)
		calendar.set(Calendar.HOUR_OF_DAY, 0)
		calendar.set(Calendar.MINUTE, 0)
		calendar.set(Calendar.SECOND, 0)
		calendar.set(Calendar.MILLISECOND, 0)
		val firstDay: Date = calendar.getTime()
		
		calendar.set(Calendar.MONTH, 0)
		calendar.add(Calendar.YEAR, 1)
		val lastDay: Date = calendar.getTime()
		
		issueDateMonth == 12 || Invoice.finder.where().ge("issueDate", firstDay).lt("issueDate", lastDay).findRowCount() == 0
	}
}