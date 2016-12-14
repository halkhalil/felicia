import React from "react";

import {LogoutButton} from "components/user/LogoutButton.jsx";

class ApplicationMenu extends React.Component {
	constructor() {
		super()
		
		this.state = {
			selectedItem: '/'
		}
		this.handleLogout = this.handleLogout.bind(this)
		this.handleMenuClick = this.handleMenuClick.bind(this)
	}
	
	handleLogout() {
		this.props.onLogout()
	}
	
	handleMenuClick(event) {
		event.preventDefault()
		var item = jQuery(event.currentTarget).attr('href')
		
		this.setState({ selectedItem: item });
		this.props.onMenuItemClick(item)
	}
	
	getActiveClass(item) {
		return this.state.selectedItem === item ? "active" : ''
	}
	
	render() {
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
						<a className="navbar-brand" href="/">Felicia Accounting</a>
					</div>
					<div id="navbar" className="navbar-collapse collapse">
						<ul className="nav navbar-nav">
							<li className={this.getActiveClass("/")}>
								<a href="/" onClick={this.handleMenuClick}>Home</a>
							</li>
							<li className={this.getActiveClass("/customers")}>
								<a href="/customers" onClick={this.handleMenuClick}>Customers</a>
							</li>
							<li className={this.getActiveClass("/invoices")}>
								<a href="/invoices" onClick={this.handleMenuClick}>Invoices</a>
							</li>
							<li className="dropdown">
								<a href="#" className="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Administration <span className="caret"></span></a>
								<ul className="dropdown-menu">
									<li className="dropdown-header">Users</li>
									<li className={this.getActiveClass("/admin/users/add")}>
										<a href="/admin/users/add" onClick={this.handleMenuClick}>Add</a>
									</li>
									<li className={this.getActiveClass("/admin/users")}>
										<a href="/admin/users" onClick={this.handleMenuClick}>List</a>
									</li>
								</ul>
							</li>
						</ul>
						<ul className="nav navbar-nav navbar-right">
							<li>
								<LogoutButton onLogout={this.handleLogout} />
							</li>
						</ul>
						<p className="navbar-text pull-right">
							<span className="label label-primary">User: {this.props.user.login}</span>
							
						</p>
					</div>
				</div>
			</nav>
		)
	}
}

// property validators:
ApplicationMenu.propTypes = {
	onMenuItemClick: React.PropTypes.func.isRequired,
	onLogout: React.PropTypes.func.isRequired
};

export {ApplicationMenu}