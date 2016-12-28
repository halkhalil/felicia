export function entries(entries) {
	return {
		type: 'entries',
		entries: entries
	}
}

export function alterField(symbol, value) {
	return {
		type: 'alterField',
		symbol: symbol,
		value: value
	}
}