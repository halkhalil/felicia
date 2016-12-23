package controllers.input

import play.data.format._;
import play.data.validation._;
import play.api.libs.json._

case class AuthenticationInput(login: String, password: String)

object AuthenticationInput {
	implicit object AuthenticationInputFormat extends Format[AuthenticationInput] {
		def writes(authenticationInput: AuthenticationInput): JsValue = {
			JsObject(Seq(
				"login" -> JsString(authenticationInput.login),
				"password" -> JsString(authenticationInput.password)
			))
		}
		
		def reads(json: JsValue): JsResult[AuthenticationInput] = {
			JsSuccess(new AuthenticationInput(
				(json \ "login").as[String],
				(json \ "password").as[String]
			))
		}
    }
}