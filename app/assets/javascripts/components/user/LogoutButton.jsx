import React from "react";

export class LogoutButton extends React.Component {
	constructor() {
		super()
		
		this.handleLogout = this.handleLogout.bind(this)
	}
	
	handleLogout(event) {
		event.preventDefault()
		
		jQuery.ajax({
			url: '/logout',
			type: "POST",
			context: this
		}).done(function(response) {
			if (typeof this.props.onLogout !== 'undefined') {
				this.props.onLogout(response.user)
			}
		})
	}
	
	render() {
		return (
			<button type="submit" onClick={this.handleLogout} className="btn btn-primary pull-right">Log out</button>
		)
	}
}