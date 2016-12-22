import React from "react";
import { connect } from 'react-redux'
import { browserHistory } from 'react-router';

import * as AlertsActions from "redux/actions/alerts";
import {UserAddForm} from "./UserAddForm";

const mapDispatchToProps = (dispatch) => {
	return {
		save: (data) => {
			const ENDPOINT = '/api/user'
			
			jQuery.ajax({
				type: 'POST',
				url: ENDPOINT,
				dataType: 'json',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(data),
				success: (data) => {
					if (data.id !== undefined) {
						browserHistory.push('/admin/user/' + data.id)
						dispatch(AlertsActions.addSuccess('User has been created.'))
					} else {
						dispatch(AlertsActions.addDanger('User was not created.'))
					}
				}
			}).fail(
				(error) => {
					if (error.responseJSON instanceof Object) {
						dispatch(AlertsActions.addDanger('Error creating user: ' + error.responseJSON.error))
					} else {
						dispatch(AlertsActions.addDanger('Error creating user.'))
					}
				}
			)
		},
		
		cancel() {
			browserHistory.push('/admin/users');
		}
	}
}

const mapStateToProps = (state) => {
	return {
		saving: state.users.saving
	}
}

const UserAdd = connect(
	mapStateToProps,
	mapDispatchToProps
)(UserAddForm)

export {UserAdd}