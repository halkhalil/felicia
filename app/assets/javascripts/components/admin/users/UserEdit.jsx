import React from "react";
import { connect } from 'react-redux'

import * as UsersActions from "redux/actions/users";
import {UserForm} from "./UserForm";

const mapDispatchToProps = (dispatch) => {
	return {
		fetch: (id) => {
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
		save: (id, data) => {
			const ENDPOINT = '/api/user/' + id
			
			dispatch(UsersActions.fetchError(undefined))
			
			jQuery.ajax({
				type: 'PUT',
				url: ENDPOINT,
				dataType: 'json',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(data),
				success: (data) => {} //dispatch(UsersActions.fetch(data))
			}).fail(
				() => {} //dispatch(UsersActions.fetchError('Error loading user.'))
			)
		}
	}
}

const mapStateToProps = (state) => {
	return {
		user: state.users.user,
		fetchError: state.users.userFetchError
	}
}

const UserEdit = connect(
	mapStateToProps,
	mapDispatchToProps
)(UserForm)

export {UserEdit}