package controllers.input

import play.data.format._;
import play.data.validation._;
import play.api.libs.json._

case class UserCreateInput(login: String, name: String, password: String)

object UserCreateInput {
	implicit object UserInputFormat extends Format[UserCreateInput] {
		def writes(userCreateInput: UserCreateInput): JsValue = {
			JsObject(Seq(
				"login" -> JsString(userCreateInput.login),
				"name" -> JsString(userCreateInput.name),
				"password" -> JsString(userCreateInput.password)
			))
		}
		
		def reads(json: JsValue): JsResult[UserCreateInput] = {
			JsSuccess(new UserCreateInput(
				(json \ "login").as[String],
				(json \ "name").as[String],
				(json \ "password").as[String]
			))
		}
    }
}