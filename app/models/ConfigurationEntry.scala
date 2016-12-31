package models

import java.util._;
import javax.persistence._;
import javax.validation.constraints._;

import com.avaje.ebean.Model;
import play.data.format._;
import play.data.validation._;
import models.user.UserSession
import play.api.libs.json.Json
import play.api.libs.json._

@Entity
class ConfigurationEntry extends Model {
	@Id
	var id:Int = _

	@NotNull
	var sectionName:String = _

	@NotNull
	var name:String = _

	@NotNull
	var symbol:String = _

	@NotNull
	var typeId:String = _

	@Column(columnDefinition = "TEXT")
	var typeConfiguration:String = _

	@Column(columnDefinition = "TEXT")
	var value:String = _
}

object ConfigurationEntry {
	implicit object ConfigurationEntryFormat extends Format[ConfigurationEntry] {

        def writes(entry: ConfigurationEntry): JsValue = {
            val entrySeq = Seq(
            	"id" -> JsNumber(entry.id),
					"symbol" -> JsString(entry.symbol),
					"name" -> JsString(entry.name),
					"sectionName" -> JsString(entry.sectionName),
					"typeId" -> JsString(entry.typeId),
					"typeConfiguration" -> JsString(entry.typeConfiguration),
					"value" -> JsString(entry.value)
            )
            JsObject(entrySeq)
        }
        
      def reads(json: JsValue): JsResult[ConfigurationEntry] = {
			JsSuccess(new ConfigurationEntry())
   	}
	}
	
	def finder:Model.Finder[Long, ConfigurationEntry] = new Model.Finder[Long, ConfigurationEntry](classOf[ConfigurationEntry])
}