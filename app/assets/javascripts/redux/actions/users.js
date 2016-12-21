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

export function listFetching(isFetching) {
	return {
		type: UsersActionTypes.LIST_FETCHING,
		isFetching: isFetching
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

export function saveError(error) {
	return {
		type: 'saveError',
		error: error
	}
}

export function clearSaveError() {
	return {
		type: 'saveError',
		error: undefined
	}
}

export function saving(isSaving) {
	return {
		type: 'saving',
		isSaving: isSaving
	}
}