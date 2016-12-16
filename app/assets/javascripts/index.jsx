import React from "react";
import ReactDOM from "react-dom";

import {Application} from "components/Application.jsx";

// render application:
ReactDOM.render(
	<div>
		<Application configuration={initialData} />
	</div>,
	document.querySelector("#container")
);
