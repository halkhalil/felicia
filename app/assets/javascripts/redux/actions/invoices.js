export function fetchAll(data) {
	return {
		type: 'fetchAll',
		invoices: data
	}
}

export function fetch(data) {
	return {
		type: 'fetch',
		invoice: data
	}
}

export function alterField(field, value) {
	return {
		type: 'alterField',
		field: field,
		value: value
	}
}