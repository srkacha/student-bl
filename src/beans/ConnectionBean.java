package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dao.ConnectionDAO;
import dao.UserDAO;
import dto.User;

public class ConnectionBean implements Serializable{
	
	private User loggedInUser = new User();
	private ConnectionDAO connectionDAO = new ConnectionDAO();
	private UserDAO userDAO = new UserDAO();
	
	public List<User> getOnlineConnections(){
		List<User> connections = userDAO.selectConnections(loggedInUser.getId());
		List<User> online = connections.stream().filter((u) -> userDAO.isUserOnilne(u.getId())).collect(Collectors.toList());
		return online;
		
	}
	
	public List<User> getOtherUsers(){
		return userDAO.selectAllOtherUsers(loggedInUser.getId());
	}
	
	public List<User> getOtherUsersFromFaculty(int facultyId){
		return userDAO.selectOtherUsersFormFaculty(loggedInUser.getId(), facultyId);
	}
	
	public List<User> getConnections(){
		return userDAO.selectConnections(loggedInUser.getId());
	}
	
	public List<User> getRequests() {
		return userDAO.selectRequests(loggedInUser.getId());
	}


	public User getLoggedInUser() {
		return loggedInUser;
	}



	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}



	public ConnectionDAO getConnectionDAO() {
		return connectionDAO;
	}



	public void setConnectionDAO(ConnectionDAO connectionDAO) {
		this.connectionDAO = connectionDAO;
	}



	public int getNumberOfRequestsForUser() {
		return connectionDAO.countRequestsForId(loggedInUser.getId());
	}
}
