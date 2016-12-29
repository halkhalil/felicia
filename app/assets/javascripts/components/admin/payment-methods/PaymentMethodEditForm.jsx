import React from "react";

export class PaymentMethodEditForm extends React.Component {
	
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
				name: this.props.method.name,
				symbol: this.props.method.symbol
			})
		}
	}
	
	isFormValid() {
		if (this.props.method.name.length === 0) {
			return false
		}
		if (this.props.method.symbol.length === 0) {
			return false
		}
		
		return true
	}
	
	handleChange(field, value) {
		this.props.onFieldChange(field, value)
	}
	
	nonEmptyErrorClass(field, otherClasses) {
		return (this.props.method[field].length === 0 ? 'has-error ' : '') + otherClasses
	}
	
	submitButtonClasses() {
		return 'btn btn-primary' + (this.props.saving ? ' disabled' : '')
	}
	
	render() {
		if (this.props.method !== undefined) {
			return (
				<div>
					<h3>Edit Payment Method <strong>{this.props.method.name}</strong></h3>
					
					<form className="form-horizontal" onSubmit={(event) => this.handleSubmit(event)}>
						<div className={this.nonEmptyErrorClass('symbol', 'form-group')}>
							<label className="control-label col-sm-2">Symbol:</label>
							<div className="col-sm-10">
								<input type="text" className="form-control" onChange={(event) => this.handleChange('symbol', event.target.value)} value={this.props.method.symbol} />
							</div>
						</div>
						<div className={this.nonEmptyErrorClass('name', 'form-group')}>
							<label className="control-label col-sm-2">Name:</label>
							<div className="col-sm-10">
								<input type="text" className="form-control" onChange={(event) => this.handleChange('name', event.target.value)} value={this.props.method.name} />
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