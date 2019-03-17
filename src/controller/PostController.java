package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.ContentBean;
import beans.UserBean;
import dao.UserPostDAO;
import dto.Post;
import dto.UserPost;

/**
 * Servlet implementation class PostController
 */
@WebServlet("/Post")
public class PostController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UserPostDAO userPostDAO = new UserPostDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostController() {
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
		}else if("new".equals(action)) {
			UserPost newPost = new UserPost();
			String content = request.getParameter("content");
			String link = request.getParameter("link");
			if(validateInput(content, link)) {
				//kreiranje posta
				newPost.setContent(content);
				newPost.setUserId(userBean.getLoggedInUser().getId());
				if(!"".equals(link)) {
					if(link.contains("youtube")) {
						String videoId = link.split("=")[1];
						String embedLink = "https://www.youtube.com/embed/" + videoId;
						newPost.setLink(embedLink);
						newPost.setYoutubeLink(true);
					}else {
						newPost.setLink(link);
						newPost.setYoutubeLink(false);
					}
				}else newPost.setLink("");
				if(userPostDAO.insert(newPost)) {
					writer.write("success");
				}else writer.write("error");
			}else {
				writer.write("Morate popuniti bar jedno od polja");
			}
			
		}
	}
	
	private boolean validateInput(String content, String link) {
		boolean result = true;
		if((content == null || "".equals(content)) && (link == null || "".equals(link))) {
			result = false;
		}
		
		return result;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
