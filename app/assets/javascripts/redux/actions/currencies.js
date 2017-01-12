export function fetchAll(data) {
	return {
		type: 'currencies.fetchAll',
		currencies: data
	}
}