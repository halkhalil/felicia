import React from "react";
import { connect } from 'react-redux'

import {SpinnerBox} from "./SpinnerBox";

const mapDispatchToProps = (dispatch) => {
	return {
		
	}
}

const mapStateToProps = (state) => {
	return {
		visible: state.alerts.processing,
	}
}

const Spinner = connect(
	mapStateToProps,
	mapDispatchToProps
)(SpinnerBox)

export {Spinner}