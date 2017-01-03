const defaultState = {
	invoices: [],
	invoice: undefined
}

export default function invoices(state = defaultState, action) {
	let update = (oldState = state, adjustment) => { 
		return Object.assign({}, oldState, adjustment)
	}

	switch (action.type) {
		case 'invoices.fetchAll':
			return update(state, { invoices: action.invoices })
		case 'invoices.fetch':
			return update(state, { invoice: action.invoice })
		case 'invoices.alterField':
			let invoice = update(state.invoice, { [action.field]: action.value })
			
			return update(state, { invoice: invoice })
		default:
			return state
	}
}