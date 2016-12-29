import React from "react";
import { connect } from 'react-redux'
import { browserHistory } from 'react-router';

import * as AlertsActions from "redux/actions/alerts";
import {PaymentMethodAddForm} from "./PaymentMethodAddForm";

const mapDispatchToProps = (dispatch) => {
	return {
		save: (data) => {
			const ENDPOINT = '/api/payment-method'
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({
				type: 'POST',
				url: ENDPOINT,
				dataType: 'json',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(data),
				success: (data) => {
					if (data.id !== undefined) {
						browserHistory.push('/admin/payment-method/' + data.id)
						dispatch(AlertsActions.addSuccess('Payment method has been created.'))
					} else {
						dispatch(AlertsActions.addDanger('Payment method was not created.'))
					}
				}
			}).fail(
				(error) => {
					if (error.responseJSON instanceof Object) {
						dispatch(AlertsActions.addDanger('Error creating payment method: ' + error.responseJSON.error))
					} else {
						dispatch(AlertsActions.addDanger('Error creating payment method.'))
					}
				}
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		},
		
		cancel() {
			browserHistory.push('/admin/payment-methods');
		}
	}
}

const mapStateToProps = (state) => {
	return {
		saving: state.alerts.processing
	}
}

const PaymentMethodAdd = connect(
	mapStateToProps,
	mapDispatchToProps
)(PaymentMethodAddForm)

export {PaymentMethodAdd}