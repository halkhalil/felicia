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
import models.invoice.InvoicePart
import javax.inject.Inject
import services.supporting.DatesService
import controllers.input.InvoiceUpdateInput

@Singleton
class InvoicesService @Inject() (datesService: DatesService) {
	
	def getAll(year: Int, month: Int): List[Invoice] = {
		Invoice.finder.where()
			.ge("issueDate", datesService.firstDayOfTheMonth(year, month))
			.lt("issueDate", datesService.firstDayOfTheNextMonth(year, month))
			.orderBy("publicIdNumber asc")
			.findList().asScala.toList
	}
	
	def getRecent(limit: Int): List[Invoice] = {
		Invoice.finder
			.orderBy("id desc")
			.setMaxRows(limit)
			.findList().asScala.toList
	}
	
	def get(id: Int): Option[Invoice] = {
		val invoice: Invoice = Invoice.finder.where().eq("id", id).findUnique()
		
		if (invoice != null) Some(invoice) else None
	}
	
	def getLast(year: Int): Option[Invoice] = {
		val invoice: Invoice = Invoice.finder.where()
			.ge("issueDate", datesService.firstDayOfYear(year))
			.lt("issueDate", datesService.firstDayOfYear(year + 1))
			.orderBy("publicIdNumber desc")
			.setMaxRows(1).findUnique()
		
		if (invoice != null) Some(invoice) else None
	}
	
	def getParts(invoice: Invoice): List[InvoicePart] = {
		InvoicePart.finder.where().eq("invoice", invoice).findList().asScala.toList
	}
	
	def delete(invoice: Invoice): Invoice = {
		getParts(invoice).foreach { invoicePart => invoicePart.delete() }
		
		invoice.delete()
		
		invoice
	}

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
		
		invoice.publicIdNumber = newPublicIdNumber(invoiceInput.issueDate)
		invoice.publicId = newPublicId(invoice.publicIdNumber, invoiceInput.issueDate)
		invoice.currency = ConfigurationService.getTextNonEmpty("invoices.currency").get
		invoice.buyerIsCompany = invoiceInput.buyerIsCompany
		invoice.buyerName = invoiceInput.buyerName
		invoice.buyerAddress = invoiceInput.buyerAddress
		invoice.buyerZip = invoiceInput.buyerZip
		invoice.buyerCity = invoiceInput.buyerCity
		invoice.buyerCountry = invoiceInput.buyerCountry
		invoice.buyerTaxId = invoiceInput.buyerTaxId
		invoice.buyerEmail = invoiceInput.buyerEmail
		invoice.buyerPhone = invoiceInput.buyerPhone
		
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
	
	def validationErrorOnUpdate(invoiceUpdateInput: InvoiceUpdateInput): Option[String] = {
		if (invoiceUpdateInput.buyerName.trim().length() == 0) return Some("Buyer name cannot be empty")
		if (invoiceUpdateInput.buyerAddress.trim().length() == 0) return Some("Buyer address cannot be empty")
		if (invoiceUpdateInput.buyerCity.trim().length() == 0) return Some("Buyer city cannot be empty")
		if (invoiceUpdateInput.buyerZip.trim().length() == 0) return Some("Buyer zip code cannot be empty")
		if (invoiceUpdateInput.buyerCountry.trim().length() == 0) return Some("Buyer country cannot be empty")
		
		if (invoiceUpdateInput.paymentMethod == null) return Some("Payment method is unknown")
		
		None
	}

	def update(invoice: Invoice, invoiceUpdateInput: InvoiceUpdateInput): Invoice = {
		invoice.buyerIsCompany = invoiceUpdateInput.buyerIsCompany
		invoice.buyerName = invoiceUpdateInput.buyerName
		invoice.buyerAddress = invoiceUpdateInput.buyerAddress
		invoice.buyerZip = invoiceUpdateInput.buyerZip
		invoice.buyerCity = invoiceUpdateInput.buyerCity
		invoice.buyerCountry = invoiceUpdateInput.buyerCountry
		invoice.buyerTaxId = invoiceUpdateInput.buyerTaxId
		invoice.buyerEmail = invoiceUpdateInput.buyerEmail
		invoice.buyerPhone = invoiceUpdateInput.buyerPhone
		invoice.paymentMethod = invoiceUpdateInput.paymentMethod
		invoice.save()
		
		invoice
	}
	
	private def newPublicId(newPublicIdNumber: Int, issueDate: Date): String = {
		val calendar: Calendar = Calendar.getInstance()
		calendar.setTime(issueDate)
		val year: Int = calendar.get(Calendar.YEAR);
		val month: Int = calendar.get(Calendar.MONTH) + 1

		newPublicIdNumber + "/" + month + "/" + year
	}
	
	private def newPublicIdNumber(issueDate: Date): Int = {
		val calendar: Calendar = Calendar.getInstance()
		calendar.setTime(issueDate)
		val year: Int = calendar.get(Calendar.YEAR);
		
		Invoice.finder.where()
			.ge("issueDate", datesService.firstDayOfYear(year))
			.lt("issueDate", datesService.firstDayOfYear(year + 1))
			.findRowCount() + 1
	}
	
	private def checkIssueDate(issueDate: Date): Boolean = {
		val calendar: Calendar = Calendar.getInstance()
		calendar.setTime(issueDate)
		val year: Int = calendar.get(Calendar.YEAR);
		val month: Int = calendar.get(Calendar.MONTH) + 1
		
		month == 12 || 
			Invoice.finder.where()
				.ge("issueDate", datesService.firstDayOfTheNextMonth(issueDate))
				.lt("issueDate", datesService.firstDayOfYear(year + 1))
				.findRowCount() == 0
	}

}