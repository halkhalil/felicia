const defaultState = {
	user: undefined,
	paymentMethods: [],
	currencies: [],
	units: [],
	invoices: {
		minYear: 2016,
		maxYear: 2017
	}
}

export default function configuration(state = defaultState, action) {
	let createState = (oldState = state, adjustment) => { 
		return Object.assign({}, oldState, adjustment)
	}
	
	switch (action.type) {
		case 'replace':
			return createState(state, action.data )
		case 'setUser':
			return createState(state, { user: action.user })
		case 'clearUser':
			return createState(state, { user: undefined })
		default:
			return state
	}
}