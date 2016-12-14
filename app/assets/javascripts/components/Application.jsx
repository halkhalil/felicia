import React from "react";
import ReactDOM from "react-dom";

import {LoginForm} from "components/user/LoginForm.jsx";
import {LogoutButton} from "components/user/LogoutButton.jsx";
 
export class Application extends React.Component {
	constructor() {
		super()
		
		this.state = initialData
		this.handleAuthenticate = this.handleAuthenticate.bind(this)
		this.handleLogout = this.handleLogout.bind(this)
	}
	
	handleAuthenticate(user) {
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
					<LoginForm onAuthenticate={this.handleAuthenticate} />
				</div>
			)
		}
	}
}