package services

import play.api.libs.json._
import javax.inject._
import models.user.UserSession

@Singleton
class ApplicationService {
	
	def frontEndConfiguration(userSessionId: Option[String]):JsValue = {
		val data:JsObject = Json.obj()
		
		userSessionId.map { sessionId =>
			val userSession:UserSession = UserSession.finder.where().eq("sessionId", sessionId).findUnique()
			
			if (userSession != null && userSession.user != null) {
				return data + (
					"user" -> Json.toJson(userSession.user)
				)
			}
		}
		
		data
	}
}