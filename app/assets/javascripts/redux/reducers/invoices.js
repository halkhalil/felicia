const defaultState = {
	invoices: [],
	invoice: undefined
}

export default function invoices(state = defaultState, action) {
	let update = (oldState = state, adjustment) => { 
		return Object.assign({}, oldState, adjustment)
	}

	switch (action.type) {
		case 'fetchAll':
			return update(state, { invoices: action.invoices })
		case 'fetch':
			return update(state, { invoice: action.invoice })
		case 'alterField':
			let invoice = update(state.invoice, { [action.field]: action.value })
			
			return update(state, { invoice: invoice })
		default:
			return state
	}
}