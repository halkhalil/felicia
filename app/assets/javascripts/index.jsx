import React from "react";
import ReactDOM from "react-dom";
import { Provider } from 'react-redux'
import { createStore } from 'redux'

import {Application} from "components/Application";
import mainReducers from "redux/reducers";

let store = createStore(mainReducers)

// render application:
ReactDOM.render(
	<div>
		<Provider store={store}>
			<Application configuration={initialData} />
		</Provider>
	</div>,
	document.querySelector("#container")
);
