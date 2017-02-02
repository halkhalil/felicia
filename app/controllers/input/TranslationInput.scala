package controllers.input

import play.api.libs.json.Format
import play.api.libs.json.JsValue
import play.api.libs.json.JsResult
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber

case class TranslationInput(language: String, value: String)

object TranslationInput {
	implicit object TranslationInputFormat extends Format[TranslationInput] {
		def writes(translationInput: TranslationInput): JsValue = {
			JsObject(Seq(
				"value" -> JsString(translationInput.value),
				"language" -> JsString(translationInput.language)
			))
		}

		def reads(json: JsValue): JsResult[TranslationInput] = {
			JsSuccess(new TranslationInput(
				(json \ "language").as[String],
				(json \ "value").as[String]
			))
		}
	}
}