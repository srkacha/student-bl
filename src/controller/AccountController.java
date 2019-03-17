package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;
import org.apache.tomcat.util.http.fileupload.util.Streams;

import com.sun.org.apache.bcel.internal.generic.ReturnaddressType;
import com.sun.xml.internal.xsom.impl.Ref.Term;

import beans.ConnectionBean;
import beans.FacultyBean;
import beans.UserBean;
import dao.ConnectionDAO;
import dao.FacultyDAO;
import dao.LoggingInfoDAO;
import dao.UserDAO;
import dto.User;

/**
 * Servlet implementation class AccountController
 */
@MultipartConfig
@WebServlet("/Account")
public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAO();
	private FacultyDAO facultyDAO = new FacultyDAO();
	private ConnectionDAO connectionDAO = new ConnectionDAO();
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String address = "/WEB-INF/pages/home.jsp";
		String action = request.getParameter("action");
		String userID = request.getParameter("userId");
		HttpSession session = request.getSession();

		clearMessages(session);

		UserBean userBean = (UserBean) session.getAttribute("userBean");

		if (userBean == null || userBean.isLoggedIn() == false) {
			address = "/Login";
		} else if ("me".equals(action)) {
			// ucitaj nalog ulogovanog korisnika
			address = "WEB-INF/pages/account.jsp";
			userBean.setUserToShow(userBean.getLoggedInUser());
		} else if ("view".equals(action)) {
			try {
				int id = Integer.parseInt(userID);
				User selectedUser = userDAO.selectForId(id);
				if (selectedUser != null) {
					selectedUser.setConnectionType(connectionDAO.getConnectionType(id, userBean.getLoggedInUser().getId()));
					if(selectedUser.getId() == userBean.getLoggedInUser().getId()) selectedUser.setConnectionType(2);
					address = "WEB-INF/pages/account.jsp";
					userBean.setUserToShow(selectedUser);
				} else {
					session.setAttribute("globalErrorMessage",
							"Došlo je do greške kod prikaza korisnika, korisnik ili nije izabran ili ne postoji.");
					address = "WEB-INF/pages/error.jsp";
				}
			} catch (Exception e) {
				session.setAttribute("globalErrorMessage",
						"Došlo je do greške kod prikaza korisnika, korisnik ili nije izabran ili ne postoji.");
				address = "WEB-INF/pages/error.jsp";
			}
		} else if ("edit".equals(action)) {
			address = "WEB-INF/pages/edit.jsp";
			// ponovo ga dovukli posto cemo ovaj objekat mijenjati pa ne bi bilo oke da
			// mijenjamo objekat ulogovanog korisnika
			userBean.setUserToShow(userDAO.selectForId(userBean.getLoggedInUser().getId()));
		} else if ("saveEdit".equals(action) || ServletFileUpload.isMultipartContent(request)) {
			// validacija podataka za edit, znaci da se radi o file upload requestu sto nam
			// je bitno
			User generatedUser = generateUserFromRequest(session, request);
			if (validateInput(session, request, generatedUser)) {
				// validno je sve pa se moze odraditi update u bazi
				//zbog generisanja imena moramo ucitati ime u generated usera
				generatedUser.setUsername(userBean.getLoggedInUser().getUsername());
				String fileName = savePictureFile(session, request, generatedUser);
				if(fileName != null) {
					//moze upis u bazu
					generatedUser.setPicture(fileName);
					
					//ako korisnik nije izmjenio sliku ostace stara, za sad nema mogucnost da otkloni sliku
					if("".equals(generatedUser.getPicture())) generatedUser.setPicture(userBean.getLoggedInUser().getPicture());
					generatedUser.setId(userBean.getLoggedInUser().getId());
					
					//upis u bazu
					if(userDAO.update(generatedUser)) {
						//korisnik uspjesno azuriran
						userBean.setLoggedInUser(userDAO.selectForId(generatedUser.getId()));
						//update user to show zbog prikaza
						userBean.setUserToShow(userBean.getLoggedInUser());
						address = "WEB-INF/pages/home.jsp";
						//update logged in usera u connection beanu
						ConnectionBean connectionBean = (ConnectionBean)session.getAttribute("connectionBean");
						if(connectionBean!= null) connectionBean.setLoggedInUser(userDAO.selectForId(generatedUser.getId()));
					}else {
						session.setAttribute("globalErrorMessage",
								"Došlo je do greške kod pri ažuriranju naloga, molimo vas da pokušate ponovno kasnije.");
						address = "WEB-INF/pages/error.jsp";
					}
				}else {
					session.setAttribute("globalErrorMessage",
							"Došlo je do greške kod pri ažuriranju naloga, molimo vas da pokušate ponovno kasnije.");
					address = "WEB-INF/pages/error.jsp";
				}
			} else {
				address = "WEB-INF/pages/edit.jsp";
			}
		} else {
			session.setAttribute("globalErrorMessage",
					"Došlo je do greške kod prikaza korisnika, korisnik ili nije izabran ili ne postoji.");
			address = "WEB-INF/pages/error.jsp";
		}
		
		//refreshing session if possible
		refershLoginSession(session);

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
	

	private String savePictureFile(HttpSession session, HttpServletRequest request, User user) {
		String result = null;

		String fileUploadPath = getSavePath();
		if(fileUploadPath == null) {
			result = null;
		}else {
			//upload fajla
			File uploadDir = new File(fileUploadPath);
			if(!uploadDir.exists()) {
				uploadDir.mkdirs();
			}
			try {
				Part filePart = request.getPart("file");
				String fileName =Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
				String extension = fileName.split("\\.")[1];
				String realFileName = user.getUsername() + "-" + System.currentTimeMillis() + "." + extension;
				File file = new File(uploadDir + File.separator + realFileName);
				file.createNewFile();
				try (InputStream inputStream = filePart.getInputStream()){
					Files.copy(inputStream, file.toPath(),StandardCopyOption.REPLACE_EXISTING);
				}catch (Exception e) {
					result = "";
					return result;
				}
				result = "img" + File.separator + "users" + File.separator + realFileName;
				return result;
			}catch (Exception e) {
				result = "";
			}
		}
		return result;
	}
	
	private String getSavePath() {
		try {
			ServletContext servletContext = getServletContext();
			InputStream inputStream = servletContext.getResourceAsStream("WEB-INF/properties/upload.properties");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				String parts[] = line.split("=");
				if("IMAGE_UPLOAD_PATH".equals(parts[0])) return parts[1];
			}
		}catch (Exception e) {
			return null;
		}
		return null;
	}

	private void clearMessages(HttpSession session) {
		session.setAttribute("yearMessage", "");
		session.setAttribute("nameMessage", "");
		session.setAttribute("surnameMessage", "");
		session.setAttribute("mailMessage", "");
	}

	private User generateUserFromRequest(HttpSession session, HttpServletRequest request) {
		User result = new User();
		result.setName(request.getParameter("name"));
		result.setSurname(request.getParameter("surname"));
		result.setMail(request.getParameter("mail"));
		result.setDescription(request.getParameter("description"));
		try {
			int facultyId = Integer.parseInt(request.getParameter("faculty"));
			result.setFacultyId(facultyId);
			result.setFaculty(facultyDAO.getFacultyForId(facultyId));
		}catch(Exception e) {
			result.setFacultyId(0);
			result.setFaculty(null);
		}
		try {
			int year = Integer.parseInt(request.getParameter("year"));
			result.setYearOfStudy(year);
		}catch (Exception e) {}
		
		return result;
	}

	private boolean validateInput(HttpSession session, HttpServletRequest request, User user) {
		boolean result = true;
		
		User loggedIn = ((UserBean)session.getAttribute("userBean")).getLoggedInUser();
		if(loggedIn==null) return false;

		// validacija
		if (user.getName() == null || "".equals(user.getName())) {
			session.setAttribute("nameMessage", "Polje ne smije ostati prazno");
			result = false;
		}
		if (user.getSurname() == null || "".equals(user.getSurname())) {
			session.setAttribute("surnameMessage", "Polje ne smije ostati prazno");
			result = false;
		}
		if (user.getMail() == null || "".equals(user.getMail())) {
			session.setAttribute("mailMessage", "Polje ne smije ostati prazno");
			result = false;
		}else if(!(user.getMail().equals(loggedIn.getMail())) && userDAO.mailExists(user.getMail())) {
			session.setAttribute("mailMessage", "Mail je zauzet");
			return false;
		}
		if (user.getYearOfStudy() < 0) {
			session.setAttribute("yearMessage", "Morate unijeti pozitivan cijeli broj ili ostavite prazno");
			result = false;
		}

		return result;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
