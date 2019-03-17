package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.ContentBean;
import beans.UserBean;
import dao.BlogDAO;
import dao.LoggingInfoDAO;
import dto.Blog;
import dto.Comment;
import dto.User;

/**
 * Servlet implementation class BlogsController
 */
@WebServlet("/Blogs")
public class BlogsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private BlogDAO blogDAO = new BlogDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlogsController() {
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
		RequestDispatcher dispatcher = null;

		UserBean userBean = (UserBean) session.getAttribute("userBean");
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();

		if (userBean == null || userBean.isLoggedIn() == false) {
			address = "/Login";
			dispatcher = request.getRequestDispatcher(address);
			dispatcher.forward(request, response);
		}else if("all".equals(action)) {
			address = "/WEB-INF/pages/blogs.jsp";
			dispatcher = request.getRequestDispatcher(address);
			dispatcher.forward(request, response);
		}else if("newBlog".equals(action)) {
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			if(validateInput(session, title, content)) {
				Blog blog = new Blog();
				blog.setTitle(title);
				blog.setContent(content);
				blog.setUserId(userBean.getLoggedInUser().getId());
				blog.setTimestamp(new Date());
				if(blogDAO.insert(blog)) {
					writer.write("success");
				}else {
					writer.write("Problem sa kreiranjem novog bloga");
				}
			}else {
				String message = (String)session.getAttribute("blogMessage");
				writer.write(message);
			}
		}else if("view".equals(action)) {
			String blogId = request.getParameter("blogId");
			if(blogId != null && !"".equals(blogId)) {
				Blog blog = blogDAO.getById(blogId);
				if(blog != null) {
					address = "WEB-INF/pages/blog.jsp";
					ContentBean contentBean = (ContentBean)session.getAttribute("contentBean");
					contentBean.setBlogToShow(blog);
					dispatcher = request.getRequestDispatcher(address);
					dispatcher.forward(request, response);
				}else {
					address = "WEB-INF/pages/error.jsp";
					session.setAttribute("globalErrorMessage", "Blog kojem pokusavate da pristupite ne postoji.");
					dispatcher = request.getRequestDispatcher(address);
					dispatcher.forward(request, response);
				}
			}else {
				address = "WEB-INF/pages/error.jsp";
				session.setAttribute("globalErrorMessage", "Blog kojem pokusavate da pristupite ne postoji.");
				dispatcher = request.getRequestDispatcher(address);
				dispatcher.forward(request, response);
			}
		}else if("newComment".equals(action)) {
			String commentContent = request.getParameter("content");
			String blogId = request.getParameter("blogId");
			if(commentContent != null && !"".equals(commentContent)) {
				Comment comment = new Comment();
				comment.setContent(commentContent);
				comment.setTimestamp(new Date());
				comment.setUserId(userBean.getLoggedInUser().getId());
				if(blogDAO.addComment(blogId, comment)) {
					writer.write("success");
				}
			}else {
				writer.write("Polje ne smije ostati prazno");
			}
		}
		
		//refreshing session if possible
		refershLoginSession(session);
	}
	
	private boolean validateInput(HttpSession session ,String title, String content) {
		boolean result = true;
		
		if(title == null || "".equals(title)) {
			result = false;
			session.setAttribute("blogMessage", "Polja ne smiju ostati prazna");
		}
		if(content == null || "".equals(content)) {
			result = false;
			session.setAttribute("blogMessage", "Polja ne smiju ostati prazna");
		}
		
		return result;
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
