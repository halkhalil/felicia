import React from "react";
import ReactDOM from "react-dom";
import { Router, Route, IndexRoute, browserHistory } from 'react-router'

import {LoginForm} from "components/user/LoginForm.jsx";
import {LogoutButton} from "components/user/LogoutButton.jsx";
import {Core} from "components/common/Core.jsx";
import {Start} from "components/common/Start.jsx";
import {NotFound} from "components/common/NotFound.jsx";

import {Customers} from "components/customers/Customers.jsx";
import {Invoices} from "components/invoices/Invoices.jsx";
import {Users} from "components/admin/users/Users.jsx";
import {UserAdd} from "components/admin/users/UserAdd.jsx";
 
export class Application extends React.Component {
	constructor(props) {
		super(props)
		
		this.state = this.props.configuration;
		this.handleAuthenticate = this.handleAuthenticate.bind(this)
		this.handleLogout = this.handleLogout.bind(this)
	}
	
	handleAuthenticate(user) {
		this.setState({ user: user })
	}
	
	handleLogout() {
		this.setState({ user: undefined })
	}
	
	render() {
		if (typeof this.state.user !== 'undefined') {
			return (
				<Router history={browserHistory}>
					<Route path="/" user={this.state.user} onLogout={this.handleLogout} component={Core}>
						<IndexRoute component={Start} />
						<Route path="customers" component={Customers} />
						<Route path="invoices" component={Invoices} />
						<Route path="admin">
							<Route path="users">
								<IndexRoute component={Users} />
								<Route path="add" component={UserAdd} />
							</Route>
						</Route>
						
						<Route path="*" component={NotFound} />
					</Route>
				</Router>
			)
		} else {
			return <LoginForm onAuthenticate={this.handleAuthenticate} />
		}
	}
}