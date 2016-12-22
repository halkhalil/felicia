import React from "react";

export class UserAddForm extends React.Component {
	
	constructor() {
		super()
		
		this.state = {
			login: '',
			name: '',
			password: '',
			passwordRepeat: '',
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
				login: this.state.login,
				name: this.state.name,
				password: this.state.password
			})
		}
	}
	
	isFormValid() {
		if (this.state.login.length === 0) {
			return false
		}
		if (this.state.name.length === 0) {
			return false
		}
		if (this.state.password.length === 0) {
			return false
		}
		if (this.state.password !== this.state.passwordRepeat) {
			return false
		}
		
		return true
	}
	
	handleChange(field, value) {
		this.setState({ [field]: value })
	}
	
	nonEmptyErrorClass(field, otherClasses) {
		return (this.state.validationError && this.state[field].length === 0 ? 'has-error ' : '') + otherClasses
	}
	
	passwordErrorClass(otherClasses) {
		return (this.state.validationError && (this.state.password.length === 0 || this.state.password !== this.state.passwordRepeat) ? 'has-error ' : '') + otherClasses
	}
	
	submitButtonClasses() {
		return 'btn btn-primary' + (this.props.saving ? ' disabled' : '')
	}
	
	render() {
		return (
			<div>
				<h3>Add User <strong>{this.state.login}</strong></h3>
				
				<form className="form-horizontal" onSubmit={(event) => this.handleSubmit(event)}>
					<div className={this.nonEmptyErrorClass('login', 'form-group')}>
						<label className="control-label col-sm-2">Login:</label>
						<div className="col-sm-10">
							<input type="text" className="form-control" onChange={(event) => this.handleChange('login', event.target.value)} value={this.state.login} />
						</div>
					</div>
					<div className={this.nonEmptyErrorClass('name', 'form-group')}>
						<label className="control-label col-sm-2">Name:</label>
						<div className="col-sm-10">
							<input type="text" className="form-control" onChange={(event) => this.handleChange('name', event.target.value)} value={this.state.name} />
						</div>
					</div>
					<div className={this.passwordErrorClass('form-group')}>
						<label className="control-label col-sm-2">Password:</label>
						<div className="col-sm-10">
							<input type="password" className="form-control" onChange={(event) => this.handleChange('password', event.target.value)} value={this.state.password} />
						</div>
					</div>
					<div className={this.passwordErrorClass('form-group')}>
						<label className="control-label col-sm-2">Password repeat:</label>
						<div className="col-sm-10">
							<input type="password" className="form-control" onChange={(event) => this.handleChange('passwordRepeat', event.target.value)} value={this.state.passwordRepeat} />
							{
								this.state.password !== this.state.passwordRepeat &&
								<span className="help-block">Password repetition does not match the password</span>
							}
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
							{this.state.validationError && !this.props.saving && <div className="alert alert-danger">Please fill correct values in all fields.</div>}
						</div>
					</div>
				</form>
			</div>
		)
	}
}