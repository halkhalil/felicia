import React from "react";
import moment from "moment";
import Calendar from 'react-input-calendar';
import 'react-input-calendar/style/index.css'
import Select from 'react-select';
import 'react-select/dist/react-select.css';
import InvoicePartsForm from './InvoicePartsForm'

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
			issueDate: moment().format('YYYY-MM-DD'),
			orderDate: moment().format('YYYY-MM-DD'),
			dueDate: moment().add(10, 'days').format('YYYY-MM-DD'),
			paymentMethod: 0,
			parts: [],
			validationError: false
		}
	}
	
	handleSubmit(event) {
		event.preventDefault()
		
		this.setState({ validationError: false })
		if (!this.isFormValid()) {
			this.setState({ validationError: true })
		} else {
			let partsTransformed = this.state.parts.map((part) => {
				let partTransformed =  Object.assign({}, part, {
					quantity: parseFloat(part.quantity.replace(/,/, '.')),
					unitPrice: Math.round(parseFloat(part.unitPrice.replace(/,/, '.')) * 100),
					total: Math.round(parseFloat(part.total) * 100)
				})
				
				return partTransformed
			})
			
			this.props.save({
				buyerIsCompany: this.state.buyerIsCompany,
				buyerName: this.state.buyerName,
				buyerAddress: this.state.buyerAddress,
				buyerZip: this.state.buyerZip,
				buyerCity: this.state.buyerCity,
				buyerCountry: this.state.buyerCountry,
				buyerTaxId: this.state.buyerTaxId,
				buyerEmail: this.state.buyerEmail,
				buyerPhone: this.state.buyerPhone,
				issueDate: this.state.issueDate,
				orderDate: this.state.orderDate,
				dueDate: this.state.dueDate,
				paymentMethod: this.state.paymentMethod,
				parts: partsTransformed
			})
		}
	}
	
	isFormValid() {
		let validateEmpty = ['buyerName', 'buyerAddress', 'buyerZip', 'buyerCity', 'buyerCountry', 'issueDate', 'orderDate', 'dueDate']
		let validateNonZero = ['paymentMethod']
		let valid = true
		
		validateEmpty.forEach((field) => {
			if (this.state[field].length === 0) {
				valid = false
			}
		})
		validateNonZero.forEach((field) => {
			let intParsed = parseInt(this.state[field])
			if (isNaN(intParsed) || intParsed === 0) {
				valid = false
			}
		})
		
		this.state.parts.forEach((part) => {
			if (part.name.length === 0) {
				valid = false
			}
			if (part.unit.length === 0) {
				valid = false
			}
			
			let total = parseFloat(part.total)
			if (isNaN(total) || total <= 0) {
				valid = false
			}
		})
		if (this.state.parts.length === 0) {
			valid = false
		}
		
		return valid
	}
	
	handleChange(field, value) {
		this.setState({ [field]: value })
	}
	
	handlePartChange(partIndex, part) {
		this.setState({ parts: this.state.parts.map((item, index) => index === partIndex ? part : item) })
	}
	
	handlePartAdd(part) {
		let parts = [...this.state.parts]
		parts.push(part)
		
		this.setState({ parts: parts })
	}
	
	handlePartDelete(partIndex) {
		let parts = [...this.state.parts]
		parts.splice(partIndex, 1)
		
		this.setState({ parts: parts })
	}
	
	nonEmptyErrorClass(field, otherClasses) {
		return (this.state.validationError && this.state[field].length === 0 ? 'has-error ' : '') + otherClasses
	}
	
	nonZeroErrorClass(field, otherClasses) {
		return (this.state.validationError && parseInt(this.state[field]) === 0 ? 'has-error ' : '') + otherClasses
	}
	
	submitButtonClasses() {
		return 'btn btn-primary' + (this.props.saving ? ' disabled' : '')
	}
	
	render() {
		let units = this.props.units.map((unit) => {
			return { value: unit, label: unit }
		})
		
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
					<div className="form-group">
						<label className="control-label col-sm-2">E-mail:</label>
						<div className="col-sm-10">
							<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerEmail', event.target.value)} value={this.state.buyerEmail} />
						</div>
					</div>
					<div className="form-group">
						<label className="control-label col-sm-2">Phone:</label>
						<div className="col-sm-10">
							<input type="text" className="form-control" onChange={(event) => this.handleChange('buyerPhone', event.target.value)} value={this.state.buyerPhone} />
						</div>
					</div>
					
					Other details:
					<div className={this.nonEmptyErrorClass('issueDate', 'form-group')}>
						<label className="control-label col-sm-2">Issue Date:</label>
						<div className="col-sm-10 form-inline">
							<Calendar 
								format="YYYY-MM-DD"
								closeOnSelect={true}
								hideOnBlur={true}
								computableFormat="YYYY-MM-DD"
								date={this.state.issueDate} onChange={(date) => this.handleChange('issueDate', date)}
								inputFieldClass="form-control"
							/>
						</div>
					</div>
					<div className={this.nonEmptyErrorClass('orderDate', 'form-group')}>
						<label className="control-label col-sm-2">Order Date:</label>
						<div className="col-sm-10 form-inline">
							<Calendar
								format="YYYY-MM-DD"
								closeOnSelect={true}
								hideOnBlur={true}
								computableFormat="YYYY-MM-DD"
								date={this.state.orderDate} onChange={(date) => this.handleChange('orderDate', date)}
								inputFieldClass="form-control"
							/>
						</div>
					</div>
					<div className={this.nonEmptyErrorClass('dueDate', 'form-group')}>
						<label className="control-label col-sm-2">Due Date:</label>
						<div className="col-sm-10 form-inline">
							<Calendar
								format="YYYY-MM-DD"
								closeOnSelect={true}
								hideOnBlur={true}
								computableFormat="YYYY-MM-DD"
								date={this.state.dueDate} onChange={(date) => this.handleChange('dueDate', date)}
								inputFieldClass="form-control"
							/>
						</div>
					</div>
					<div className={this.nonZeroErrorClass('paymentMethod', 'form-group')}>
						<label className="control-label col-sm-2">Payment method:</label>
						<div className="col-sm-4 form-inline">
							<Select
								clearable={false}
								searchable={false}
								value={this.state.paymentMethod}
								options={this.props.paymentMethods}
								labelKey="name"
								valueKey="id"
								onChange={(selection) => this.handleChange('paymentMethod', selection.id)}
							/>
						</div>
					</div>
					
					<InvoicePartsForm
						parts={this.state.parts}
						units={this.props.units}
						showValidationErrors={this.state.validationError}
						onPartAdd={(part) => this.handlePartAdd(part)}
						onPartDelete={(partIndex) => this.handlePartDelete(partIndex)}
						onPartChange={(partIndex, part) => this.handlePartChange(partIndex, part)}
					/>
					
					<div className="form-group"> 
						<div className="col-sm-12 text-right">
							<button type="submit" className={this.submitButtonClasses()}><span className="glyphicon glyphicon-ok"></span> Save</button>
							<span>&nbsp;</span>
							<button type="button" className="btn btn-default" onClick={this.props.cancel}>Cancel</button>
						</div>
					</div>
					<br />
					<div className="form-group">
						<div className="col-sm-offset-6 col-sm-6">
							{this.state.validationError && !this.props.saving && <div className="alert alert-danger"><strong>Error: </strong> Please fill all fields.</div>}
						</div>
					</div>
				</form>
			</div>
		)
	}
}