package controllers.input

import play.data.format._;
import play.data.validation._;
import play.api.libs.json._

case class UserUpdateInput(name: String, password: String, role: String)

object UserUpdateInput {
	implicit object UserUpdateInputFormat extends Format[UserUpdateInput] {
		def writes(userUpdateInput: UserUpdateInput): JsValue = {
			JsObject(Seq(
				"name" -> JsString(userUpdateInput.name),
				"password" -> JsString(userUpdateInput.password),
				"role" -> JsString(userUpdateInput.role)
			))
		}
		
		def reads(json: JsValue): JsResult[UserUpdateInput] = {
			JsSuccess(new UserUpdateInput(
				(json \ "name").as[String],
				(json \ "password").as[String],
				(json \ "role").as[String]
			))
		}
    }
}