package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

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

import beans.UserBean;
import dao.MaterialDAO;
import dto.Material;
import dto.User;

/**
 * Servlet implementation class MaterialsController
 */
@WebServlet("/Materials")
@MultipartConfig
public class MaterialsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MaterialDAO materialDAO = new MaterialDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MaterialsController() {
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
			address = "/WEB-INF/pages/materials.jsp";
			dispatcher = request.getRequestDispatcher(address);
			dispatcher.forward(request, response);
		}else if("newFile".equals(action)) {
			String desc = request.getParameter("description");
			if(desc == null) desc = ""; //opis je opcion pa ga necemo validirati
			String fileName = saveFile(session, request);
			if(fileName != null) {
				Material material = new Material();
				material.setDescription(desc);
				material.setPath(fileName);
				material.setTimestamp(new Date());
				material.setUserId(userBean.getLoggedInUser().getId());
				if(materialDAO.insert(material)) {
					writer.write("success");
				}else {
					writer.write("Dodavanje materijala nije uspjelo");
				}
			}else {
				writer.write("Polje za dodavanje materijala ne smije ostati prazno");
			}
		}
	}
	
	private String saveFile(HttpSession session, HttpServletRequest request) {
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
				if("".equals(fileName)) return null;
				File file = new File(fileUploadPath + File.separator + fileName);
				file.createNewFile();
				try (InputStream inputStream = filePart.getInputStream()){
					Files.copy(inputStream, file.toPath(),StandardCopyOption.REPLACE_EXISTING);
				}catch (Exception e) {
					result = null;
					return result;
				}
				result = "files" + File.separator + fileName;
				return result;
			}catch (Exception e) {
				result = null;
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
				if("FILE_UPLOAD_PATH".equals(parts[0])) return parts[1];
			}
		}catch (Exception e) {
			return null;
		}
		return null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
