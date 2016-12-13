import React from "react";
import ReactDOM from "react-dom";

import {LoginForm} from "components/LoginForm.jsx";
import {LogoutButton} from "components/LogoutButton.jsx";
 
class Application extends React.Component {
	constructor() {
		super()
		
		this.state = initialData
		this.handleAuthenticated = this.handleAuthenticated.bind(this)
		this.handleLogout = this.handleLogout.bind(this)
	}
	
	handleAuthenticated(user) {
		this.setState({ user: user })
	}
	
	handleLogout(user) {
		this.setState({ user: undefined })
	}
	
	render() {
		if (typeof this.state.user !== 'undefined') {
			return (
				<div>
					Felicia application skeleton: {this.state.user.login}
					<br />
					<LogoutButton onLogout={this.handleLogout} />
				</div>
			)
		} else {
			return (
				<div>
					<LoginForm onAuthenticated={this.handleAuthenticated} />
				</div>
			)
		}
	}
}

ReactDOM.render(
	<div>
		<Application />
	</div>,
	document.querySelector("#container")
);
