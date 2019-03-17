package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;
import dao.LoggingInfoDAO;
import dto.User;

/**
 * Servlet implementation class HomeController
 */
@WebServlet("/Home")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String address = "/WEB-INF/pages/home.jsp";
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		
		UserBean userBean = (UserBean)session.getAttribute("userBean");
		if(userBean == null || userBean.isLoggedIn() == false) {
			address = "/Login";
		}else {
			//korisnik postoji i ulogovan je 
			//pripremi odgovarajuce beannove i ucitaj home
			refershLoginSession(session);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(address);
		dispatcher.forward(request, response);
	}
	
	private void refershLoginSession(HttpSession session) {
		UserBean userBean = (UserBean)session.getAttribute("userBean");
		if(userBean != null) {
			User loggedIn = userBean.getLoggedInUser();
			LoggingInfoDAO loggingInfoDAO = new LoggingInfoDAO();
			loggingInfoDAO.refreshLogout(loggedIn.getId());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
