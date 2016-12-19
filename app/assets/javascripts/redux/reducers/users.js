import * as UsersActionTypes from '../actions/types/users'

const defaultState = {
	users: []
}

function users(state = defaultState, action) {
	switch (action.type) {
		case UsersActionTypes.FETCH_ALL:
			return Object.assign({}, state, {
				users: action.users
			})
		default:
			return state
	}
}

export default users