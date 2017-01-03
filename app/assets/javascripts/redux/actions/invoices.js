export function fetchAll(data) {
	return {
		type: 'invoices.fetchAll',
		invoices: data
	}
}

export function fetch(data) {
	return {
		type: 'invoices.fetch',
		invoice: data
	}
}

export function alterField(field, value) {
	return {
		type: 'invoices.alterField',
		field: field,
		value: value
	}
}