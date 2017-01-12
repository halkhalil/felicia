import React from "react";
import numeral from "numeral"
import moment from "moment"
import Calendar from 'react-input-calendar'
import 'react-input-calendar/style/index.css'

class CurrencyRatesTable extends React.Component {
	
	constructor() {
		super()
		
		this.state = {
			filterDate: moment().format('YYYY-MM-DD')
		}
	}
	
	componentDidMount() {
		this.handleChange(this.state.filterDate)
	}
	
	handleChange(date) {
		let day = moment(date)
		this.props.fetchAll(day.format('YYYY'), day.format('M'), day.format('D'))
		this.setState({ filterDate: date })
	}
	
	render() {
		return (
			<div>
				<h3>Currencies</h3>
				
				<div className="row">
					<label className="form-control-static col-sm-1">Date:</label>
					<div className="col-sm-11 form-inline">
						<Calendar 
							format="YYYY-MM-DD"
							closeOnSelect={true}
							hideOnBlur={true}
							computableFormat="YYYY-MM-DD"
							date={this.state.filterDate} onChange={(date) => this.handleChange(date)}
							inputFieldClass="form-control"
						/>
					</div>
				</div>
				<div className="help-block"></div>
				<table className="table table-striped table-bordered table-nonfluid">
					<thead>
						<tr>
							<th>Source</th>
							<th>Target</th>
							<th>Rate</th>
							<th>Date</th>
						</tr>
					</thead>
					<tbody>
						{
							this.props.fetching &&
							<tr className="info">
								<td colSpan="4">
									Loading ...
								</td>
							</tr>
						}
						{
							this.props.currencies.map((currency) => {
								return (
									<tr key={currency.id}>
										<td>
											{currency.source}
										</td>
										<td>
											{currency.target}
										</td>
										<td>
											{numeral(currency.rate).format('0,0.0000')}
										</td>
										<td>
											{currency.day}
										</td>
									</tr>
								)
							})
						}
						{
							this.props.currencies.length === 0 && !this.props.fetching &&
							<tr className="info">
								<td colSpan="4">
									No currencies found
								</td>
							</tr>
						}
					</tbody>
				</table>
			</div>
		)
	}
}

// property validators:
CurrencyRatesTable.propTypes = {
	fetchAll: React.PropTypes.func.isRequired,
	fetching: React.PropTypes.bool.isRequired
};

export default CurrencyRatesTable