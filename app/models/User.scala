package models

import java.util._;
import javax.persistence._;
import javax.validation.constraints._;

import com.avaje.ebean.Model;
import play.data.format._;
import play.data.validation._;
import models.user.UserSession

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
	def finder:Model.Finder[Long, User] = new Model.Finder[Long, User](classOf[User]);
}