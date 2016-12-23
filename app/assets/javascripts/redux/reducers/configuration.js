const defaultState = {

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
		case 'clear':
			return {}
		default:
			return state
	}
}