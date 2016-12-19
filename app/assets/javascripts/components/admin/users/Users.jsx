import React from "react";
import { connect } from 'react-redux'

import * as UsersActions from "redux/actions/users";
import {UsersTable} from "./UsersTable";

const mapDispatchToProps = (dispatch) => {
	return {
		fetchAll: () => {
			jQuery.ajax({ 
				type: 'GET',
				url: '/api/users',
				dataType: 'json',
				success: function(data) {
					dispatch(
						UsersActions.fetchAll(data)
					)
				}
			}).fail(() => {
				console.log('error')
			})
		}
	}
}

const mapStateToProps = (state) => {
	return {
		users: state.users.users
	}
}

const Users = connect(
	mapStateToProps,
	mapDispatchToProps
)(UsersTable)

export {Users}