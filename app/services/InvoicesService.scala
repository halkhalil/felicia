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

@Singleton
class InvoicesService {

	def validationErrorOnCreate(invoiceInput: InvoiceInput): Option[String] = {
		if (invoiceInput.buyerName.trim().length() == 0) return Some("Buyer name cannot be empty")
		if (invoiceInput.buyerAddress.trim().length() == 0) return Some("Buyer address cannot be empty")
		if (invoiceInput.buyerCity.trim().length() == 0) return Some("Buyer city cannot be empty")
		if (invoiceInput.buyerZip.trim().length() == 0) return Some("Buyer zip code cannot be empty")
		if (invoiceInput.buyerCountry.trim().length() == 0) return Some("Buyer country cannot be empty")
		//if (invoiceInput.issueDate) return Some("Issue date cannot be empty")
		
		// TODO: validate dates
		
		if (ConfigurationService.getTextNonEmpty("invoices.currency").isEmpty) return Some("Main currency was not defined")
		if (ConfigurationService.getTextNonEmpty("invoices.place").isEmpty) return Some("Place of issue was not defined")
		
		if (ConfigurationService.getTextNonEmpty("company.name").isEmpty) return Some("Seller name was not defined")
		if (ConfigurationService.getTextNonEmpty("company.address").isEmpty) return Some("Seller address was not defined")
		if (ConfigurationService.getTextNonEmpty("company.zip").isEmpty) return Some("Seller zip code was not defined")
		if (ConfigurationService.getTextNonEmpty("company.city").isEmpty) return Some("Seller city was not defined")
		if (ConfigurationService.getTextNonEmpty("company.country").isEmpty) return Some("Seller country was not defined")
		if (ConfigurationService.getTextNonEmpty("company.tax.id").isEmpty) return Some("Seller tax ID was not defined")
			
		
		None
	}
	
	def create(invoiceInput: InvoiceInput): Invoice = {
		val invoice: Invoice = new Invoice()
		
		invoice.publicId = newPublicId
		invoice.currency = ConfigurationService.getTextNonEmpty("invoices.currency").get
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
		
		
		invoice.save()
		
		invoice
	}
	
	private def newPublicId: String = {
		val year: Int = Calendar.getInstance().get(Calendar.YEAR);
		val month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1
		
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
}