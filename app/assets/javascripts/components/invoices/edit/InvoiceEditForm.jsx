import React from "react";
import moment from "moment";
import numeral from "numeral"
import Select from 'react-select';
import 'react-select/dist/react-select.css';

class InvoiceEditForm extends React.Component {
	
	constructor() {
		super()
		
		this.state = {
			validationError: false
		}
	}
	
	componentDidMount() {
		this.props.fetch(this.props.params.id)
	}
	
	handleSubmit(event) {
		event.preventDefault()
		
		this.setState({ validationError: false })
		if (!this.isFormValid()) {
			this.setState({ validationError: true })
		} else {
			this.props.save(this.props.params.id, {
				buyerIsCompany: this.props.invoice.buyerIsCompany,
				buyerName: this.props.invoice.buyerName,
				buyerAddress: this.props.invoice.buyerAddress,
				buyerZip: this.props.invoice.buyerZip,
				buyerCity: this.props.invoice.buyerCity,
				buyerCountry: this.props.invoice.buyerCountry,
				buyerTaxId: this.props.invoice.buyerTaxId,
				buyerEmail: this.props.invoice.buyerEmail,
				buyerPhone: this.props.invoice.buyerPhone,
				paymentMethod: this.props.invoice.paymentMethod
			})
		}
	}
	
	isFormValid() {
		let validateEmpty = ['buyerName', 'buyerAddress', 'buyerZip', 'buyerCity', 'buyerCountry']
		let validateNonZero = ['paymentMethod']
		let valid = true
		
		validateEmpty.forEach((field) => {
			if (this.props.invoice[field].length === 0) {
				valid = false
			}
		})
		validateNonZero.forEach((field) => {
			let intParsed = parseInt(this.props.invoice[field])
			if (isNaN(intParsed) || intParsed === 0) {
				valid = false
			}
		})
		
		return valid
	}
	
	handleChange(field, value) {
		this.props.onFieldChange(field, value)
	}
	
	nonEmptyErrorClass(field, otherClasses) {
		return (this.state.validationError && this.props.invoice[field].length === 0 ? 'has-error ' : '') +  otherClasses
	}
	
	nonZeroErrorClass(field, otherClasses) {
		return (this.state.validationError && parseInt(this.props.invoice[field]) === 0 ? 'has-error ' : '') + otherClasses
	}
	
	submitButtonClasses() {
		return 'btn btn-primary' + (this.props.saving ? ' disabled' : '')
	}
	
	render() {
		if (this.props.invoice !== undefined) {
			let invoice = this.props.invoice
			let year = moment(invoice.issueDate).format('Y')
			let month = moment(invoice.issueDate).format('M')
			
			return (
				<div>
					<h3>Edit Invoice <strong>{invoice.publicId}</strong></h3>
					
					<form className="form-horizontal" onSubmit={(event) => this.handleSubmit(event)}>
						<div className="row">
							<div className="col-sm-3">
								<div className="well">
									<h4>Seller:</h4>
									<address>
										<strong>{invoice.sellerName}</strong><br />
										{invoice.sellerAddress}<br />
										{invoice.sellerCity}, {invoice.sellerZip}<br />
										{invoice.sellerCountry}<br />
										<abbr title="Tax ID">Tax:</abbr> {invoice.sellerTaxId}
									</address>
								</div>
							</div>
							<div className="col-sm-9">
								<div className="well">
									<h4>Buyer:</h4>
									<div className="form-group">
										<label className="control-label col-sm-3">Is a company:</label>
										<div className="col-sm-9">
											<label className="radio-inline">
												<input type="radio" name="buyerIsCompany" value="0" checked={invoice.buyerIsCompany === false} onChange={(event) => this.handleChange('buyerIsCompany', event.target.value === '1')} /> No
											</label>
											<label className="radio-inline">
												<input type="radio" name="buyerIsCompany" value="1" checked={invoice.buyerIsCompany === true}  onChange={(event) => this.handleChange('buyerIsCompany', event.target.value === '1')} /> Yes
											</label>
										</div>
									</div>
									<div className={this.nonEmptyErrorClass('buyerName', 'form-group')}>
										<label className="control-label col-sm-3">Name:</label>
										<div className="col-sm-9">
											<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerName', event.target.value)} value={invoice.buyerName} />
										</div>
									</div>
									<div className={this.nonEmptyErrorClass('buyerAddress', 'form-group')}>
										<label className="control-label col-sm-3">Address:</label>
										<div className="col-sm-9">
											<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerAddress', event.target.value)} value={invoice.buyerAddress} />
										</div>
									</div>
									<div className={this.nonEmptyErrorClass('buyerZip', 'form-group')}>
										<label className="control-label col-sm-3">Zip code:</label>
										<div className="col-sm-9">
											<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerZip', event.target.value)} value={invoice.buyerZip} />
										</div>
									</div>
									<div className={this.nonEmptyErrorClass('buyerCity', 'form-group')}>
										<label className="control-label col-sm-3">City:</label>
										<div className="col-sm-9">
											<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerCity', event.target.value)} value={invoice.buyerCity} />
										</div>
									</div>
									<div className={this.nonEmptyErrorClass('buyerCountry', 'form-group')}>
										<label className="control-label col-sm-3">Country:</label>
										<div className="col-sm-9">
											<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerCountry', event.target.value)} value={invoice.buyerCountry} />
										</div>
									</div>
									<div className="form-group">
										<label className="control-label col-sm-3">Tax ID:</label>
										<div className="col-sm-9">
											<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerTaxId', event.target.value)} value={invoice.buyerTaxId} />
										</div>
									</div>
									<div className="form-group">
										<label className="control-label col-sm-3">E-mail:</label>
										<div className="col-sm-9">
											<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerEmail', event.target.value)} value={invoice.buyerEmail} />
										</div>
									</div>
									<div className="form-group">
										<label className="control-label col-sm-3">Phone:</label>
										<div className="col-sm-9">
											<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerPhone', event.target.value)} value={invoice.buyerPhone} />
										</div>
									</div>
								</div>
							</div>
						</div>
						<div className="well">
							<h4>Details:</h4>
							<div className="form-group">
								<label className="control-label col-sm-2">Issue Date:</label>
								<div className="col-sm-2 form-control-static">
									{invoice.issueDate}
								</div>
								<label className="control-label col-sm-2">Order Date:</label>
								<div className="col-sm-2 form-control-static">
									{invoice.orderDate}
								</div>
							</div>
							
							<div className="form-group">
								<label className="control-label col-sm-2">Due Date:</label>
								<div className="col-sm-2 form-control-static">
									{invoice.dueDate}
								</div>
								<label className="control-label col-sm-2">Place Of Issue:</label>
								<div className="col-sm-2 form-control-static">
									{invoice.placeOfIssue}
								</div>
							</div>
							<div className={this.nonZeroErrorClass('paymentMethod', 'form-group')}>
								<label className="control-label col-sm-2">Payment method:</label>
								<div className="col-sm-4 form-inline">
									<Select
										clearable={false}
										searchable={false}
										value={invoice.paymentMethod}
										options={this.props.paymentMethods}
										labelKey="name"
										valueKey="id"
										onChange={(selection) => this.handleChange('paymentMethod', selection.id)}
									/>
								</div>
							</div>
						</div>
						
						<table className="table table-striped table-bordered">
							<thead>
								<tr>
									<th className="text-center">Name</th>
									<th width="140" className="text-center">Quantity</th>
									<th width="140" className="text-center">Unit</th>
									<th width="140" className="text-center">Unit price</th>
									<th width="140" className="text-center">Total</th>
								</tr>
							</thead>
							<tbody>
							{
								invoice.parts.map((part, index) =>
									<tr key={index}>
										<td className="text-center">
											{part.name}
										</td>
										<td className="text-center">
											{(part.quantity).toFixed(2)}
										</td>
										<td className="text-center">
											{part.unit}
										</td>
										<td className="text-right">
											{numeral(part.unitPrice / 100).format('0,0.00')} {invoice.currency}
										</td>
										<td className="text-right">
											{numeral(part.total / 100).format('0,0.00')} {invoice.currency}
										</td>
									</tr>
								)
							}
							{
								invoice.parts.length > 0 &&
								<tr>
									<td colSpan="4">
										
									</td>
									<td className="text-right">
										{numeral(invoice.total / 100).format('0,0.00')} {invoice.currency}
									</td>
								</tr>
							}
							{
								invoice.parts.length === 0 &&
								<tr>
									<td colSpan="5">
										<div className="alert alert-warning"><strong>Warning: </strong> No invoice parts found.</div>
									</td>
								</tr>
							}
							</tbody>
						</table>
						
						<div className="form-group">
							<div className="col-md-12 text-right">
								<button type="submit" className={this.submitButtonClasses()}><span className="glyphicon glyphicon-ok"></span> Save</button>
								<span>&nbsp;</span>
								<button type="button" className="btn btn-default" onClick={() => this.props.goToInvoices(year, month)}>Cancel</button>
							</div>
						</div>
						<div className="form-group">
							<div className="col-sm-12">
								{this.state.validationError && !this.props.saving && <div className="alert alert-danger"><strong>Error: </strong> Please check the errors and fix them.</div>}
							</div>
						</div>
					</form>
				</div>
			)
		}
		
		return <div />
	}
}

export default InvoiceEditForm