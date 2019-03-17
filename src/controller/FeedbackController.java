package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;
import dao.FeedbackDAO;

/**
 * Servlet implementation class FeedbackController
 */
@WebServlet("/Feedback")
public class FeedbackController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private FeedbackDAO feedbackDAO = new FeedbackDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FeedbackController() {
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
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		

		UserBean userBean = (UserBean) session.getAttribute("userBean");
		
		if (userBean == null || userBean.isLoggedIn() == false) {
			writer.write("Not authorized");
		}else if("like".equals(action)) {
			try {
				int postId = Integer.parseInt(request.getParameter("postId"));
				if(feedbackDAO.like(userBean.getLoggedInUser().getId(), postId)) {
					writer.write("success");
				}else writer.write("error");
			}catch (Exception e) {
				writer.write("error");
			}
		}else if("dislike".equals(action)) {
			try {
				int postId = Integer.parseInt(request.getParameter("postId"));
				if(feedbackDAO.dislike(userBean.getLoggedInUser().getId(), postId)) {
					writer.write("success");
				}else writer.write("error");
			}catch (Exception e) {
				writer.write("error");
			}
		}else if("remove".equals(action)) {
			try {
				int postId = Integer.parseInt(request.getParameter("postId"));
				if(feedbackDAO.removeFeedback(userBean.getLoggedInUser().getId(), postId)) {
					writer.write("success");
				}else writer.write("error");
			}catch (Exception e) {
				writer.write("error");
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
