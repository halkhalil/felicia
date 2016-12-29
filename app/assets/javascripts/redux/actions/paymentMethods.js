export function fetchAll(data) {
	return {
		type: 'fetchAll',
		methods: data
	}
}

export function fetch(data) {
	return {
		type: 'fetch',
		method: data
	}
}

export function alterPaymentMethodField(field, value) {
	return {
		type: 'alterPaymentMethodField',
		field: field,
		value: value
	}
}