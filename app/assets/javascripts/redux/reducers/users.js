import * as UsersActionTypes from '../actions/types/users'

const defaultState = {
	users: [],
	list: {
		fetchError: undefined,
		fetching: false
	},
	user: undefined,
	userFetchError: undefined,
	saving: false,
	saveError: undefined
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
			
		case 'fetch':
			return createState(state, { user: action.user })
		case 'fetch_error':
			return createState(state, { userFetchError: action.error })
		case 'saveError':
			return createState(state, { saveError: action.error })
		case 'saving':
			return createState(state, { saving: action.isSaving })
			
		default:
			return state
	}
}