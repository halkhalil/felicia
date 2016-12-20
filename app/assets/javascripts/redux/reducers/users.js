import * as UsersActionTypes from '../actions/types/users'

const defaultState = {
	users: [],
	list: {
		fetchError: undefined,
		fetching: false
	}
}

export default function users(state = defaultState, action) {
	let createState = (oldState = state, adjustment) => { 
		return Object.assign({}, oldState, adjustment)
	}
	
	switch (action.type) {
		case UsersActionTypes.FETCH_ALL:
			return createState(state, { users: action.users })
		case UsersActionTypes.LIST_FETCH_ERROR:
			return createState(state, {
				list: createState(state.list, { fetchError: action.error })
			})
		case UsersActionTypes.LIST_FETCHING:
			return createState(state, {	
				list: createState(state.list, { fetching: action.isFetching })
			})
		default:
			return state
	}
}