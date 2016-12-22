const defaultState = {
	list: [],
	processing: false
}

export default function alerts(state = defaultState, action) {
	let createState = (oldState = state, adjustment) => { 
		return Object.assign({}, oldState, adjustment)
	}
	
	switch (action.type) {
		case 'add':
			let alert = {
				category: action.category,
				text: action.text,
				time: action.time
			}
			return createState(state, { list: [...state.list, alert ] })
		case 'clean':	
			let filtered = state.list.filter((item) => (Date.now() / 1000) - item.time < action.timeout )
				
			return createState(state, { list: [...filtered ] })
		case 'processingOn':
			return createState(state, { processing: true })
		case 'processingOff':
			return createState(state, { processing: false })
		default:
			return state
	}
}