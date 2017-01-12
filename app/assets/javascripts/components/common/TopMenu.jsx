import React from "react";
import { Link } from 'react-router'
import { connect } from 'react-redux'

import * as ConfigurationActions from "redux/actions/configuration";
import {LogoutButton} from "components/user/LogoutButton";
import {Spinner} from "components/common/spinner/Spinner";

class TopMenuComponent extends React.Component {
	constructor() {
		super()
		
		this.state = {
			selectedItem: '/'
		}
		this.handleLogout = this.handleLogout.bind(this)
		this.handleMenuClick = this.handleMenuClick.bind(this)
	}
	
	handleLogout() {
		this.props.clearConfiguration()
	}
	
	handleMenuClick(event) {
		var item = jQuery(event.currentTarget).attr('href')
		
		this.setState({ selectedItem: item });
	}
	
	getActiveClass(item) {
		return this.state.selectedItem === item ? "active" : ''
	}
	
	render() {
		let isAdmin = this.props.user.role === 'admin'
		
		return (
			<nav className="navbar navbar-default navbar-fixed-top">
				<div className="container">
					<div className="navbar-header">
						<button type="button" className="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
							<span className="sr-only">Toggle navigation</span>
							<span className="icon-bar"></span>
							<span className="icon-bar"></span>
							<span className="icon-bar"></span>
						</button>
						<Link className="navbar-brand" to="/">Felicia Accounting</Link>
					</div>
					<div id="navbar" className="navbar-collapse collapse">
						<ul className="nav navbar-nav">
							<li className={this.getActiveClass("/")}>
								<Link to="/" onClick={this.handleMenuClick}>Home</Link>
							</li>
							
							<li className={this.getActiveClass("/customers")}>
								<Link to="/customers" onClick={this.handleMenuClick}>Customers</Link>
							</li>
							<li className={this.getActiveClass("/invoices")}>
								<Link to="/invoices" onClick={this.handleMenuClick}>Invoices</Link>
							</li>
							{
								isAdmin &&
								<li className="dropdown">
									<a href="#" className="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Administration <span className="caret"></span></a>
									<ul className="dropdown-menu">
										<li className="dropdown-header">Users</li>
										<li className={this.getActiveClass("/admin/users/add")}>
											<Link to="/admin/users/add" onClick={this.handleMenuClick}>Add</Link>
										</li>
										<li className={this.getActiveClass("/admin/users")}>
											<Link to="/admin/users" onClick={this.handleMenuClick}>List</Link>
										</li>
										<li className="dropdown-header">Configuration</li>
										<li className={this.getActiveClass("/admin/configuration")}>
											<Link to="/admin/configuration" onClick={this.handleMenuClick}>General settings</Link>
										</li>
										<li className={this.getActiveClass("/admin/payment-methods")}>
											<Link to="/admin/payment-methods" onClick={this.handleMenuClick}>Payment methods</Link>
										</li>
										<li className={this.getActiveClass("/admin/currencies")}>
											<Link to="/admin/currencies" onClick={this.handleMenuClick}>Currencies</Link>
										</li>
									</ul>
								</li>
							}
						</ul>
						<ul className="nav navbar-nav navbar-right">
							<li>
								<LogoutButton onLogout={this.handleLogout} />
							</li>
						</ul>
						<p className="navbar-text pull-right">
							<span className="label label-primary">User: {this.props.user.login}</span>
						</p>
						<p className="navbar-text pull-right">
							<Spinner />
						</p>
					</div>
				</div>
			</nav>
		)
	}
}

// property validators:
TopMenuComponent.propTypes = {
	user: React.PropTypes.object.isRequired,
	clearConfiguration: React.PropTypes.func.isRequired
};

const mapDispatchToProps = (dispatch) => {
	return {
		clearConfiguration: () => {
			dispatch(ConfigurationActions.clearUser())
		}
	}
}

const mapStateToProps = (state) => {
	return {
		user: state.configuration.user
	}
}

const TopMenu = connect(
	mapStateToProps,
	mapDispatchToProps
)(TopMenuComponent)

export {TopMenu}