import React from "react";

class SpinnerBox extends React.Component {
	
	render() {
		if (this.props.visible) {
			return (
				<span className="loader loader-xs"></span>
			)
		} else {
			return null
		}
	}
	
}

// property validators:
SpinnerBox.propTypes = {
	visible: React.PropTypes.bool.isRequired
};

export {SpinnerBox}