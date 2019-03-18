package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.org.apache.xpath.internal.operations.And;

import beans.ConnectionBean;
import beans.ContentBean;
import beans.FacultyBean;
import beans.UserBean;
import dao.FacultyDAO;
import dao.LoggingInfoDAO;
import dao.UserDAO;
import dto.User;
import util.SecureUtil;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/Login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UserDAO userDAO = new UserDAO();
    private FacultyDAO facultyDAO = new FacultyDAO();
    private LoggingInfoDAO loggingInfoDAO = new LoggingInfoDAO();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String address = "/WEB-INF/pages/login.jsp";
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		
		//resetting the validation messages
		resetValidationMessages(session);
		
		if(action == null || "".equals(action)) {
			//do nothing, just load the page
			//check if the logged in user is still in session
			UserBean userBean = (UserBean)session.getAttribute("userBean");
			if(userBean != null && userBean.isLoggedIn()) {
				address = "/WEB-INF/pages/home.jsp";
			}
		}else if("register".equals(action)) {
			if(validateUserRegistrationInput(session, request)) {
				//proce registracija
				if(registerUser(session, request)) {
					//odmah ga ulogujemo i pripremimo beanove
					action = "login";
					loginUser(session, request, action);
					address = "/WEB-INF/pages/edit.jsp";
				}
			}
		}else if("login".equals(action)) {
			//login user-a
			if(loginUser(session, request, action)) {
				address = "/Home";
			}else {
				session.setAttribute("loginMessage", "Pogrešno ste unijeli kredencijale");
			}
		}else if("logout".equals(action)){
			UserBean userBean = (UserBean)session.getAttribute("userBean");
			if(userBean != null) {
				//prvo logovanje logouta jer jos imamo id korisnika u beanu
				logLoggingInfo(userBean.getLoggedInUser().getId(), action);
				userBean.logoutUser();
				//ponistavamo sesiju, uklanjamo sve beanove i ostale objekte
				session.invalidate();
			}
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(address);
		dispatcher.forward(request, response);
	}
	
	private boolean loginUser(HttpSession session, HttpServletRequest request, String action) {
		UserBean userBean = new UserBean();
		if(userBean.loginUser(request.getParameter("username"), request.getParameter("password"))) {
			//da bi mogli prikazati svoj nalog, moramo staviti tip konekcije 2
			userBean.getLoggedInUser().setConnectionType(2);
			session.setAttribute("userBean", userBean);
			//pripremamo i sve ostalo
			prepareBeans(session, userBean);
			//upisemo u bazu kad se ulogovao
			logLoggingInfo(userBean.getLoggedInUser().getId(), action);
			
			return true;
		}else return false;
	}
	
	private void logLoggingInfo(int id, String action) {
		if("login".equals(action)) {
			loggingInfoDAO.insert(id);
		}else if("logout".equals(action)) {
			loggingInfoDAO.update_logout(id);
		}
	}
	
	//priprema svih bean objekata koji mogu biti od koristi
	private void prepareBeans(HttpSession session, UserBean userBean) {
		FacultyBean facultyBean = new FacultyBean();
		facultyBean.setFaculties(facultyDAO.getAll());
		session.setAttribute("facultyBean",	facultyBean);
		
		//priprema konekcija
		ConnectionBean connectionBean = new ConnectionBean();
		connectionBean.setLoggedInUser(userBean.getLoggedInUser());
		session.setAttribute("connectionBean", connectionBean);
		
		//priprema content beana
		ContentBean contentBean = new ContentBean();
		contentBean.setLoggedInUserId(userBean.getLoggedInUser().getId());
		session.setAttribute("contentBean", contentBean);
	}
	
	private boolean registerUser(HttpSession session, HttpServletRequest request) {
		UserBean userBean = new UserBean();
		User user = createUser(request);
		return userBean.registerUser(user);
	}
	
	private User createUser(HttpServletRequest request) {
		User user = new User();
		user.setName(request.getParameter("name"));
		user.setSurname(request.getParameter("surname"));
		user.setUsername(request.getParameter("username"));
		user.setMail(request.getParameter("mail"));
		user.setPasswordHash(SecureUtil.generateSHA256Hash(request.getParameter("password")));
		return user;
	}
	
	private void resetValidationMessages(HttpSession session) {
		session.setAttribute("loginMessage", "");
		session.setAttribute("nameMessage", "");
		session.setAttribute("surnameMessage", "");
		session.setAttribute("usernameMessage", "");
		session.setAttribute("mailMessage", "");
		session.setAttribute("passwordMessage", "");
		session.setAttribute("rePasswordMessage", "");
	}
	
	private boolean validateUserRegistrationInput(HttpSession session, HttpServletRequest request) {
		boolean result = true;
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String username = request.getParameter("username");
		String mail = request.getParameter("mail");
		String password = request.getParameter("password");
		String rePassword = request.getParameter("rePassword");
		
		//name validation
		if(name == null || "".equals(name)) {
			session.setAttribute("nameMessage", "Polje ne smije ostati prazno");
			result = false;
		}
		//surname
		if(surname == null || "".equals(surname)) {
			session.setAttribute("surnameMessage", "Polje ne smije ostati prazno");
			result = false;
		}
		//username
		if(username == null || "".equals(username)) {
			session.setAttribute("usernameMessage", "Polje ne smije ostati prazno");
			result = false;
		}else if(userDAO.usernameExists(username)) {
			session.setAttribute("usernameMessage", "Korisničko ime je zauzeto");
			result = false;
		}
		//mail
		if(mail == null || "".equals(mail)) {
			session.setAttribute("mailMessage", "Polje ne smije ostati prazno");
			result = false;
		}else if (userDAO.mailExists(mail)) {
			session.setAttribute("mailMessage", "Mail je zauzet");
			result = false;
		}
		//password
		if(password == null || "".equals(password)) {
			session.setAttribute("passwordMessage", "Polje ne smije ostati prazno");
			result = false;
		}else if(!password.equals(rePassword)) {
			session.setAttribute("rePasswordMessage", "Lozinke moraju biti iste");
			result = false;
		}
	
		
		return result;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
