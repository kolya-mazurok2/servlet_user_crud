package com.stud.servlet.dao;

import java.sql.SQLException;
import java.util.List;

import com.stud.servlet.models.User;

public interface UserDAO {
	public boolean createUser(User user) throws SQLException;
	public User readUser(long id) throws SQLException;
	public boolean updateUser(User user) throws SQLException;
	public boolean deleteUser(long id) throws SQLException;
	public List<User> listUser() throws SQLException;
}
