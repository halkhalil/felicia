import React from "react";
import ReactDOM from "react-dom";
import { connect } from 'react-redux'
import { Router, Route, IndexRoute, browserHistory } from 'react-router'

import * as ConfigurationActions from "redux/actions/configuration";

import {LoginForm} from "components/user/LoginForm.jsx";
import {LogoutButton} from "components/user/LogoutButton.jsx";
import {Core} from "components/common/Core.jsx";
import {Start} from "components/common/Start.jsx";
import {NotFound} from "components/common/NotFound.jsx";

import {Customers} from "components/customers/Customers.jsx";
import {Invoices} from "components/invoices/Invoices.jsx";
import {Users} from "components/admin/users/Users.jsx";
import {UserAdd} from "components/admin/users/UserAdd.jsx";
import {UserEdit} from "components/admin/users/UserEdit";
 
class ApplicationComponent extends React.Component {
	constructor(props) {
		super(props)
		
		this.handleAuthenticate = this.handleAuthenticate.bind(this)
		this.handleLogout = this.handleLogout.bind(this)
	}
	
	handleAuthenticate(user) {
		this.props.setUser(user)
	}
	
	handleLogout() {
		this.props.clearConfiguration()
	}
	
	render() {
		if (typeof this.props.user !== 'undefined') {
			return (
				<Router history={browserHistory}>
					<Route path="/" user={this.props.user} onLogout={this.handleLogout} component={Core}>
						<IndexRoute component={Start} />
						<Route path="customers" component={Customers} />
						<Route path="invoices" component={Invoices} />
						<Route path="admin">
							<Route path="users">
								<IndexRoute component={Users} />
								<Route path="add" component={UserAdd} />
							</Route>
							<Route path="user/:id" component={UserEdit} />
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

const mapDispatchToProps = (dispatch) => {
	return {
		setUser: (user) => {
			dispatch(ConfigurationActions.setUser(user))
		},
		
		clearConfiguration: () => {
			dispatch(ConfigurationActions.clear())
		}
	}
}

const mapStateToProps = (state) => {
	return {
		user: state.configuration.user
	}
}

const Application = connect(
	mapStateToProps,
	mapDispatchToProps
)(ApplicationComponent)

export {Application}