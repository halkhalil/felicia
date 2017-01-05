import React from "react";
import { connect } from 'react-redux'
import InvoicesRecentTable from './InvoicesRecentTable'
import * as AlertsActions from "redux/actions/alerts"
import * as InvoicesActions from "redux/actions/invoices"

const mapDispatchToProps = (dispatch) => {
	return {
		fetchRecent: () => {
			const ENDPOINT = `/api/invoices/recent`
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({
				type: 'GET',
				url: ENDPOINT,
				dataType: 'json',
				success: (data) => dispatch(InvoicesActions.fetchRecent(data))
			}).fail(
				() => dispatch(AlertsActions.addDanger('Error fetching invoices.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		}
	}
}

const mapStateToProps = (state) => {
	return {
		invoices: state.invoices.recentInvoices,
		fetching: state.alerts.processing
	}
}

const InvoicesRecent = connect(
	mapStateToProps,
	mapDispatchToProps
)(InvoicesRecentTable)

export default InvoicesRecent