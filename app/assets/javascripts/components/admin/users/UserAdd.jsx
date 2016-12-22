import React from "react";
import { connect } from 'react-redux'
import { browserHistory } from 'react-router';

import * as UsersActions from "redux/actions/users";
import {UserAddForm} from "./UserAddForm";

const mapDispatchToProps = (dispatch) => {
	return {
		save: (data) => {
			const ENDPOINT = '/api/user'
			
			dispatch(UsersActions.saveError(undefined))
			
			jQuery.ajax({
				type: 'POST',
				url: ENDPOINT,
				dataType: 'json',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(data),
				success: (data) => {
					if (data.id !== undefined)
						browserHistory.push('/admin/user/' + data.id);
				}
			}).fail(
				() => dispatch(UsersActions.saveError('Error creating user.'))
			)
		},
		clearSaveError: () => {
			dispatch(UsersActions.clearSaveError())
		},
		
		cancel() {
			browserHistory.push('/admin/users');
		}
	}
}

const mapStateToProps = (state) => {
	return {
		saveError: state.users.saveError,
		saving: state.users.saving
	}
}

const UserAdd = connect(
	mapStateToProps,
	mapDispatchToProps
)(UserAddForm)

export {UserAdd}