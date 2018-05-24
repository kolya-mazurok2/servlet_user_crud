package com.stud.servlet.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.stud.servlet.dao.UserDAO;
import com.stud.servlet.models.User;

public class UserDAOImpl implements UserDAO {
	private static final String jdbcURL = "jdbc:mysql://localhost:3306/spring_jdbc?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
	private static final String jdbcUsername = "root";
	private static final String jdbcPassword = "1";
	private Connection jdbcConnection;
	
	private static final String sqlCreate = "INSERT INTO user "
			+ "(first_name, last_name, login, email, password) "
			+ "VALUES (?,?,?,?,?)";
	private static final String sqlRead = "SELECT * FROM user WHERE id = ?";
	private static final String sqlUpdate = "UPDATE user SET "
			+ "first_name = ?, "
			+ "last_name = ?, "
			+ "login = ?, "
			+ "email = ?, "
			+ "password = ? "
			+ "WHERE id = ?";
	private static final String sqlDelete = "DELETE FROM user  WHERE id = ?";
	private static final String sqlList = "SELECT * FROM user";
	
	public UserDAOImpl() {};
	/*public UserDAOImpl(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}*/
	
	protected void connect() throws SQLException {
		if(jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			}
			catch (ClassNotFoundException ex) {
				throw new SQLException(ex);
			}
			jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}
	}
	
	protected void disconnect() throws SQLException {
		if(jdbcConnection != null && !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}
	
	public boolean createUser(User user) throws SQLException {
		connect();
		PreparedStatement ps = jdbcConnection.prepareStatement(sqlCreate);
		ps.setString(1, user.getFirstName());
		ps.setString(2, user.getLastName());
		ps.setString(3, user.getLogin());
		ps.setString(4, user.getEmail());
		ps.setString(5, user.getPassword());
		
		boolean rowInserted = ps.executeUpdate() > 0;
		ps.close();
		disconnect();
		
		return rowInserted;
	}
	
	public User readUser(long id) throws SQLException {
		User user = null;
		connect();
		PreparedStatement ps = jdbcConnection.prepareStatement(sqlRead);
		ps.setLong(1, id);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			String login = rs.getString("login");
			String email = rs.getString("email");
			String password = rs.getString("password");
			
			user = new User(id, firstName, lastName, login, email, password);
		}
		
		rs.close();
		ps.close();
		
		return user;
	}
	
	public boolean updateUser(User user) throws SQLException {
		connect();
		PreparedStatement ps = jdbcConnection.prepareStatement(sqlUpdate);
		ps.setString(1, user.getFirstName());
		ps.setString(2, user.getLastName());
		ps.setString(3, user.getLogin());
		ps.setString(4, user.getEmail());
		ps.setString(5, user.getPassword());
		ps.setLong(6, user.getId());
		
		boolean rowUpdated = ps.executeUpdate() > 0;
		ps.close();
		disconnect();
		
		return rowUpdated;
	}
	
	public boolean deleteUser(long id) throws SQLException {
		connect();
		PreparedStatement ps = jdbcConnection.prepareStatement(sqlDelete);
		ps.setLong(1, id);
		
		boolean rowDeleted = ps.executeUpdate() > 0;
		ps.close();
		disconnect();
		
		return rowDeleted;
	}
	
	public List<User> listUser() throws SQLException {
		List<User> listUser = new ArrayList<>();
		connect();
		Statement st = jdbcConnection.createStatement();
		ResultSet rs = st.executeQuery(sqlList);
		
		while(rs.next()) {
			long id = rs.getLong("id");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			String login = rs.getString("login");
			String email = rs.getString("email");
			String password = rs.getString("password");
			
			listUser.add(new User(id, firstName, lastName, login, email, password));
		}
		
		rs.close();
		st.close();
		disconnect();
		
		return listUser;
	}
}
