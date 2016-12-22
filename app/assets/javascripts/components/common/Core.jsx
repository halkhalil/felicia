import React from "react";

import {TopMenu} from "components/common/TopMenu.jsx";
import {Alerts} from "components/common/alerts/Alerts";

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
						<Alerts />
						{this.props.children}
					</div>
				</div>
			</div>
		)
	}
}