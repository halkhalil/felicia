import React from "react";
import { connect } from 'react-redux'
import { browserHistory } from 'react-router';

import * as AlertsActions from "redux/actions/alerts";
import {InvoiceAddForm} from "./InvoiceAddForm";

const mapDispatchToProps = (dispatch) => {
	return {
		save: (data) => {
			const ENDPOINT = '/api/invoice'
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({
				type: 'POST',
				url: ENDPOINT,
				dataType: 'json',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(data),
				success: (data) => {
					if (data.id !== undefined) {
						browserHistory.push('/invoice/' + data.id)
						dispatch(AlertsActions.addSuccess('Invoice has been created.'))
					} else {
						dispatch(AlertsActions.addDanger('Invoice was not created.'))
					}
				}
			}).fail(
				(error) => {
					if (error.responseJSON instanceof Object) {
						dispatch(AlertsActions.addDanger('Error creating invoice: ' + error.responseJSON.error))
					} else {
						dispatch(AlertsActions.addDanger('Error creating invoice.'))
					}
				}
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		},
		
		cancel() {
			browserHistory.push('/invoices');
		}
	}
}

const mapStateToProps = (state) => {
	return {
		paymentMethods: state.configuration.paymentMethods,
		saving: state.alerts.processing
	}
}

const InvoiceAdd = connect(
	mapStateToProps,
	mapDispatchToProps
)(InvoiceAddForm)

export {InvoiceAdd}