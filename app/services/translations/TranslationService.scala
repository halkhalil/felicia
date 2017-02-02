package services.translations

import javax.inject.Singleton
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
import models.translations.TranslationObject
import models.translations.TranslationValue

@Singleton
class TranslationService {
	
	def save(objectType: String, objectId: Int, field: String, language: String, value: String): Boolean = {
		getType(objectType).map { translationObject =>
			var translationValue: TranslationValue = 
				TranslationValue.finder.where()
					.eq("objectType", translationObject)
					.eq("objectId", objectId)
					.eq("field", field)
					.eq("language", language)
					.findUnique()
			if (translationValue == null) {
				translationValue = new TranslationValue()
				translationValue.objectType = translationObject
				translationValue.objectId = objectId
				translationValue.field = field
				translationValue.language = language
			}
			
			translationValue.value = value
			translationValue.save()
			
			true
		}.getOrElse(false)
	}
	
	
	def get(objectType: String, objectId: Int, field: String, language: String): Option[TranslationValue] = {
		getType(objectType).map { translationObject =>
			val translationValue: TranslationValue = TranslationValue.finder.where()
				.eq("objectType", translationObject)
				.eq("objectId", objectId)
				.eq("field", field)
				.eq("language", language)
				.findUnique()

			if (translationValue != null) Some(translationValue) else None
		}.getOrElse(None)
	}
	
	def get(objectType: String, objectId: Int, field: String): List[TranslationValue] = {
		getType(objectType).map { translationObject =>
			TranslationValue.finder.where()
				.eq("objectType", translationObject)
				.eq("field", field)
				.eq("objectId", objectId)
				.findList().asScala.toList
		}.getOrElse(List())
	}
	
	def get(objectType: String, objectId: Int): List[TranslationValue] = {
		getType(objectType).map { translationObject =>
			TranslationValue.finder.where()
				.eq("objectType", translationObject)
				.eq("objectId", objectId)
				.findList().asScala.toList
		}.getOrElse(List())
	}
	
	def getType(objectType: String): Option[TranslationObject] = {
		val translationObject: TranslationObject = TranslationObject.finder.where().eq("name", objectType).findUnique()
		
		if (translationObject != null) Some(translationObject) else None
	}
	
	def delete(objectType: String, objectId: Int) = {
		get(objectType, objectId).map { translationValue =>
			translationValue.delete()
		}
	}
	
}