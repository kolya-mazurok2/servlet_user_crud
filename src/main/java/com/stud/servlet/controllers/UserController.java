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
		userDAO = new UserDAOImpl();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		String action = request.getServletPath();
		try {
			switch(action) {
				case "/create":
					createAction(request, response);
				break;
				case "/read":
					readAction(request, response);
				break;
				case "/update":
					updateAction(request, response);
				break;
				case "/delete":
					deleteAction(request, response);
				break;
				case "/list":
					getListAction(request, response);
				break;
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	private void createAction(HttpServletRequest request, HttpServletResponse response)
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
	
	private void readAction(HttpServletRequest request, HttpServletResponse response)
		throws SQLException, IOException {
		Long id = Long.parseLong(request.getParameter("id"));
		User user = userDAO.readUser(id);
		String json = objectToJson(user);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
	
	private void updateAction(HttpServletRequest request, HttpServletResponse response)
		throws SQLException {
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			BufferedReader br = request.getReader();
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
			User user = jsonToObject(sb);
			userDAO.updateUser(user);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void deleteAction(HttpServletRequest request, HttpServletResponse response)
		throws SQLException {
		Long id = Long.parseLong(request.getParameter("id"));
		
		userDAO.deleteUser(id);
	}
	
	private void getListAction(HttpServletRequest request, HttpServletResponse response)
		throws SQLException, IOException {
		List<User> list = userDAO.listUser();
		String json = objectsToJson(list);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
	
	private String objectToJson(User user) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", user.getId());
		jsonObj.put("firstName", user.getFirstName());
		jsonObj.put("lastName", user.getLastName());
		jsonObj.put("login", user.getLogin());
		jsonObj.put("email", user.getEmail());
		jsonObj.put("password", user.getPassword());
		
		return jsonObj.toString();
	}
	
	private String objectsToJson(List<User> list) {
		String json = "[";
		for(int i=0; i<list.size(); i++) {
			json = i != list.size()-1 
				? json + objectToJson(list.get(i)) + ",\n"
				: json + objectToJson(list.get(i)) + "]";
		}
		
		return json;
	}
	
	private User jsonToObject(StringBuffer sb) {
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
			return user;
		}
		catch(JSONException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
