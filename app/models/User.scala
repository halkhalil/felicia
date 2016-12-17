package models

import java.util._;
import javax.persistence._;
import javax.validation.constraints._;

import com.avaje.ebean.Model;
import play.data.format._;
import play.data.validation._;
import models.user.UserSession
import play.api.libs.json.Json
import play.api.libs.json._

@Entity
class User extends Model {
	@Id
	var id:Int = _
	
	@NotNull
	var login:String = _
	
	@NotNull
	var password:String = _
	
	@ManyToOne()
	var role:UserRole = _
	
	@OneToMany(mappedBy = "user")
	var sessions:List[UserSession] = _
}

object User {
	implicit object UserFormat extends Format[User] {

        def writes(user: User): JsValue = {
            val loginSeq = Seq(
            	"id" -> JsNumber(user.id),
					"login" -> JsString(user.login)
            )
            JsObject(loginSeq)
        }
        
        def reads(json: JsValue): JsResult[User] = {
            JsSuccess(new User())
        }

    }

	
	def finder:Model.Finder[Long, User] = new Model.Finder[Long, User](classOf[User]);
}