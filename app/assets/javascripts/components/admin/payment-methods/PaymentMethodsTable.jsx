import React from "react";
import { Link } from 'react-router'

import Confirm from "components/common/dialogs/Confirm";

class PaymentMethodsTable extends React.Component {
	
	componentDidMount() {
		this.props.fetchAll()
	}
	
	render() {
		return (
			<div>
				<h3>Payment Methods</h3>
				<table className="table table-striped table-bordered">
					<thead>
						<tr>
							<th width="50">ID</th>
							<th>Name</th>
							<th>Symbol</th>
							<th width="150"></th>
						</tr>
					</thead>
					<tbody>
						{
							this.props.fetching &&
							<tr className="info">
								<td colSpan="4">
									Loading ...
								</td>
							</tr>
						}
						{
							this.props.methods.map((method) => {
								return (
									<tr key={method.id}>
										<td>
											{method.id}
										</td>
										<td>
											{method.name}
										</td>
										<td>
											{method.symbol}
										</td>
										<td>
											<div className="btn-group">
												<Link to={'/admin/payment-method/' + method.id} className="btn btn-primary btn-xs">
													<span className="glyphicon glyphicon-pencil"></span> Edit
												</Link>
												<a className="btn btn-danger btn-xs" data-toggle="modal" data-target={`#deleteDialog${method.id}`} href="#">
													<span className="glyphicon glyphicon-remove"></span> Delete
												</a>
												<Confirm id={`deleteDialog${method.id}`} text="Are you sure you want to delete this payment method?" onConfirmed={() => this.props.delete(method.id)} />
											</div>
										</td>
									</tr>
								)
							})
						}
						{
							this.props.methods.length === 0 && !this.props.fetching &&
							<tr className="info">
								<td colSpan="4">
									No payment methods found
								</td>
							</tr>
						}
					</tbody>
				</table>
				
				<Link to="/admin/payment-methods/add" className="btn btn-primary btn-sm"><span className="glyphicon glyphicon-plus"></span> Add Payment Method</Link>
			</div>
		)
	}
}

// property validators:
PaymentMethodsTable.propTypes = {
	fetchAll: React.PropTypes.func.isRequired,
	delete: React.PropTypes.func.isRequired,
	fetching: React.PropTypes.bool.isRequired,
	methods: React.PropTypes.arrayOf(React.PropTypes.shape({
		symbol: React.PropTypes.string.isRequired,
		name: React.PropTypes.string.isRequired,
		id: React.PropTypes.number.isRequired
	})).isRequired
};

export {PaymentMethodsTable}