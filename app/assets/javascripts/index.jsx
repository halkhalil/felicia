import React from "react";
import ReactDOM from "react-dom";
import { Provider } from 'react-redux'
import { createStore } from 'redux'

import {Application} from "components/Application";
import mainReducers from "redux/reducers";
import * as ConfigurationActions from "redux/actions/configuration";

let store = createStore(mainReducers)
store.dispatch(ConfigurationActions.replace(initialData))

// render application:
ReactDOM.render(
	<div>
		<Provider store={store}>
			<Application />
		</Provider>
	</div>,
	document.querySelector("#container")
);
