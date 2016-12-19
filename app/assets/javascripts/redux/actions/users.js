import * as UsersActionTypes from './types/users'

export function fetchAll(data) {
	return {
		type: UsersActionTypes.FETCH_ALL,
		users: data
	}
}