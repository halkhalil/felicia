import React from "react";
import { connect } from 'react-redux'

import * as CurrenciesActions from "redux/actions/currencies";
import * as AlertsActions from "redux/actions/alerts";
import CurrencyRatesTable from "./CurrencyRatesTable";

const mapDispatchToProps = (dispatch) => {
	return {
		fetchAll: (year, month, day) => {
			const ENDPOINT = `/api/currencies/${year}/${month}/${day}`
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({ 
				type: 'GET',
				url: ENDPOINT,
				dataType: 'json',
				success: (data) => dispatch(CurrenciesActions.fetchAll(data))
			}).fail(
				() => dispatch(AlertsActions.addDanger('Error fetching currencies.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		}
	}
}

const mapStateToProps = (state) => {
	return {
		currencies: state.currencies.currencies,
		fetching: state.alerts.processing
	}
}

const CurrencyRates = connect(
	mapStateToProps,
	mapDispatchToProps
)(CurrencyRatesTable)

export default CurrencyRates