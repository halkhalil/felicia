package models.user

import models._
import java.util._;
import javax.persistence._;
import javax.validation.constraints._;

import com.avaje.ebean.Model;
import play.data.format._;
import play.data.validation._;

@Entity
class UserSession extends Model {
	@Id
	var id:Int = _
	
	@NotNull
	var sessionId:String = _
	
	@NotNull
	var ip:String = _
	
	@NotNull
	@Column(columnDefinition = "DATETIME")
	var added:Date = _
	
	@ManyToOne()
	var user:User = _
}

object UserSession {
	def finder:Model.Finder[Long, UserSession] = new Model.Finder[Long, UserSession](classOf[UserSession]);
}