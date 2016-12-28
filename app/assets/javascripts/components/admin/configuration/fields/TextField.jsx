import React from "react";

class TextField extends React.Component {

	render() {
		return (
			<input type="text" className={this.props.className} onChange={(event) => this.props.onChange(event.target.value)} value={this.props.value} />
		)
	}
	
}

// property validators:
TextField.propTypes = {
	value: React.PropTypes.string.isRequired,
	onChange: React.PropTypes.func.isRequired,
	className: React.PropTypes.string
};

export {TextField}