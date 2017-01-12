import { combineReducers } from 'redux'

import users from './users'
import alerts from './alerts'
import configuration from './configuration'
import adminConfiguration from './adminConfiguration'
import paymentMethods from './paymentMethods'
import invoices from './invoices'
import currencies from './currencies'

const mainReducers = combineReducers({
	users, alerts, configuration, adminConfiguration, paymentMethods, invoices, currencies
})

export default mainReducers