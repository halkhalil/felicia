import React from "react";
import { connect } from 'react-redux'
import { browserHistory } from 'react-router';

import * as AlertsActions from "redux/actions/alerts"
import * as InvoicesActions from "redux/actions/invoices"
import InvoiceEditForm from "./InvoiceEditForm";

const mapDispatchToProps = (dispatch) => {
	return {
		fetch(id) {
			const ENDPOINT = '/api/invoice/' + id
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({
				type: 'GET',
				url: ENDPOINT,
				dataType: 'json',
				success: (data) => dispatch(InvoicesActions.fetch(data))
			}).fail(
				() => dispatch(AlertsActions.addDanger('Error while loading the invoice.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		},
		
		onFieldChange(field, value) {
			dispatch(InvoicesActions.alterField(field, value))
		},
		
		save(id, data) {
			const ENDPOINT = '/api/invoice/' + id
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({
				type: 'PUT',
				url: ENDPOINT,
				dataType: 'json',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(data),
				success: (data) => {
					dispatch(AlertsActions.addSuccess('Invoice has been saved.'))
					dispatch(InvoicesActions.fetch(data))
				}
			}).fail(
				() => dispatch(AlertsActions.addDanger('Error while saving the invoice.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		},
		
		goToInvoices(year, month) {
			browserHistory.push(`/invoices/${year}/${month}`)
		}
	}
}

const mapStateToProps = (state) => {
	return {
		invoice: state.invoices.invoice,
		paymentMethods: state.configuration.paymentMethods,
		saving: state.alerts.processing
	}
}

const InvoiceEdit = connect(
	mapStateToProps,
	mapDispatchToProps
)(InvoiceEditForm)

export default InvoiceEdit