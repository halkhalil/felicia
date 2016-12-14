import React from "react";

class LogoutButton extends React.Component {
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
			this.props.onLogout(response.user)
		})
	}
	
	render() {
		return (
			<a href="#" onClick={this.handleLogout}>Log out</a>
		)
	}
}

// property validators:
LogoutButton.propTypes = {
	onLogout: React.PropTypes.func.isRequired
};

export {LogoutButton}