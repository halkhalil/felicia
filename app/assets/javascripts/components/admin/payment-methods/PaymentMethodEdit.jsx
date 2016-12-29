import React from "react";
import { connect } from 'react-redux'
import { browserHistory } from 'react-router';

import * as PaymentMethodsActions from "redux/actions/paymentMethods";
import * as AlertsActions from "redux/actions/alerts";
import {PaymentMethodEditForm} from "./PaymentMethodEditForm";

const mapDispatchToProps = (dispatch) => {
	return {
		fetch(id) {
			const ENDPOINT = '/api/payment-method/' + id
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({
				type: 'GET',
				url: ENDPOINT,
				dataType: 'json',
				success: (data) => dispatch(PaymentMethodsActions.fetch(data))
			}).fail(
				() => dispatch(AlertsActions.addDanger('Error loading payment method.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		},
		
		onFieldChange(field, value) {
			dispatch(PaymentMethodsActions.alterPaymentMethodField(field, value))
		},
		
		save(id, data) {
			const ENDPOINT = '/api/payment-method/' + id
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({
				type: 'PUT',
				url: ENDPOINT,
				dataType: 'json',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(data),
				success: (data) => {
					dispatch(AlertsActions.addSuccess('Payment method has been saved.'))
					dispatch(PaymentMethodsActions.fetch(data))
				}
			}).fail(
				() => dispatch(AlertsActions.addDanger('Error while saving payment method.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		},
		
		cancel() {
			browserHistory.push('/admin/payment-methods')
		}
	}
}

const mapStateToProps = (state) => {
	return {
		method: state.paymentMethods.method,
		saving: state.alerts.processing
	}
}

const PaymentMethodEdit = connect(
	mapStateToProps,
	mapDispatchToProps
)(PaymentMethodEditForm)

export {PaymentMethodEdit}