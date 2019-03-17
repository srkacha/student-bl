package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import beans.UserBean;
import dao.ConnectionDAO;
import dao.LoggingInfoDAO;
import dao.UserDAO;
import dto.User;

/**
 * Servlet implementation class ConnectionsController
 */
@WebServlet("/Connections")
public class ConnectionsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ConnectionDAO connectionDAO = new ConnectionDAO();
	private UserDAO userDAO = new UserDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConnectionsController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String address = "/WEB-INF/pages/connections.jsp";
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		RequestDispatcher requestDispatcher= null;
		
		
		response.setContentType("text/plain;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();

		UserBean userBean = (UserBean) session.getAttribute("userBean");

		if (userBean == null || userBean.isLoggedIn() == false) {
			address = "/Login";
			requestDispatcher = request.getRequestDispatcher(address);
			requestDispatcher.forward(request, response);
		}else if("accept".equals(action)) {
			if(connectionDAO.acceptRequest(userBean.getLoggedInUser().getId(), Integer.parseInt(request.getParameter("userId")))) {
				writer.write("success");
			}else writer.write("error");
		}else if("decline".equals(action)) {
			if(connectionDAO.removeConnection(userBean.getLoggedInUser().getId(), Integer.parseInt(request.getParameter("userId")))) {
				writer.write("success");
			}else writer.write("error");
		}else if("remove".equals(action)) {
			if(connectionDAO.removeConnection(userBean.getLoggedInUser().getId(), Integer.parseInt(request.getParameter("userId")))) {
				writer.write("success");
			}else writer.write("error");
		}else if("add".equals(action)) {
			if(connectionDAO.sendRequest(userBean.getLoggedInUser().getId(), Integer.parseInt(request.getParameter("userId")))) {
				writer.write("success");
			}else writer.write("error");
		}else if("cancel".equals(action)) {
			if(connectionDAO.removeConnection(userBean.getLoggedInUser().getId(), Integer.parseInt(request.getParameter("userId")))) {
				writer.write("success");
			}else writer.write("error");
		}else {
			requestDispatcher = request.getRequestDispatcher(address);
			requestDispatcher.forward(request, response);

		}
		
		//refreshing session if possible
		refershLoginSession(session);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void refershLoginSession(HttpSession session) {
		UserBean userBean = (UserBean)session.getAttribute("userBean");
		if(userBean != null) {
			User loggedIn = userBean.getLoggedInUser();
			LoggingInfoDAO loggingInfoDAO = new LoggingInfoDAO();
			loggingInfoDAO.refreshLogout(loggedIn.getId());
		}
	}

}
