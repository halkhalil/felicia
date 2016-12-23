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

export function clear() {
	return {
		type: 'clear'
	}
}