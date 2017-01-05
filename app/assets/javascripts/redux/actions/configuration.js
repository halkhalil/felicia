export function replace(data) {
	return {
		type: 'replace',
		data: data
	}
}

export function setUser(user) {
	return {
		type: 'setUser',
		user: user
	}
}

export function clearUser() {
	return {
		type: 'clearUser'
	}
}