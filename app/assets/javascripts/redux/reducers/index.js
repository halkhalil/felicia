import { combineReducers } from 'redux'

import users from './users'
import alerts from './alerts'
import configuration from './configuration'

const mainReducers = combineReducers({
	users, alerts, configuration
})

export default mainReducers