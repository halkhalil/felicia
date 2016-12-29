import { combineReducers } from 'redux'

import users from './users'
import alerts from './alerts'
import configuration from './configuration'
import adminConfiguration from './adminConfiguration'
import paymentMethods from './paymentMethods'

const mainReducers = combineReducers({
	users, alerts, configuration, adminConfiguration, paymentMethods
})

export default mainReducers