package com.stud.servlet.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.stud.servlet.dao.impl.UserDAOImpl;
import com.stud.servlet.models.User;

@WebServlet(
		urlPatterns = {"/create", "/read", "/update", "/delete", "/list"}
)
public class UserController extends HttpServlet{
	private static final long serialVersionUID = -5317134869447984976L;
	private UserDAOImpl userDAO;
	
	public void init() {
		//String jdbcURL = getServletContext().getInitParameter("jdbcURL");
		//String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
		//String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
		
		//userDAO = new UserDAOImpl(jdbcURL, jdbcUsername, jdbcPassword);
		userDAO = new UserDAOImpl();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		System.out.println("request: "+request.getServletPath());
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		String action = request.getServletPath();
		try {
			switch(action) {
				case "/create":
					createUser(request, response);
				break;
				case "/read":
					readUser(request, response);
				break;
				case "/update":
					updateUser(request, response);
				break;
				case "/delete":
					deleteUser(request, response);
				break;
				case "/list":
					listUser(request, response);
				break;
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	private void createUser(HttpServletRequest request, HttpServletResponse response)
		throws SQLException {
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			BufferedReader br = request.getReader();
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		try {
			JSONObject jsonObj = new JSONObject(sb.toString());
			User user = new User(
				jsonObj.get("firstName").toString(),
				jsonObj.get("lastName").toString(),
				jsonObj.get("login").toString(),
				jsonObj.get("email").toString(),
				jsonObj.get("password").toString()
			);
			userDAO.createUser(user);
		}
		catch(JSONException ex) {
			ex.printStackTrace();
		}
	}
	
	private void readUser(HttpServletRequest request, HttpServletResponse response)
		throws SQLException, IOException {
		Long id = Long.parseLong(request.getParameter("id"));
		String json = new JSONObject(userDAO.readUser(id).toJson()).toString();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
	
	private void updateUser(HttpServletRequest request, HttpServletResponse response)
		throws SQLException {
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			BufferedReader br = request.getReader();
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		try {
			JSONObject jsonObj = new JSONObject(sb.toString());
			User user = new User(
				Long.parseLong(jsonObj.get("id").toString()),
				jsonObj.get("firstName").toString(),
				jsonObj.get("lastName").toString(),
				jsonObj.get("login").toString(),
				jsonObj.get("email").toString(),
				jsonObj.get("password").toString()
			);
			userDAO.updateUser(user);
		}
		catch(JSONException ex) {
			ex.printStackTrace();
		}
	}
	
	private void deleteUser(HttpServletRequest request, HttpServletResponse response)
		throws SQLException {
		Long id = Long.parseLong(request.getParameter("id"));
		
		userDAO.deleteUser(id);
	}
	
	private void listUser(HttpServletRequest request, HttpServletResponse response)
		throws SQLException, IOException {
		List<User> listUser = userDAO.listUser();
		String json = "[";
		for(int i=0; i<listUser.size(); i++) {
			json = i != listUser.size()-1 
					? json + new JSONObject(listUser.get(i).toJson()).toString() + ",\n"
					: json + new JSONObject(listUser.get(i).toJson()).toString() + "]";
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
}
