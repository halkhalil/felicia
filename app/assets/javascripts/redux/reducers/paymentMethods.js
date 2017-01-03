const defaultState = {
	methods: [],
	method: undefined
}

export default function paymentMethods(state = defaultState, action) {
	let update = (oldState = state, adjustment) => { 
		return Object.assign({}, oldState, adjustment)
	}

	switch (action.type) {
		case 'paymentMethods.fetchAll':
			return update(state, { methods: action.methods })
		case 'paymentMethods.fetch':
			return update(state, { method: action.method })
		case 'paymentMethods.alterField':
			let method = update(state.method, { [action.field]: action.value })
			
			return update(state, { method: method })
		default:
			return state
	}
}