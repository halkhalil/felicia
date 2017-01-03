export function fetchAll(data) {
	return {
		type: 'paymentMethods.fetchAll',
		methods: data
	}
}

export function fetch(data) {
	return {
		type: 'paymentMethods.fetch',
		method: data
	}
}

export function alterPaymentMethodField(field, value) {
	return {
		type: 'paymentMethods.alterField',
		field: field,
		value: value
	}
}