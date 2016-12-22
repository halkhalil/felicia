import * as UsersActionTypes from './types/users'

export function fetchAll(data) {
	return {
		type: UsersActionTypes.FETCH_ALL,
		users: data
	}
}

export function listFetchError(error) {
	return {
		type: UsersActionTypes.LIST_FETCH_ERROR,
		error: error
	}
}

export function fetch(data) {
	return {
		type: 'fetch',
		user: data
	}
}

export function fetchError(error) {
	return {
		type: 'fetch_error',
		error: error
	}
}