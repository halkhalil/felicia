import React from "react";
import moment from "moment";
import numeral from "numeral"
import { Link } from 'react-router'
import Confirm from "components/common/dialogs/Confirm"

class InvoicesBrowser extends React.Component {
	
	componentDidMount() {
		this.props.fetchAll(this.getYear(), this.getMonth())
	}
	
	handleFilterClick(year, month) {
		this.props.fetchAll(year, month)
	}
	
	browseUrl(year, month) {
		return "/invoices/" + year + "/" + month
	}
	
	getYear() {
		return this.props.params.year !== undefined ? this.props.params.year : moment().format('Y')
	}
	
	getMonth() {
		return this.props.params.month !== undefined ? this.props.params.month : moment().format('M')
	}
	
	render() {
		let years = []
		for (let year = this.props.settings.minYear; year <= this.props.settings.maxYear; year++) {
			years.push(year)
		}
		let months = []
		for (let month = 0; month < 12; month++) {
			months.push(moment().month(month).format("MMMM"))
		}
		
		return (
			<div>
				<h3>Invoices</h3>
				
				<nav aria-label="Page navigation">
					<div className="row">
						<div className="col-sm-10">
							<ul className="pagination">
							{
								years.map((year) => 
									<li key={year} className={this.getYear() == year ? 'active' : ''}>
										<Link to={this.browseUrl(year, this.getMonth())} onClick={() => this.handleFilterClick(year, this.getMonth())}>{year}</Link>
									</li>
								)
							}
							</ul>
						</div>
						<div className="col-sm-2 text-right">
							<Link to="/invoices/add" className="btn btn-primary"><span className="glyphicon glyphicon-plus"></span> Add Invoice</Link>
						</div>
					</div>
					<ul className="pagination">
					{
						months.map((month, index) =>
							<li key={month} className={(index + 1) == this.getMonth() ? 'active' : ''}>
								<Link to={this.browseUrl(this.getYear(), index + 1)} onClick={() => this.handleFilterClick(this.getYear(), index + 1)}>{month}</Link>
							</li>
						)
					}
					</ul>
				</nav>
				
				
				
				<table className="table table-striped table-bordered">
					<thead>
						<tr>
							<th width="120" className="text-center">ID</th>
							<th className="text-center">Buyer</th>
							<th className="text-center">City</th>
							<th className="text-center">Country</th>
							<th className="text-center">Issued</th>
							<th width="140" className="text-center">Value</th>
							<th width="150"></th>
						</tr>
					</thead>
					<tbody>
						{
							this.props.fetching &&
							<tr className="info">
								<td colSpan="8">
									Loading ...
								</td>
							</tr>
						}
						{
							this.props.invoices.map((invoice) => {
								return (
									<tr key={invoice.id}>
										<td className="text-right">
											{invoice.publicId}
										</td>
										<td className="text-center">
											{invoice.buyerName}
										</td>
										<td className="text-center">
											{invoice.buyerCity}
										</td>
										<td className="text-center">
											{invoice.buyerCountry}
										</td>
										<td className="text-center">
											{invoice.issueDate}
										</td>
										<td className="text-right">
											{numeral(invoice.total / 100).format('0,0[.]00')} {invoice.currency}
										</td>
										<td>
											<div className="btn-group">
												<Link to={'/invoice/' + invoice.id} className="btn btn-primary btn-xs">
													<span className="glyphicon glyphicon-pencil"></span> Edit
												</Link>
												<a className="btn btn-danger btn-xs" data-toggle="modal" data-target={`#deleteInvoiceDialog${invoice.id}`} href="#">
													<span className="glyphicon glyphicon-remove"></span> Delete
												</a>
												<Confirm id={`deleteInvoiceDialog${invoice.id}`} text="Are you sure you want to delete this invoice?" onConfirmed={() => this.props.delete(invoice.id)} />
											</div>
										</td>
									</tr>
								)
							})
						}
						{
							this.props.invoices.length === 0 && !this.props.fetching &&
							<tr className="info">
								<td colSpan="8">
									No invoices found
								</td>
							</tr>
						}
					</tbody>
				</table>
			</div>
		)
	}
}

export default InvoicesBrowser