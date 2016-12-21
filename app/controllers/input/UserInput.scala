package controllers.input

import play.data.format._;
import play.data.validation._;
import play.api.libs.json._

case class UserInput(name: String)

object UserInput {
	implicit object UserInputFormat extends Format[UserInput] {
		def writes(userInput: UserInput): JsValue = {
			JsObject(Seq(
				"name" -> JsString(userInput.name)
			))
		}
		
		def reads(json: JsValue): JsResult[UserInput] = {
			JsSuccess(new UserInput((json \ "name").as[String]))
		}
    }
}