import React from "react";
import { connect } from 'react-redux'
import { Link } from 'react-router'
import InvoicesBrowser from './InvoicesBrowser'
import * as AlertsActions from "redux/actions/alerts"
import * as InvoicesActions from "redux/actions/invoices"

const mapDispatchToProps = (dispatch) => {
	return {
		fetchAll: (year, month) => {
			const ENDPOINT = `/api/invoices/${year}/${month}`
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({ 
				type: 'GET',
				url: ENDPOINT,
				dataType: 'json',
				success: (data) => dispatch(InvoicesActions.fetchAll(data))
			}).fail(
				() => dispatch(AlertsActions.addDanger('Error fetching invoices.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		},
	}
}

const mapStateToProps = (state) => {
	return {
		invoices: state.invoices.invoices,
		settings: state.configuration.invoices,
		fetching: state.alerts.processing
	}
}

const Invoices = connect(
	mapStateToProps,
	mapDispatchToProps
)(InvoicesBrowser)

export default Invoices