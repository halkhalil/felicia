import React from "react";

class NumberField extends React.Component {

	render() {
		return (
			<input type="number" className={this.props.className} onChange={(event) => this.props.onChange(event.target.value)} value={this.props.value} />
		)
	}
	
}

// property validators:
NumberField.propTypes = {
	value: React.PropTypes.string.isRequired,
	onChange: React.PropTypes.func.isRequired,
	className: React.PropTypes.string
};

export {NumberField}