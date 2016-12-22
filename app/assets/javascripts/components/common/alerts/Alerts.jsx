import React from "react";
import { connect } from 'react-redux'

import * as AlertsActions from "redux/actions/alerts";
import {AlertsBox} from "./AlertsBox";

const ALERT_CLEAN_TIMEOUT = 4

const mapDispatchToProps = (dispatch) => {
	return {
		onCleanRequest: () => {
			dispatch(AlertsActions.clean(ALERT_CLEAN_TIMEOUT))
		}
	}
}

const mapStateToProps = (state) => {
	return {
		alerts: state.alerts.list,
	}
}

const Alerts = connect(
	mapStateToProps,
	mapDispatchToProps
)(AlertsBox)

export {Alerts}