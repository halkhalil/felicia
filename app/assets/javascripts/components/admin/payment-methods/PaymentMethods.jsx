import React from "react";
import { connect } from 'react-redux'

import * as PaymentMethodsActions from "redux/actions/paymentMethods";
import * as AlertsActions from "redux/actions/alerts";
import {PaymentMethodsTable} from "./PaymentMethodsTable";

const mapDispatchToProps = (dispatch) => {
	return {
		fetchAll: () => {
			const ENDPOINT = '/api/payment-methods'
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({ 
				type: 'GET',
				url: ENDPOINT,
				dataType: 'json',
				success: (data) => dispatch(PaymentMethodsActions.fetchAll(data))
			}).fail(
				() => dispatch(AlertsActions.addDanger('Error fetching payment methods.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		},
		
		delete(id) {
			const ENDPOINT = '/api/payment-method/' + id
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({ 
				type: 'DELETE',
				url: ENDPOINT,
				dataType: 'json',
				success: (data) => dispatch(AlertsActions.addSuccess('Payment method has been deleted.'))
			}).fail(
				() => dispatch(AlertsActions.addDanger('Error while deleting payment method.'))
			).always(
				() => {
					dispatch(AlertsActions.processingOff())
					this.fetchAll()
				}
			)
		}
	}
}

const mapStateToProps = (state) => {
	return {
		methods: state.paymentMethods.methods,
		fetching: state.alerts.processing
	}
}

const PaymentMethods = connect(
	mapStateToProps,
	mapDispatchToProps
)(PaymentMethodsTable)

export {PaymentMethods}