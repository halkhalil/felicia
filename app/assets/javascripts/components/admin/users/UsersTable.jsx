import React from "react";
import { Link } from 'react-router'

class UsersTable extends React.Component {
	
	componentDidMount() {
		this.props.fetchAll()
	}
	
	render() {
		return (
			<div>
				<h3>Users</h3>
				<table className="table table-striped table-bordered">
					<thead>
						<tr>
							<th width="50">ID</th>
							<th>Login</th>
							<th width="150"></th>
						</tr>
					</thead>
					<tbody>
						{
							this.props.fetching &&
							<tr className="info">
								<td colSpan="3">
									Loading ...
								</td>
							</tr>
						}
						{
							this.props.users.map((user) => {
								return (
									<tr key={user.id}>
										<td>
											{user.id}
										</td>
										<td>
											{user.login}
										</td>
										<td>
											<div className="btn-group">
												<Link to={'/admin/users/' + user.id} className="btn btn-primary btn-xs">
													<span className="glyphicon glyphicon-pencil"></span> Edit
												</Link>
												<Link to={'/admin/users/' + user.id + '/delete'} className="btn btn-danger btn-xs">
													<span className="glyphicon glyphicon-remove"></span> Delete
												</Link>
											</div>
										</td>
									</tr>
								)
							})
						}
						{
							this.props.users.length === 0 && this.props.fetchError === undefined && !this.props.fetching &&
							<tr className="info">
								<td colSpan="3">
									No users found
								</td>
							</tr>
						}
						{
							this.props.fetchError !== undefined && 
							<tr className="danger">
								<td colSpan="3">
									{this.props.fetchError}
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
UsersTable.propTypes = {
	fetchAll: React.PropTypes.func.isRequired,
	fetching: React.PropTypes.bool.isRequired,
	users: React.PropTypes.arrayOf(React.PropTypes.shape({
		login: React.PropTypes.string.isRequired,
		id: React.PropTypes.number.isRequired
	})).isRequired
};

export {UsersTable}