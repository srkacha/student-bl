package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
import dao.UserDAO;
import dto.User;

/**
 * Servlet implementation class UserController
 */
@WebServlet("/User")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UserDAO userDAO = new UserDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		

		UserBean userBean = (UserBean) session.getAttribute("userBean");
		
		if (userBean == null || userBean.isLoggedIn() == false) {
			writer.write("Not authorized");
		}else if("getUser".equals(action)) {
			String userId = request.getParameter("userId");
			int id = Integer.parseInt(userId);
			User result = userDAO.selectForId(id);
			String resultJson = new Gson().toJson(result, new TypeToken<User>(){}.getType()).toString();
			writer.write(resultJson);
		}else if("getUsers".equals(action)) {
			try {
				Integer facultyId = Integer.parseInt(request.getParameter("facultyId"));
				List<User> users = userDAO.selectOtherUsersFormFaculty(userBean.getLoggedInUser().getId(), facultyId);
				String resultJson = new Gson().toJson(users, new TypeToken<List<User>>() {}.getType()).toString();
				writer.write(resultJson);
			}catch (Exception e) {
				List<User> users = userDAO.selectAllOtherUsers(userBean.getLoggedInUser().getId());
				String resultJson = new Gson().toJson(users, new TypeToken<List<User>>() {}.getType()).toString();
				writer.write(resultJson);
			}
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
