package beans;
import java.io.Serializable;

import dao.UserDAO;
import dto.User;
import util.SecureUtil;

public class UserBean implements Serializable{
	
	private User loggedInUser = new User();
	private User userToShow = new User();
	private UserDAO userDAO = new UserDAO();
	private boolean isLoggedIn = false;
	
	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public boolean loginUser(String username, String password) {
		boolean result = false;
		if((loggedInUser = userDAO.select(username, SecureUtil.generateSHA256Hash(password))) != null){
			isLoggedIn = true;
			result = true;
		}
		return result;
	}
	
	public void logoutUser() {
		loggedInUser = new User();
		isLoggedIn = false;
	}
	
	public boolean registerUser(User user) {
		return userDAO.insert(user);
	}

	public User getUserToShow() {
		return userToShow;
	}

	public void setUserToShow(User userToShow) {
		this.userToShow = userToShow;
	}
	
	
}
