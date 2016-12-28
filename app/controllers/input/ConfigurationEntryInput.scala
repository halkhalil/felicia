package controllers.input

import play.data.format._;
import play.data.validation._;
import play.api.libs.json._

case class ConfigurationEntryInput(symbol: String, value: String)

object ConfigurationEntryInput {
	implicit object ConfigurationEntryInputFormat extends Format[ConfigurationEntryInput] {
		def writes(configurationEntryInput: ConfigurationEntryInput): JsValue = {
			JsObject(Seq(
				"symbol" -> JsString(configurationEntryInput.symbol),
				"value" -> JsString(configurationEntryInput.value)
			))
		}
		
		def reads(json: JsValue): JsResult[ConfigurationEntryInput] = {
			JsSuccess(new ConfigurationEntryInput(
				(json \ "symbol").as[String],
				(json \ "value").as[String]
			))
		}
    }
}