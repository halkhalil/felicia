package controllers.input

import play.data.format._;
import play.data.validation._;
import play.api.libs.json._

case class UserInput(name: String, password: String)

object UserInput {
	implicit object UserInputFormat extends Format[UserInput] {
		def writes(userInput: UserInput): JsValue = {
			JsObject(Seq(
				"name" -> JsString(userInput.name),
				"password" -> JsString(userInput.password)
			))
		}
		
		def reads(json: JsValue): JsResult[UserInput] = {
			JsSuccess(new UserInput(
				(json \ "name").as[String],
				(json \ "password").as[String]
			))
		}
    }
}