const defaultState = {
	currencies: [],
}

export default function currencies(state = defaultState, action) {
	let update = (oldState = state, adjustment) => { 
		return Object.assign({}, oldState, adjustment)
	}

	switch (action.type) {
		case 'currencies.fetchAll':
			return update(state, { currencies: action.currencies })
		default:
			return state
	}
}