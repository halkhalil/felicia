import React from "react";

class LoginForm extends React.Component {
	constructor() {
		super()
		
		this.state = {
			message: '',
			messageType: 'info',
			form: {
				login: '',
				password: ''
			}
		}
		this.handleSubmit = this.handleSubmit.bind(this)
		this.handleChange = this.handleChange.bind(this)
	}
	
	handleSubmit(event) {
		event.preventDefault()
		
		if (this.state.form.login.length === 0 || this.state.form.password.length === 0) {
			this.showErrorMessage('Enter login and password')
			return
		}
		
		this.showInfoMessage('Authentication in progress ...')
		jQuery.ajax({
			url: '/login',
			type: "POST",
			data: JSON.stringify({
				login: this.state.form.login,
				password: this.state.form.password
			}),
			contentType: "application/json; charset=utf-8",
			context: this
		}).done(function(response) {
			this.clearMessage()
			this.props.onAuthenticate(response.user)
		}).fail(function(jqXHR, exception) {
			try {
				var response = JSON.parse(jqXHR.responseText);
				if (response && response.error) {
					this.showErrorMessage(response.error)
				} else {
					this.showErrorMessage('Unknown error')
				}
			} catch (error) {
				this.showErrorMessage('Unknown error')
			}
		})
	}
	
	handleChange(field, value) {
		var formData = Object.assign({}, this.state.form, { [field]: value } )
		this.setState({ form: formData })
	}
	
	clearMessage() {
		this.setState({ message: '' })
	}
	
	showInfoMessage(message) {
		this.setState({ message: message, messageType: 'info' })
	}
	
	showErrorMessage(message) {
		this.setState({ message: message, messageType: 'danger' })
	}
	
	render() {
		if (this.state.messageType && this.state.message) {
			var classString = 'alert alert-' + this.state.messageType
			var status = <div id="status" className={classString} ref="status">{this.state.message}</div>
		}
		
		return (
			<div className="row">
				<div className="col-md-4"></div>
				<form className="form-horizontal col-md-4" role="form" onSubmit={this.handleSubmit}>
					<div className="panel panel-info">
						<div className="panel-heading">Log in</div>
					
						<div className="panel-body">
							<div className="form-group">
								<label htmlFor="username" className="col-md-2 control-label">Login:</label>
								
								<div className="col-md-10">
									<input type="text" value={this.state.form.login} onChange={(event) => this.handleChange('login', event.target.value)} className="form-control" />
								</div>
							</div>
							<div className="form-group">
								<label htmlFor="password" className="col-md-2 control-label">Password:</label>
								
								<div className="col-md-10">
									<input type="password" value={this.state.form.password} onChange={(event) => this.handleChange('password', event.target.value)} className="form-control" />
								</div>
							</div>
							<div className="form-group">
								<div className="col-sm-12">
									<button type="submit" className="btn btn-primary pull-right">Log in</button>
								</div>
							</div>
						</div>
					</div>
					{ status }
				</form>
				<div className="col-md-4"></div>
			</div>
		)
	}
}

// property validators:
LoginForm.propTypes = {
	onAuthenticate: React.PropTypes.func.isRequired
};

export {LoginForm}