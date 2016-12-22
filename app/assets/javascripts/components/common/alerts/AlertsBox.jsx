import React from "react";

const CLEAN_REQUEST_TIMEOUT = 1000

class AlertsBox extends React.Component {

	componentDidMount() {
	   let intervalId = setInterval(this.props.onCleanRequest, CLEAN_REQUEST_TIMEOUT)
	   this.setState({intervalId: intervalId})
	}

	componentWillUnmount() {
	   clearInterval(this.state.intervalId)
	}
	
	render() {
		return (
			<div>
				{
					this.props.alerts.map((singleAlert, index) =>
						<div key={index} className={`alert alert-${singleAlert.category} alert-dismissable`}>
							<a href="#" className="close" data-dismiss="alert" aria-label="close">&times;</a>
							{singleAlert.text}
						</div>
					)
				}
			</div>
		)
	}
}

// property validators:
AlertsBox.propTypes = {
	onCleanRequest: React.PropTypes.func.isRequired,
	alerts: React.PropTypes.arrayOf(React.PropTypes.shape({
		category: React.PropTypes.string.isRequired,
		text: React.PropTypes.string.isRequired
	})).isRequired
};

export {AlertsBox}