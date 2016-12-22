import React from "react";
import { connect } from 'react-redux'
import { browserHistory } from 'react-router';

import * as UsersActions from "redux/actions/users";
import {UserEditForm} from "./UserEditForm";

const mapDispatchToProps = (dispatch) => {
	return {
		fetch(id) {
			const ENDPOINT = '/api/user/' + id
			
			dispatch(UsersActions.fetchError(undefined))
			
			jQuery.ajax({ 
				type: 'GET',
				url: ENDPOINT,
				dataType: 'json',
				success: (data) => dispatch(UsersActions.fetch(data))
			}).fail(
				() => dispatch(UsersActions.fetchError('Error loading user.'))
			)
		},
		
		save(id, data) {
			const ENDPOINT = '/api/user/' + id
			
			dispatch(UsersActions.saveError(undefined))
			
			jQuery.ajax({
				type: 'PUT',
				url: ENDPOINT,
				dataType: 'json',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(data),
				success: (data) => {}
			}).fail(
				() => dispatch(UsersActions.saveError('Error saving user.'))
			)
		},
		
		clearSaveError() {
			dispatch(UsersActions.clearSaveError())
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
		saveError: state.users.saveError,
		saving: state.users.saving
	}
}

const UserEdit = connect(
	mapStateToProps,
	mapDispatchToProps
)(UserEditForm)

export {UserEdit}