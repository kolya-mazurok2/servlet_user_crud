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
	private static final String JDBC_PROPERTY_URL = "jdbc:mysql://localhost:3306/spring_jdbc?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
	private static final String JDBC_PROPERTY_USERNAME = "root";
	private static final String JDBC_PROPERTY_PASSWORD = "1";
	private static final String SQL_STATEMENT_CREATE = "INSERT INTO user "
			+ "(first_name, last_name, login, email, password) "
			+ "VALUES (?,?,?,?,?)";
	private static final String SQL_STATEMENT_READ = "SELECT * FROM user WHERE id = ?";
	private static final String SQL_STATEMENT_UPDATE = "UPDATE user SET "
			+ "first_name = ?, "
			+ "last_name = ?, "
			+ "login = ?, "
			+ "email = ?, "
			+ "password = ? "
			+ "WHERE id = ?";
	private static final String SQL_STATEMENT_DELETE = "DELETE FROM user  WHERE id = ?";
	private static final String SQL_STATEMENT_GET_LIST = "SELECT * FROM user";
	
	private Connection jdbcConnection;
	
	public UserDAOImpl() {};
	
	protected void connect() throws SQLException {
		if(jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			}
			catch (ClassNotFoundException ex) {
				throw new SQLException(ex);
			}
			jdbcConnection = DriverManager.getConnection(JDBC_PROPERTY_URL, JDBC_PROPERTY_USERNAME, JDBC_PROPERTY_PASSWORD);
		}
	}
	
	protected void disconnect() throws SQLException {
		if(jdbcConnection != null && !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}
	
	public boolean createUser(User user) throws SQLException {
		PreparedStatement ps = jdbcConnection.prepareStatement(SQL_STATEMENT_CREATE);
		
		return executeUpdate(ps, user, null, null);
	}
	
	public User readUser(Long id) throws SQLException {
		PreparedStatement ps = jdbcConnection.prepareStatement(SQL_STATEMENT_READ);
		
		return executeQuery(ps, id);
	}
	
	public boolean updateUser(User user) throws SQLException {
		PreparedStatement ps = jdbcConnection.prepareStatement(SQL_STATEMENT_UPDATE);
		
		return executeUpdate(ps, user, null, true);
	}
	
	public boolean deleteUser(long id) throws SQLException {
		PreparedStatement ps = jdbcConnection.prepareStatement(SQL_STATEMENT_DELETE);
		
		return executeUpdate(ps, null, id, null);
	}
	
	public List<User> listUser() throws SQLException {
		Statement st = jdbcConnection.createStatement();
		
		return executeQuery(st);
	}
	
	private User executeQuery(PreparedStatement ps, Long id) {
		User user = null;
		try {
			connect();
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				id = rs.getLong("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String login = rs.getString("login");
				String email = rs.getString("email");
				String password = rs.getString("password");
				
				user = new User(id, firstName, lastName, login, email, password);
			}
			rs.close();
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				ps.close();
				disconnect();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return user;
	}
	
	private List<User> executeQuery(Statement st) {
		List<User> list = new ArrayList<>();
		try {
			connect();
			ResultSet rs = st.executeQuery(SQL_STATEMENT_GET_LIST);
			
			while(rs.next()) {
				long id = rs.getLong("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String login = rs.getString("login");
				String email = rs.getString("email");
				String password = rs.getString("password");
				
				list.add(new User(id, firstName, lastName, login, email, password));
			}
			rs.close();
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				st.close();
				disconnect();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}
	
	private Boolean executeUpdate(PreparedStatement ps, User user, Long id, Boolean update) {
		Boolean row = null;
		
		try {
			connect();
			
			if(id == null) {
				ps.setString(1, user.getFirstName());
				ps.setString(2, user.getLastName());
				ps.setString(3, user.getLogin());
				ps.setString(4, user.getEmail());
				ps.setString(5, user.getPassword());
				
				if(update == true) {
					ps.setLong(6, user.getId());
				}
			}
			else {
				ps.setLong(1, id);
			}
			
			row = ps.executeUpdate() > 0;
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				ps.close();
				disconnect();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return row;
	}
}
