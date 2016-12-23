import React from "react";

import {TopMenu} from "components/common/TopMenu.jsx";
import {Alerts} from "components/common/alerts/Alerts";

export class Core extends React.Component {
	
	render() {
		return (
			<div>
				<TopMenu />
				
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