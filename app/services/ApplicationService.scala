package services

import play.api.libs.json._
import javax.inject._
import models.user.UserSession

@Singleton
class ApplicationService {
	
	def initialData(userSessionId: Option[String]):JsValue = {
		val data:JsObject = Json.obj()
		
		userSessionId.map { sessionId =>
			val userSession:UserSession = UserSession.finder.where().eq("sessionId", sessionId).findUnique()
			
			if (userSession != null && userSession.user != null) {
				return data + (
					"user" -> Json.obj(
						"login" -> JsString(userSession.user.login),
						"name" -> JsString(userSession.user.name)
					)
				)
			}
		}
		
		data
	}
}