import React from "react";
import { Link } from 'react-router'

import {Confirm} from "components/common/dialogs/Confirm";

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
							<th>Name</th>
							<th>Login</th>
							<th>Role</th>
							<th width="150"></th>
						</tr>
					</thead>
					<tbody>
						{
							this.props.fetching &&
							<tr className="info">
								<td colSpan="5">
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
											{user.name}
										</td>
										<td>
											{user.login}
										</td>
										<td>
											{user.role}
										</td>
										<td>
											<div className="btn-group">
												<Link to={'/admin/user/' + user.id} className="btn btn-primary btn-xs">
													<span className="glyphicon glyphicon-pencil"></span> Edit
												</Link>
												<a className="btn btn-danger btn-xs" data-toggle="modal" data-target={`#deleteDialog${user.id}`} href="#">
													<span className="glyphicon glyphicon-remove"></span> Delete
												</a>
												<Confirm id={`deleteDialog${user.id}`} text="Are you sure you want to delete this user?" onConfirmed={() => this.props.delete(user.id)} />
											</div>
										</td>
									</tr>
								)
							})
						}
						{
							this.props.users.length === 0 && this.props.fetchError === undefined && !this.props.fetching &&
							<tr className="info">
								<td colSpan="5">
									No users found
								</td>
							</tr>
						}
						{
							this.props.fetchError !== undefined && 
							<tr className="danger">
								<td colSpan="5">
									{this.props.fetchError}
								</td>
							</tr>
						}
					</tbody>
				</table>
				
				<Link to="/admin/users/add" className="btn btn-primary btn-sm"><span className="glyphicon glyphicon-plus"></span> Add User</Link>
			</div>
		)
	}
}

// property validators:
UsersTable.propTypes = {
	fetchAll: React.PropTypes.func.isRequired,
	delete: React.PropTypes.func.isRequired,
	fetching: React.PropTypes.bool.isRequired,
	users: React.PropTypes.arrayOf(React.PropTypes.shape({
		login: React.PropTypes.string.isRequired,
		name: React.PropTypes.string.isRequired,
		id: React.PropTypes.number.isRequired
	})).isRequired
};

export {UsersTable}