package services

import play.api.libs.json._
import javax.inject._
import models.user.UserSession

@Singleton
class ApplicationService {
	
	def getFrontendInitialData(userId: Option[String]):JsValue = {
		val data:JsObject = Json.obj(
			"currentItem" -> "/"
		)
		
		userId.map { userId =>
			val userSession:UserSession = UserSession.finder.where().eq("sessionId", userId).findUnique()
			
			data + (
				"user" -> Json.obj(
					"login" -> JsString(userSession.user.login)
				)
			)
		}.getOrElse {
			data
		}
	}
}