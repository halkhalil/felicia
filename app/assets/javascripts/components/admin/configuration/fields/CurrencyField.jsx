import React from "react";
import { connect } from 'react-redux'
import Select from 'react-select';
import 'react-select/dist/react-select.css';

class CurrencyFieldComponent extends React.Component {

	render() {
		let values = this.props.currencies.map((currency) => {
			return {
				name: currency,
				id: currency
			}
		})
		
		return (
			<Select
				clearable={false}
				searchable={false}
				value={this.props.value}
				options={values}
				labelKey="name"
				valueKey="id"
				onChange={(selection) => this.props.onChange(selection.id)}
			/>
		)
	}
	
}

// property validators:
CurrencyFieldComponent.propTypes = {
	value: React.PropTypes.string.isRequired,
	onChange: React.PropTypes.func.isRequired,
	className: React.PropTypes.string
};

const mapDispatchToProps = (dispatch) => {
	return {
		
	}
}

const mapStateToProps = (state) => {
	return {
		currencies: state.configuration.currencies
	}
}

const CurrencyField = connect(
	mapStateToProps,
	mapDispatchToProps
)(CurrencyFieldComponent)

export {CurrencyField}