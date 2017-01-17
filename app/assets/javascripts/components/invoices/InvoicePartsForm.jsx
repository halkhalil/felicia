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
				<table className="table table-striped table-bordered">
					<thead>
						<tr>
							<th className="text-center">Name</th>
							<th width="140" className="text-center">Quantity</th>
							<th width="140" className="text-center">Unit</th>
							<th width="140" className="text-center">Unit price</th>
							<th width="140" className="text-center">Total</th>
							<th width="30"></th>
						</tr>
					</thead>
					<tbody>
					{
						[...new Array(totalParts)].map((element, index) =>
							<tr key={index} className="vert-align">
								<td className={this.nonEmptyTextValidator(index, 'name', '')}>
									<input type="text" className="form-control" onChange={(event) => this.handleChange(index, 'name', event.target.value)} value={this.props.parts[index].name} />
								</td>
								<td className={this.positiveFloatValidator(index, 'quantity', '')}>
									<input type="text" className="form-control" onChange={(event) => this.handleChange(index, 'quantity', event.target.value)} value={this.props.parts[index].quantity} />
								</td>
								<td className={this.nonEmptyTextValidator(index, 'unit', '')}>
									<Select
										clearable={false}
										searchable={false}
										value={this.props.parts[index].unit}
										options={units}
										onChange={(selection) => this.handleChange(index, 'unit', selection.value)}
									/>
								</td>
								<td className={this.positiveFloatValidator(index, 'unitPrice', '')}>
									<input type="text" className="form-control" onChange={(event) => this.handleChange(index, 'unitPrice', event.target.value)} value={this.props.parts[index].unitPrice} />
								</td>
								<td className="text-right">
									{this.props.parts[index].total}
								</td>
								<td>
									<a className="btn btn-danger" data-toggle="modal" data-target={`#deletePartDialog${index}`} href="#">
										<span className="glyphicon glyphicon-remove"></span>
									</a>
									<Confirm id={`deletePartDialog${index}`} text="Are you sure you want to delete this part?" onConfirmed={() => this.handleDeletePart(index)} />
								</td>
							</tr>
						)
					}
					{
						totalParts === 0 &&
						<tr>
							<td colSpan="6">
								<div className="alert alert-warning"><strong>Warning: </strong> Please add invoice parts.</div>
							</td>
						</tr>
					}
					</tbody>
				</table>
				
				<div className="row"> 
					<div className="col-sm-offset-10 col-sm-2">
						<button type="button" className="btn btn-primary pull-right" onClick={(event) => this.handleAddPart()}><span className="glyphicon glyphicon-plus"></span> Add Part</button>
					</div>
				</div>
			</div>
		)
	}
}