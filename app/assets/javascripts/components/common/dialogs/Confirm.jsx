import React from "react";

class Confirm extends React.Component {
	
	render() {
		return (
			<div className="modal fade" id={this.props.id} role="dialog">
				<div className="modal-dialog">
					<div className="modal-content">
						<div className="modal-header">
							<button type="button" className="close" data-dismiss="modal">&times;</button>
							<h4 className="modal-title">
								{this.props.title}
							</h4>
						</div>
						<div className="modal-body">
							{
								this.props.text.split("\n").map((line, index) => <p key={index}>{line}</p>)
							}
						</div>
						<div className="modal-footer">
							<a href="#" className="btn btn-primary" data-dismiss="modal" onClick={this.props.onConfirmed}><span className="glyphicon glyphicon-ok"></span> Yes</a>
							<a href="#" className="btn btn-danger" data-dismiss="modal"><span className="glyphicon glyphicon-remove"></span> No</a>
						</div>
					</div>
				</div>
			</div>
		)
	}
}

// property validators:
Confirm.propTypes = {
	id: React.PropTypes.string.isRequired,
	text: React.PropTypes.string.isRequired,
	onConfirmed: React.PropTypes.func.isRequired
};

Confirm.defaultProps = {
	title: 'Confirmation'
};

export default Confirm