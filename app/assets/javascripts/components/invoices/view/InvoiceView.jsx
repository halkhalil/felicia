import React from "react";
import moment from "moment";
import numeral from "numeral"
import { Link } from 'react-router'
import { Modal, Button } from 'react-bootstrap'
import Select from 'react-select'
import 'react-select/dist/react-select.css'

class InvoiceView extends React.Component {

	constructor() {
		super()
		
		this.state = {
			open: false,
			currency: 'usd',
			language: 'en'
		}
		this.handleOpen = this.handleOpen.bind(this)
		this.handleClose = this.handleClose.bind(this)
		this.handlePrint = this.handlePrint.bind(this)
		this.printUrl = this.printUrl.bind(this)
	}
	
	handleOpen() {
		this.setState({ open: true });
	}
	
	handleClose() {
		this.setState({ open: false });
	}
	
	handlePrint() {
		let contentWindow = this.refs.printIframe.contentWindow
		contentWindow.focus()
		contentWindow.print()
	}
	
	printUrl() {
		return `/print/invoice/${this.props.id}/${this.state.currency}/${this.state.language}`
	}
	
	render() {
		let currencies = [
			{ value: 'usd', label: 'USD' },
			{ value: 'pln', label: 'PLN' }
		]
		
		let languages = [
			{ value: 'en', label: 'EN' },
			{ value: 'pl', label: 'PL' }
		]
		
		return (
			<span>
				<Button bsStyle="info" bsSize="xsmall" onClick={this.handleOpen}><span className="glyphicon glyphicon-list-alt"></span> View</Button>

				<Modal show={this.state.open} onHide={this.handleClose} dialogClassName="large-full">
					<Modal.Header closeButton>
						<Modal.Title>Invoice View</Modal.Title>
					</Modal.Header>
					<Modal.Body>
						<div className="row">
							<label className="form-control-static col-sm-1 text-right">Currency: </label>
							<div className="col-sm-1 form-inline">
								<Select
									clearable={false}
									searchable={false}
									value={this.state.currency}
									options={currencies}
									onChange={(selection) => this.setState({ currency: selection.value })}
								/>
							</div>
							<label className="form-control-static col-sm-1 text-right">Language: </label>
							<div className="col-sm-1 form-inline">
								<Select
									clearable={false}
									searchable={false}
									value={this.state.language}
									options={languages}
									onChange={(selection) => this.setState({ language: selection.value })}
								/>
							</div>
						</div>
						
						<br />
						<div className="panel panel-default">
							<div className="panel-body">
								<iframe ref="printIframe" src={this.printUrl()} frameBorder="0" width="100%" height="500"> </iframe>
							</div>
						</div>
					</Modal.Body>
					<Modal.Footer>
						<Button bsStyle="primary" onClick={this.handlePrint}><span className="glyphicon glyphicon-print"></span> Print</Button>
						<Button onClick={this.handleClose}>Close</Button>
					</Modal.Footer>
				</Modal>
			</span>
		)
	}
}

// property validators:
InvoiceView.propTypes = {
	year: React.PropTypes.number.isRequired,
	month: React.PropTypes.number.isRequired
};

export default InvoiceView