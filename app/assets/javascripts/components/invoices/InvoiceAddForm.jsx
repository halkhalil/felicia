import React from "react";

import 'react-input-calendar/style/index.css'
import Calendar from 'react-input-calendar';

export class InvoiceAddForm extends React.Component {
	
	constructor() {
		super()
		
		this.state = {
			buyerIsCompany: false,
			buyerName: '',
			buyerAddress: '',
			buyerZip: '',
			buyerCity: '',
			buyerCountry: '',
			buyerTaxId: '',
			issueDate: null,
			orderDate: '',
			validationError: false
		}
	}
	
	handleSubmit(event) {
		event.preventDefault()
		
		this.setState({ validationError: false })
		if (!this.isFormValid()) {
			this.setState({ validationError: true })
		} else {
			this.props.save({
				buyerIsCompany: this.state.buyerIsCompany,
				buyerName: this.state.buyerName,
				buyerAddress: this.state.buyerAddress,
				buyerZip: this.state.buyerZip,
				buyerCity: this.state.buyerCity,
				buyerCountry: this.state.buyerCountry,
				buyerTaxId: this.state.buyerTaxId,
				issueDate: this.state.issueDate,
				orderDate: this.state.orderDate
			})
		}
	}
	
	isFormValid() {
		let validateEmpty = ['buyerName', 'buyerAddress', 'buyerZip', 'buyerCity', 'buyerCountry', 'issueDate', 'orderDate']
		let valid = true
		
		validateEmpty.forEach((field) => {
			if (this.state[field].length === 0) {
				valid = false
			}
		})
		
		
		return valid
	}
	
	handleChange(field, value) {
		this.setState({ [field]: value })
	}
	
	nonEmptyErrorClass(field, otherClasses) {
		return (this.state.validationError && this.state[field].length === 0 ? 'has-error ' : '') + otherClasses
	}
	
	submitButtonClasses() {
		return 'btn btn-primary' + (this.props.saving ? ' disabled' : '')
	}
	
	render() {
		return (
			<div>
				<h3>Add Invoice</h3>
				
				<form className="form-horizontal" onSubmit={(event) => this.handleSubmit(event)}>
					Buyer:
					<div className="form-group">
						<label className="control-label col-sm-2">Is a company:</label>
						<div className="col-sm-10">
							<label className="radio-inline">
								<input type="radio" name="buyerIsCompany" value="0" checked={this.state.buyerIsCompany === false} onChange={(event) => this.handleChange('buyerIsCompany', event.target.value === '1')} /> No
							</label>
							<label className="radio-inline">
								<input type="radio" name="buyerIsCompany" value="1" checked={this.state.buyerIsCompany === true}  onChange={(event) => this.handleChange('buyerIsCompany', event.target.value === '1')} /> Yes
							</label>
						</div>
					</div>
					<div className={this.nonEmptyErrorClass('buyerName', 'form-group')}>
						<label className="control-label col-sm-2">Name:</label>
						<div className="col-sm-10">
							<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerName', event.target.value)} value={this.state.buyerName} />
						</div>
					</div>
					<div className={this.nonEmptyErrorClass('buyerAddress', 'form-group')}>
						<label className="control-label col-sm-2">Address:</label>
						<div className="col-sm-10">
							<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerAddress', event.target.value)} value={this.state.buyerAddress} />
						</div>
					</div>
					<div className={this.nonEmptyErrorClass('buyerZip', 'form-group')}>
						<label className="control-label col-sm-2">Zip code:</label>
						<div className="col-sm-10">
							<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerZip', event.target.value)} value={this.state.buyerZip} />
						</div>
					</div>
					<div className={this.nonEmptyErrorClass('buyerCity', 'form-group')}>
						<label className="control-label col-sm-2">City:</label>
						<div className="col-sm-10">
							<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerCity', event.target.value)} value={this.state.buyerCity} />
						</div>
					</div>
					<div className={this.nonEmptyErrorClass('buyerCountry', 'form-group')}>
						<label className="control-label col-sm-2">Country:</label>
						<div className="col-sm-10">
							<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerCountry', event.target.value)} value={this.state.buyerCountry} />
						</div>
					</div>
					<div className="form-group">
						<label className="control-label col-sm-2">Tax ID:</label>
						<div className="col-sm-10">
							<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerTaxId', event.target.value)} value={this.state.buyerTaxId} />
						</div>
					</div>
					
					Other details:
					<div className={this.nonEmptyErrorClass('issueDate', 'form-group')}>
						<label className="control-label col-sm-2">Issue Date:</label>
						<div className="col-sm-10">
							<Calendar format='DD/MM/YYYY' date='4-12-2014' />
						</div>
					</div>
					<div className={this.nonEmptyErrorClass('orderDate', 'form-group')}>
						<label className="control-label col-sm-2">Order Date:</label>
						<div className="col-sm-10">
							<input type="text" className="form-control" onChange={(event) => this.handleChange('orderDate', event.target.value)} value={this.state.orderDate} />
						</div>
					</div>
					
					<div className="form-group"> 
						<div className="col-sm-offset-2 col-sm-10">
							<button type="submit" className={this.submitButtonClasses()}><span className="glyphicon glyphicon-ok"></span> Save</button>
							<span>&nbsp;</span>
							<button type="button" className="btn btn-default" onClick={this.props.cancel}>Cancel</button>
						</div>
					</div>
					<div className="form-group">
						<div className="col-sm-offset-2 col-sm-10">
							{this.state.validationError && !this.props.saving && <div className="alert alert-danger"><strong>Error: </strong> Please fill all fields.</div>}
						</div>
					</div>
					
					
				</form>
			</div>
		)
	}
}