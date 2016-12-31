import React from "react";
import { Link } from 'react-router'

export class Invoices extends React.Component {
	
	render() {
		return (
			<div>
				<h3>Invoices</h3>
				
				<Link to="/invoices/add" className="btn btn-primary btn-sm"><span className="glyphicon glyphicon-plus"></span> Add Invoice</Link>
			</div>
		)
	}
}