import React from "react";
import { connect } from 'react-redux'
import { Link } from 'react-router'
import InvoicesBrowser from './InvoicesBrowser'
import * as AlertsActions from "redux/actions/alerts"
import * as InvoicesActions from "redux/actions/invoices"

const mapDispatchToProps = (dispatch) => {
	let lastMonthFetched = undefined
	let lastYearFetched = undefined
	
	return {
		fetchAll: (year, month) => {
			const ENDPOINT = `/api/invoices/${year}/${month}`
			
			lastYearFetched = year
			lastMonthFetched = month
			
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
		
		deleteLast(year) {
			const ENDPOINT = `/api/invoice/${year}/last`
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({ 
				type: 'DELETE',
				url: ENDPOINT,
				dataType: 'json',
				success: (data) => dispatch(AlertsActions.addSuccess('The very last invoice has been deleted: ' + data.publicId))
			}).fail(
				(error) => {
					if (error.responseJSON instanceof Object) {
						dispatch(AlertsActions.addDanger('Error while deleting the invoice: ' + error.responseJSON.error))
					} else {
						dispatch(AlertsActions.addDanger('Error while deleting the invoice.'))
					}
				}
			).always(
				() => {
					dispatch(AlertsActions.processingOff())
					this.fetchAll(lastYearFetched, lastMonthFetched)
				}
			)
		}
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