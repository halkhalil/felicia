import React from "react";
import Select from 'react-select';
import 'react-select/dist/react-select.css';
import Confirm from "components/common/dialogs/Confirm";

export default class InvoicePartsForm extends React.Component {
	
	handleAddPart() {
		let sourcePart = {
			name: '',
			unit: '',
			quantity: '1',
			unitPrice: '0',
			total: 0
		}
		
		this.props.onPartAdd(sourcePart)
	}
	
	handleDeletePart(index) {
		this.props.onPartDelete(index)
	}
	
	handleChange(partIndex, field, value) {
		let part = Object.assign({}, this.props.parts[partIndex], {
			[field]: value
		})
		part.total = this.totalPrice(part)
		
		this.props.onPartChange(partIndex, part)
	}
	
	parseFloatFromText(text) {
		return parseFloat(text.replace(/,/, '.'))
	}
	
	nonEmptyTextValidator(partIndex, field, otherClasses) {
		let value = this.props.parts[partIndex][field]
		let hasError = this.props.showValidationErrors && (typeof value !== 'string' || value.length === 0)
		
		return (hasError ? 'has-error ' : '') + otherClasses
	}
	
	positiveFloatValidator(partIndex, field, otherClasses) {
		let value = this.parseFloatFromText(this.props.parts[partIndex][field])
		let hasError = this.props.showValidationErrors && (isNaN(value) || value <= 0)
		
		return (hasError ? 'has-error ' : '') + otherClasses
	}
	
	totalPrice(part) {
		let quantity = this.parseFloatFromText(part.quantity)
		let unitPrice = this.parseFloatFromText(part.unitPrice)
		
		if (isNaN(quantity) || isNaN(unitPrice)) {
			return 0
		} else {
			return ((Math.round(quantity * 100) * Math.round(unitPrice * 100)) / 10000).toFixed(2)
		}
	}
	
	render() {
		let units = this.props.units.map((unit) => {
			return { value: unit, label: unit }
		})
		let totalParts = this.props.parts.length
		
		return (
			<div>
				<div className="form-group">
					<div className="col-sm-5">
						Name
					</div>
					<div className="col-sm-1">
						Quantity
					</div>
					<div className="col-sm-2">
						Unit
					</div>
					<div className="col-sm-2">
						Unit price
					</div>
					<div className="col-sm-1">
						Total
					</div>
					<div className="col-sm-1">
						
					</div>
				</div>
				{
					[...new Array(totalParts)].map((element, index) =>
						<div className="form-group" key={index}>
							<div className={this.nonEmptyTextValidator(index, 'name', 'col-sm-5')}>
								<input type="text" className="form-control" onChange={(event) => this.handleChange(index, 'name', event.target.value)} value={this.props.parts[index].name} />
							</div>
							<div className={this.positiveFloatValidator(index, 'quantity', 'col-sm-1')}>
								<input type="text" className="form-control" onChange={(event) => this.handleChange(index, 'quantity', event.target.value)} value={this.props.parts[index].quantity} />
							</div>
							<div className={this.nonEmptyTextValidator(index, 'unit', 'col-sm-2')}>
								<Select
									clearable={false}
									searchable={false}
									value={this.props.parts[index].unit}
									options={units}
									onChange={(selection) => this.handleChange(index, 'unit', selection.value)}
								/>
							</div>
							<div className={this.positiveFloatValidator(index, 'unitPrice', 'col-sm-2')}>
								<input type="text" className="form-control" onChange={(event) => this.handleChange(index, 'unitPrice', event.target.value)} value={this.props.parts[index].unitPrice} />
							</div>
							<div className="col-sm-1 form-control-static">
								{this.props.parts[index].total}
							</div>
							<div className="col-sm-1">
								<a className="btn btn-danger" data-toggle="modal" data-target={`#deletePartDialog${index}`} href="#">
									<span className="glyphicon glyphicon-remove"></span>
								</a>
								<Confirm id={`deletePartDialog${index}`} text="Are you sure you want to delete this part?" onConfirmed={() => this.handleDeletePart(index)} />
							</div>
						</div>
					)
				}
				
				{
					totalParts === 0 &&
					<div className="form-group">
						<div className="col-sm-12">
							<div className="alert alert-warning"><strong>Warning: </strong> Please add invoice parts.</div>
						</div>
					</div>
				}
				
				<div className="form-group"> 
					<div className="col-sm-offset-11 col-sm-1">
						<button type="button" className="btn btn-primary" onClick={(event) => this.handleAddPart()}><span className="glyphicon glyphicon-ok"></span> Add Part</button>
					</div>
				</div>
			</div>
		)
	}
}