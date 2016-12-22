import React from "react";
import { connect } from 'react-redux'
import { browserHistory } from 'react-router';

import * as UsersActions from "redux/actions/users";
import * as AlertsActions from "redux/actions/alerts";
import {UserEditForm} from "./UserEditForm";

const mapDispatchToProps = (dispatch) => {
	return {
		fetch(id) {
			const ENDPOINT = '/api/user/' + id
			
			dispatch(UsersActions.fetchError(undefined))
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({
				type: 'GET',
				url: ENDPOINT,
				dataType: 'json',
				success: (data) => dispatch(UsersActions.fetch(data))
			}).fail(
				() => dispatch(UsersActions.fetchError('Error loading user.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		},
		
		save(id, data) {
			const ENDPOINT = '/api/user/' + id
			
			dispatch(AlertsActions.processingOn())
			jQuery.ajax({
				type: 'PUT',
				url: ENDPOINT,
				dataType: 'json',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(data),
				success: (data) => {
					dispatch(AlertsActions.addSuccess('User data has been saved.'))
				}
			}).fail(
				() => dispatch(AlertsActions.addDanger('Error while saving user.'))
			).always(
				() => dispatch(AlertsActions.processingOff())
			)
		},
		
		cancel() {
			browserHistory.push('/admin/users');
		}
	}
}

const mapStateToProps = (state) => {
	return {
		user: state.users.user,
		fetchError: state.users.userFetchError,
		saving: state.alerts.processing
	}
}

const UserEdit = connect(
	mapStateToProps,
	mapDispatchToProps
)(UserEditForm)

export {UserEdit}