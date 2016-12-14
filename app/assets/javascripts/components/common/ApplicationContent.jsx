import React from "react";

import {Customers} from "components/customers/Customers.jsx";
import {Invoices} from "components/invoices/Invoices.jsx";
import {Users} from "components/admin/Users.jsx";
import {UserAdd} from "components/admin/UserAdd.jsx";

export class ApplicationContent extends React.Component {
	
	render() {
		
		switch (this.props.section) {
			case "/customers":
				return <Customers />
			case "/invoices":
				return <Invoices />
			case "/admin/users":
				return <Users />
			case "/admin/users/add":
				return <UserAdd />
			default:
				return (
					<div>
						<h3>Start</h3>
						<p>TODO</p>
					</div>
				)
		}
	}
}