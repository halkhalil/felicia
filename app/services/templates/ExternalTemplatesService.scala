package services.templates

import javax.inject.Singleton
import javax.inject.Inject
import play.api.Configuration
import play.Logger
import scala.io.Source
import java.nio.file.{Paths, Files}
import java.io.File

@Singleton
class ExternalTemplatesService @Inject() (configuration: Configuration) {
	val TemplatesPath: String = configuration.getString("felicia.templates.path").getOrElse(
		".".concat(File.separatorChar.toString()).concat("templates")
	)
	
	def get(id: String): Option[String] = {
		val fullPath: String = TemplatesPath.concat(File.separatorChar.toString()).concat(id.replace('.', File.separatorChar)).concat(".tpl")
		
		if (Files.exists(Paths.get(fullPath))) {
			Some(Source.fromFile(fullPath).mkString)
		} else {
			None
		}
	}
}