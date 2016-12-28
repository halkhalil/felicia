const defaultState = {
	entries: []
}

export default function adminConfiguration(state = defaultState, action) {
	let update = (oldState = state, adjustment) => {
		return Object.assign({}, oldState, adjustment)
	}
	
	switch (action.type) {
		case 'entries':
			return update(state, { entries: action.entries })
		case 'alterField':
			let newEntries = state.entries.map((entry) => entry.symbol === action.symbol ? update(entry, { value: action.value }) : entry)
			
			return update(state, { entries: newEntries })
		default:
			return state
	}
}