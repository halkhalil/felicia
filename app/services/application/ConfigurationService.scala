package services.application

import models._
import javax.inject.Singleton
import scala.collection.JavaConverters._
import controllers.input.ConfigurationEntryInput

@Singleton
class ConfigurationService {
	def getAll():List[ConfigurationEntry] = {
		ConfigurationEntry.finder.all().asScala.toList
	}
	
	def update(entriesInput: List[ConfigurationEntryInput]):List[ConfigurationEntry] = {
		entriesInput.foreach { entryInput =>
			val configurationEntry: ConfigurationEntry = ConfigurationEntry.finder.where().eq("symbol", entryInput.symbol).findUnique()
			if (configurationEntry != null) {
				configurationEntry.value = entryInput.value
				configurationEntry.save()
			}
		}
		
		getAll()
	}
}

object ConfigurationService {
	
	def getTextNonEmpty(entrySymbol: String): Option[String] = {
		val entry: ConfigurationEntry = ConfigurationEntry.finder.where().eq("symbol", entrySymbol).findUnique()
		
		if (entry != null && entry.value.length() > 0) Some(entry.value) else None 
	}
	
}