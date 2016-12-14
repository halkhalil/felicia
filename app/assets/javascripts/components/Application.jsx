import React from "react";
import ReactDOM from "react-dom";

import {LoginForm} from "components/user/LoginForm.jsx";
import {LogoutButton} from "components/user/LogoutButton.jsx";
import {ApplicationMenu} from "components/common/ApplicationMenu.jsx";
import {ApplicationContent} from "components/common/ApplicationContent.jsx";
 
export class Application extends React.Component {
	constructor() {
		super()
		
		this.state = initialData
		this.handleAuthenticate = this.handleAuthenticate.bind(this)
		this.handleLogout = this.handleLogout.bind(this)
		this.handleMenuItemClick = this.handleMenuItemClick.bind(this)
	}
	
	handleAuthenticate(user) {
		this.setState({ user: user })
	}
	
	handleLogout() {
		this.setState({ user: undefined })
	}
	
	handleMenuItemClick(item) {
		this.setState({ currentItem: item })
	}
	
	render() {
		if (typeof this.state.user !== 'undefined') {
			return (
				<div>
					<ApplicationMenu
						user={this.state.user}
						onLogout={this.handleLogout}
						onMenuItemClick={this.handleMenuItemClick} />
					
					<div className="container">
						<div className="jumbotron">
							<ApplicationContent section={this.state.currentItem} />
						</div>
					</div>
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