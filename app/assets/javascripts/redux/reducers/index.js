import { combineReducers } from 'redux'

import users from './users'
import alerts from './alerts'

const mainReducers = combineReducers({
	users, alerts
})

export default mainReducers