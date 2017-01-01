import React from "react";

import {TextField} from "./fields/TextField";
import {NumberField} from "./fields/NumberField";
import {BooleanField} from "./fields/BooleanField";
import {CurrencyField} from "./fields/CurrencyField";

export class ConfigurationEditForm extends React.Component {
	
	componentDidMount() {
		this.props.fetchAll()
	}
	
	handleSubmit(event) {
		event.preventDefault()

		let values = this.props.entries.map((entry) => { return { symbol: entry.symbol, value: entry.value } })

		this.props.save(values)
	}
	
	fieldValue(symbol) {
		return this.props.entries.find((entry) => entry.symbol === symbol).value
	}
	
	submitButtonClasses() {
		return 'btn btn-primary' + (this.props.saving ? ' disabled' : '')
	}
	
	renderSection(section) {
		let fieldsOfSection = this.props.entries.filter((entry) => { return entry.sectionName == section })
		
		return (
			<div key={section}>
				<h4>{section}</h4>
				{
					fieldsOfSection.map((entry) => {
						return (
							<div key={entry.id} className="form-group">
								<label className="control-label col-sm-2">{entry.name}:</label>
								<div className="col-sm-10">
									{
										entry.typeId === 'number' &&
										<NumberField className="form-control" onChange={(value) => this.props.onFieldChange(entry.symbol, value)} value={this.fieldValue(entry.symbol)} />
									}
									{
										entry.typeId === 'currency' &&
										<CurrencyField className="form-control" onChange={(value) => this.props.onFieldChange(entry.symbol, value)} value={this.fieldValue(entry.symbol)} />
									}
									{
										entry.typeId === 'text' &&
										<TextField className="form-control" onChange={(value) => this.props.onFieldChange(entry.symbol, value)} value={this.fieldValue(entry.symbol)} />
									}
									{
										entry.typeId === 'boolean' &&
										<BooleanField className="radio-inline" onChange={(value) => this.props.onFieldChange(entry.symbol, value)} value={this.fieldValue(entry.symbol)} />
									}
								</div>
							</div>
						)
					})
				}
			</div>
		)
	}
	
	render() {
		let sections = this.props.entries.map((entry) => entry.sectionName)
		let sectionsUnique = sections.filter((value, index) => sections.indexOf(value) == index );
		
		return (
			<div>
				<h3>Configuration</h3>
				
				<form className="form-horizontal" onSubmit={(event) => this.handleSubmit(event)}>
					{
						sectionsUnique.map((section) => {
							return this.renderSection(section)
						})
					}
					
					<div className="form-group"> 
						<div className="col-sm-offset-2 col-sm-10">
							<button type="submit" className={this.submitButtonClasses()}><span className="glyphicon glyphicon-ok"></span> Save</button>
						</div>
					</div>
				</form>
			</div>
		)
	}
}