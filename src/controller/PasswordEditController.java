package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;
import dao.UserDAO;
import util.SecureUtil;

/**
 * Servlet implementation class PasswordEditController
 */
@WebServlet("/PasswordEdit")
public class PasswordEditController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UserDAO userDAO = new UserDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PasswordEditController() {
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

		UserBean userBean = (UserBean) session.getAttribute("userBean");
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();

		if (userBean == null || userBean.isLoggedIn() == false) {
			writer.print("Neautorizovan prizstup");
		}else if("editPassword".equals(action)) {
			//validacija
			if(validateInput(session, request)) {
				//moze se izmijeniti lozinka
				if(userDAO.updatePassword(userBean.getLoggedInUser().getId(), SecureUtil.generateSHA256Hash(request.getParameter("new")))) {
					//uspjesno update
					writer.print("success");
				}else {
					writer.print("error");
				}
			}else writer.write((String)session.getAttribute("passwordChangeMessage"));
		}
	}
	
	private boolean validateInput(HttpSession session, HttpServletRequest request) {
		boolean result = true;
		
		UserBean userBean = (UserBean)session.getAttribute("userBean");
		
		String old = request.getParameter("old");
		String newPassword = request.getParameter("new");
		String reNewPassword = request.getParameter("reNew");
		if(old == null || "".equals(old)) {
			session.setAttribute("passwordChangeMessage", "Polja ne smiju ostati prazna");
			result = false;
		}
		else if(newPassword == null || "".equals(newPassword)) {
			session.setAttribute("passwordChangeMessage", "Polja ne smiju ostati prazna");
			result = false;
		}
		else if(reNewPassword == null || "".equals(reNewPassword)) {
			session.setAttribute("passwordChangeMessage", "Polja ne smiju ostati prazna");
			result = false;
		}
		else if(!userBean.getLoggedInUser().getPasswordHash().equals(SecureUtil.generateSHA256Hash(old))) {
			session.setAttribute("passwordChangeMessage", "Stara lozinka nije ispravna");
			result = false;
		}
		else if(!newPassword.equals(reNewPassword)) {
			session.setAttribute("passwordChangeMessage", "Nove lozinke se moraju podudarati");
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
