import React from "react";
import { Link } from 'react-router'

export class NotFound extends React.Component {
	render() {
		return (
			<div className="row">
				<div className="col-md-12">
					<h1>Oops!</h1>
					<h2>404 Not Found</h2>
					<p>Sorry, an error has occured, requested page not found!</p>
					
					<Link to="/" className="btn btn-primary btn-lg"><span className="glyphicon glyphicon-home"></span>Take Me Home </Link>
				</div>
			</div>
		)
	}
}