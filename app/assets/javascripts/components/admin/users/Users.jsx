import React from "react";
import { connect } from 'react-redux'

import * as UsersActions from "redux/actions/users";
import * as AlertsActions from "redux/actions/alerts";
import {UsersTable} from "./UsersTable";

const mapDispatchToProps = (dispatch) => {
	return {
		fetchAll: () => {
			const ENDPOINT = '/api/users'
			
			dispatch(UsersActions.listFetchError(undefined))
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({ 
				type: 'GET',
				url: ENDPOINT,
				dataType: 'json',
				success: (data) => dispatch(UsersActions.fetchAll(data))
			}).fail(
				() => dispatch(UsersActions.listFetchError('Error fetching users.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		}
	}
}

const mapStateToProps = (state) => {
	return {
		users: state.users.users,
		fetchError: state.users.list.fetchError,
		fetching: state.alerts.processing
	}
}

const Users = connect(
	mapStateToProps,
	mapDispatchToProps
)(UsersTable)

export {Users}