import React from "react";

export class UsersTable extends React.Component {
	
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
							<th>ID</th>
							<th>Login</th>
						</tr>
					</thead>
					<tbody>
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
									</tr>
								)
							})
						}
					</tbody>
				</table>
			</div>
		)
	}
}