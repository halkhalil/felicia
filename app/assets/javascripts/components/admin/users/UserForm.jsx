import React from "react";

export class UserForm extends React.Component {
	
	componentDidMount() {
		this.props.fetch(this.props.params.id)
	}
	
	componentWillReceiveProps(nextProps) {
		if (nextProps.user !== undefined) {
			this.setState({
				login: nextProps.user.login,
				name: nextProps.user.name,
				password: '',
				validationError: false
			})
		}
	}
	
	handleSubmit(event) {
		event.preventDefault()
		
		// validate
		this.setState({ validationError: false })
		if (this.state.name.length === 0) {
			this.setState({ validationError: true })
		}
		
		if (!this.state.validationError) {
			this.props.save(this.props.params.id, {
				name: this.state.name
			})
		}
	}
	
	handleChange(field, value) {
		this.setState({ [field]: value })
	}
	
	hasError(field, otherClasses) {
		return (this.state[field].length === 0 ? 'has-error ' : '') + otherClasses
	}
	
	render() {
		if (this.props.fetchError !== undefined) {
			return (
				<div>
					<h3>Edit User</h3>
					<div className="alert alert-danger">
						<strong>Error: </strong> {this.props.fetchError}
					</div>
				</div>
			)
		} else if (this.state !== null) {
			return (
				<div>
					<h3>Edit User <strong>{this.state.login}</strong></h3>
					<form className="form-horizontal" onSubmit={(event) => this.handleSubmit(event)}>
						<div className="form-group">
							<label className="control-label col-sm-2">Login:</label>
							<div className="col-sm-10 form-control-static">
								{this.state.login}
							</div>
						</div>
						<div className={this.hasError('name', 'form-group')}>
							<label className="control-label col-sm-2">Name:</label>
							<div className="col-sm-10">
								<input type="text" className="form-control" onChange={(event) => this.handleChange('name', event.target.value)} value={this.state.name} />
							</div>
						</div>
						<div className="form-group">
							<label className="control-label col-sm-2">Password:</label>
							<div className="col-sm-10">
								<input type="password" className="form-control" />
							</div>
						</div>
						<div className="form-group">
							<label className="control-label col-sm-2">Password repeat:</label>
							<div className="col-sm-10">
								<input type="password" className="form-control" />
							</div>
						</div>
						<div className="form-group"> 
							<div className="col-sm-offset-2 col-sm-10">
								<button type="submit" className="btn btn-primary">Save</button>
							</div>
						</div>
						<div className="form-group"> 
							<div className="col-sm-offset-2 col-sm-10">
								{this.state.validationError && <div className="alert alert-danger">Validation error</div>}
							</div>
						</div>
					</form>
				</div>
			)
		}
		
		return <div />
	}
}