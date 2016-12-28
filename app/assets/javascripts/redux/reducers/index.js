import { combineReducers } from 'redux'

import users from './users'
import alerts from './alerts'
import configuration from './configuration'
import adminConfiguration from './adminConfiguration'

const mainReducers = combineReducers({
	users, alerts, configuration, adminConfiguration
})

export default mainReducers