import React from "react";
import ReactDOM from "react-dom";
 
class Application extends React.Component {
	constructor() {
		super()
	}
	
	render() {
		return (
			<div>
				Felicia application skeleton
			</div>
		)
	}
}
	
ReactDOM.render(
	<div>
		<Application />
	</div>,
	document.querySelector("body")
);
