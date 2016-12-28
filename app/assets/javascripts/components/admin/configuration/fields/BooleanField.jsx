import React from "react";

class BooleanField extends React.Component {

	render() {
		return (
			<div>
				<label className={this.props.className}>
					<input type="radio" name="role" value="0" checked={this.props.value == '0'} onChange={(event) => this.props.onChange(event.target.value)} /> No
				</label>
				<label className={this.props.className}>
					<input type="radio" name="role" value="1" checked={this.props.value == '1'}  onChange={(event) => this.props.onChange(event.target.value)} /> Yes
				</label>
			</div>
		)
	}
	
}

// property validators:
BooleanField.propTypes = {
	value: React.PropTypes.string.isRequired,
	onChange: React.PropTypes.func.isRequired,
	className: React.PropTypes.string
};

export {BooleanField}