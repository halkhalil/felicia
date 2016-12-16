import React from "react";

import {TopMenu} from "components/common/TopMenu.jsx";

export class Core extends React.Component {
	constructor(props) {
		super(props)
		
		this.state = {
			user: this.props.route.user
		};
		this.handleLogout = this.handleLogout.bind(this)
	}
	
	handleLogout() {
		this.props.route.onLogout()
	}
	
	render() {
		return (
			<div>
				<TopMenu
					user={this.state.user}
					onLogout={this.handleLogout} />
				
				<div className="container">
					<div className="jumbotron">
						{this.props.children}
					</div>
				</div>
			</div>
		)
	}
}