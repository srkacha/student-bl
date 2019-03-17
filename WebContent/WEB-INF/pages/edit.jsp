<%@page import="beans.FacultyBean"%>
<%@page import="java.util.List"%>
<%@page import="dto.Faculty"%>
<%@page import="dto.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<title>StudentBL</title>
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<link rel="stylesheet" href="style/app.css">
<script type="text/javascript" src="js/login.js"></script>
</head>
<body>
	<%@include file="include/header.jsp"%>
	<%
		User userToShow = ((UserBean) session.getAttribute("userBean")).getLoggedInUser();
		List<Faculty> faculties = ((FacultyBean) session.getAttribute("facultyBean")).getFaculties();
		String imageURL = userToShow.getPicture();
		String mail = userToShow.getMail();
		Faculty fac = userToShow.getFaculty();
		String faculty = userToShow.getFaculty().getName();
		String desc = userToShow.getDescription();
		String username = userToShow.getUsername();
		int year = userToShow.getYearOfStudy();
		String name = userToShow.getName();
		String surname = userToShow.getSurname();
	%>
	<div class="container-fluid">
		<div class="row justify-content-center">
			<div class="col-md-5 bg-light rounded p-4 m-4 text-center text-dark">

				<img class="rounded-circle"
					src=<%=imageURL != null && !imageURL.equals("") ? imageURL : "img/user.svg"%>
					style="width: 60%"><br>
				<h3>@<%=username%></h3>

				<!-- modal za izmjenu lozinke -->
				<button type="button" class="btn btn-secondary" data-toggle="modal"
					data-target="#myModal">Izmijeni lozinku</button>

				<!-- iznadj je dugme, a ovo je modal -->
				<div class="modal" id="myModal">
					<div class="modal-dialog">
						<div class="modal-content">

							<!-- Modal Header -->
							<div class="modal-header">
								<h4 class="modal-title">Izmjena lozinke</h4>
								<button type="button" class="close" data-dismiss="modal">&times;</button>
							</div>

							<!-- Modal body -->
							<div class="modal-body text-left">
								<form id="editPasswordForm" action="PasswordEdit" method="get">
									<div class="form-gorup">
										<label>Stara lozinka:</label>
										<input type="password" class="form-control" name="old">
									</div>
									<div class="form-gorup">
										<label>Nova lozinka:</label>
										<input type="password" class="form-control" name="new">
									</div>
									<div class="form-gorup">
										<label>Potvrda nove lozinke:</label>
										<input type="password" class="form-control" name="reNew">
									</div>
									<input type="hidden" name="action" value="editPassword">
									<br>
									<label id="passwordMessage" class="text-danger"></label>
									<br>
									
								</form>
							</div>

							<!-- Modal footer -->
							<div class="modal-footer">
								<button id="savePasswordChange" type="button" class="btn btn-secondary">Sačuvaj izmjene</button>
								<button type="button" class="btn btn-danger"
									data-dismiss="modal">Zatvori</button>
							</div>

						</div>
					</div>
				</div>

			</div>

			<div class="col-md-5 p-4 m-4 rounded bg-light text-dark">
				<form action="Account" method="POST" enctype="multipart/form-data">
					<div class="form-group">
						<label>Ime: <span id="nameMessage" style="color: #cf6766"><%=session.getAttribute("nameMessage")%></span></label>
						<input id="name" type="text" class="form-control" name="name"
							onfocusout="nameEntered()" value=<%=name%>>
					</div>
					<div class="form-group">
						<label>Prezime: <span id="surnameMessage"
							style="color: #cf6766"><%=session.getAttribute("surnameMessage")%></span></label>
						<input id="surname" type="text" class="form-control"
							name="surname" onfocusout="surnameEntered()" value=<%=surname%>>
					</div>
					<div class="form-group">
						<label>Mail: <span id="mailMessage" style="color: #cf6766"><%=session.getAttribute("mailMessage")%></span></label>
						<input id="mail" type="text" class="form-control" name="mail"
							onfocusout="mailEntered()" value=<%=mail%>>
					</div>
					<div class="form-group">
						<label>Opis:</label>
						<textarea class="form-control" name="description" id="description"
							rows="5"><%=desc != null ? desc : ""%></textarea>
					</div>
					<div class="form-group">
						<label>Fakultet:</label> 
						<select class="custom-select" name="faculty">
							<option <%=fac == null?"selected":"" %> value="null">Fakultet</option>
							<%
								for (Faculty f : faculties) {
							%>
							<option <%=fac != null && fac.getId() == f.getId() ? "selected" : ""%>
								value=<%=f.getId()%>><%=f.getName()%></option>
							<%
								}
							%>
						</select>
					</div>
					<div class="form-group">
						<label>Godina studija: <span id="yearMessage" style="color: #cf6766"><%=session.getAttribute("yearMessage")%></span></label> <input id="year" type="text"
							class="form-control" name="year" value=<%=year != 0 ? year : ""%>>
					</div>
					<div class="form-group">
						<label>Slika:</label>
						<div class="custom-file">
							<input accept="image/*" type="file" name="file" class="custom-file-input" id="customFile">
							<label class="custom-file-label" for="customFile">
								Izaberi sliku</label>
						</div>
					</div>
					<input type="hidden" name="action" value="saveEdit">
					<button type="submit" class="btn btn-secondary">Sačuvaj
						izmjene</button>
				</form>
			</div>
		</div>
	</div>

	<!-- bootstrap se ucitava na kraju da ne bi ugrozavao performanse -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
		integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
		integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
		crossorigin="anonymous"></script>
		
	<!-- js fajlovi koji ukljucuju rad sa jQuerijem -->
	<script src="js/edit.js" type="text/javascript"></script>
</body>
</html>