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
import {InvoiceAdd} from "components/invoices/InvoiceAdd.jsx";
import {Users} from "components/admin/users/Users.jsx";
import {UserAdd} from "components/admin/users/UserAdd.jsx";
import {UserEdit} from "components/admin/users/UserEdit";
import {ConfigurationEdit} from "components/admin/configuration/ConfigurationEdit";
import {PaymentMethods} from "components/admin/payment-methods/PaymentMethods";
import {PaymentMethodEdit} from "components/admin/payment-methods/PaymentMethodEdit";
import {PaymentMethodAdd} from "components/admin/payment-methods/PaymentMethodAdd";
 
class ApplicationComponent extends React.Component {
	constructor(props) {
		super(props)
		
		this.handleAuthenticate = this.handleAuthenticate.bind(this)
	}
	
	handleAuthenticate(user) {
		this.props.setUser(user)
	}
	
	render() {
		if (typeof this.props.user !== 'undefined') {
			return (
				<Router history={browserHistory}>
					<Route path="/" component={Core}>
						<IndexRoute component={Start} />
						<Route path="customers" component={Customers} />
						
						<Route path="invoices">
							<IndexRoute component={Invoices} />
							<Route path="add" component={InvoiceAdd} />
						</Route>
						
						<Route path="admin">
							<Route path="users">
								<IndexRoute component={Users} />
								<Route path="add" component={UserAdd} />
							</Route>
							<Route path="user/:id" component={UserEdit} />
							<Route path="configuration" component={ConfigurationEdit} />
							<Route path="payment-methods">
								<IndexRoute component={PaymentMethods} />
								<Route path="add" component={PaymentMethodAdd} />
							</Route>
							<Route path="payment-method/:id" component={PaymentMethodEdit} />
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