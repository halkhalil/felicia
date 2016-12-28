import React from "react";
import { connect } from 'react-redux'

import * as AdminConfigurationActions from "redux/actions/adminConfiguration";
import * as AlertsActions from "redux/actions/alerts";
import {ConfigurationEditForm} from "./ConfigurationEditForm";

const mapDispatchToProps = (dispatch) => {
	return {
		fetchAll() {
			const ENDPOINT = '/api/configuration/entries'
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({
				type: 'GET',
				url: ENDPOINT,
				dataType: 'json',
				success: (data) => dispatch(AdminConfigurationActions.entries(data))
			}).fail(
				() => dispatch(AlertsActions.addDanger('Error loading configuration entries.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		},
		
		onFieldChange(symbol, value) {
			dispatch(AdminConfigurationActions.alterField(symbol, value))
		},
		
		save(data) {
			const ENDPOINT = '/api/configuration/entries'
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({
				type: 'PUT',
				url: ENDPOINT,
				dataType: 'json',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(data),
				success: (data) => {
					dispatch(AlertsActions.addSuccess('Configuration has been saved.'))
					dispatch(AdminConfigurationActions.entries(data))
				}
			}).fail(
				() => dispatch(AlertsActions.addDanger('Error while saving configuration entries.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		}
	}
}

const mapStateToProps = (state) => {
	return {
		entries: state.adminConfiguration.entries,
		saving: state.alerts.processing
	}
}

const ConfigurationEdit = connect(
	mapStateToProps,
	mapDispatchToProps
)(ConfigurationEditForm)

export {ConfigurationEdit}