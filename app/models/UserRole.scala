package models

import java.util._;
import javax.persistence._;
import javax.validation.constraints._;

import com.avaje.ebean.Model;
import play.data.format._;
import play.data.validation._;

@Entity
class UserRole extends Model {
	@Id
	var id:Int = _
	
	@NotNull
	var name:String = _
	
	@NotNull
	var symbol:String = _
	
	@OneToMany(mappedBy = "role")
	var users:List[User] = _
}

object UserRole {
	def finder:Model.Finder[Long, UserRole] = new Model.Finder[Long, UserRole](classOf[UserRole]);
}