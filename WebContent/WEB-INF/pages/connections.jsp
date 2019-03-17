<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="beans.FacultyBean"%>
<%@page import="dto.Faculty"%>
<%@page import="dto.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="beans.ConnectionBean"%>
<%@page import="beans.UserBean"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
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
		int counter = connectionBean.getNumberOfRequestsForUser();
		List<Faculty> faculties = ((FacultyBean) session.getAttribute("facultyBean")).getFaculties();
		Faculty fac = connectionBean.getLoggedInUser().getFaculty();
		
	%>

	<div class="container-fluid">
		<div class="row justify-content-center">
			<div class="col-lg-8 p-4 my-4 bg-light rounded">
				<ul class="nav nav-tabs">
					<li class="nav-item"><a id="users-tab" date-toggle="tab" class="nav-link active text-secondary"
						href="#users" aria-controls="users" aria-selected="true">Pretraga korisnika</a></li>
					<li class="nav-item"><a id="connections-tab" date-toggle="tab" class="nav-link text-secondary"
						href="#connections" aria-controls="connections" aria-selected="false">Konekcije</a></li>
					<li class="nav-item"><a id="requests-tab" date-toggle="tab" class="nav-link text-secondary"
						href="#requests" aria-controls="requests" aria-selected="false">Zahtjevi <span id="requestCount" class="badge badge-danger"><%=counter!=0?counter:"" %></span></a></li>
				</ul>

				<div class="tab-content" id="myTabContent">
					<div class="tab-pane fade show active p-4" id="users" role="tabpanel"
						aria-labelledby="users-tab">
						<form class="mb-2">
						<select id="faculty-select" class="custom-select ml-0" name="faculty">
							<option <%=fac == null?"selected":"" %> value="none">Fakultet</option>
							<%
								for (Faculty f : faculties) {
							%>
							<option <%=fac != null && fac.getId() == f.getId() ? "selected" : ""%>
								value=<%=f.getId()%>><%=f.getName()%></option>
							<%
								}
							%>
						</select>
						</form>
						
						<div class="other-user-list pl-4">
						<!-- izlistavanje svih ostalih korisnika -->
						<%
							List<User> usersToShow = new ArrayList<>();
							int facultyId = connectionBean.getLoggedInUser().getFacultyId();
							if(facultyId != 0){
								usersToShow = connectionBean.getOtherUsersFromFaculty(facultyId);
							}else usersToShow = connectionBean.getOtherUsers();
							
							for (User user : usersToShow) {
						%>
						<%
							int connectionType = user.getConnectionType();
						%>
						<div id="other-<%=user.getId() %>"
							class="row p-1 align-items-center w-100 justify-content-between bg-white rounded mb-2 py-2 pl-2">
							<div class="col">
								<div class="row align-items-center">
									<img class="mr-4 rounded-circle" style="max-height: 38px" src=<%=user.getPicture()!=null && user.getPicture()!=""?user.getPicture():"img/user.svg" %>>
									<a href="Account?action=view&userId=<%=user.getId() %>" class="mr-4 text-secondary" style="width: 100px"><%=user.getName() + " " + user.getSurname() %></a>
								</div>
							</div>
							<div class="col text-right">
								<button id="request-<%=user.getId() %>" class="<%=connectionType==0?"add-button btn btn-secondary":"cancel-button btn btn-danger" %>"><i class="<%= connectionType == 0?"fas fa-plus":"fas fa-times" %>"></i> <%= connectionType == 0?"Pošalji zahtjev":"Poništi zahtjev" %></button>
							</div>
						</div>
						
						<%
							}
						%>
						</div>
						
					</div>
					<div class="tab-pane fade show p-4" id="connections" role="tabpanel"
						aria-labelledby="connections-tab">
						
						<%
							for (User user : connectionBean.getConnections()) {
						%>
						<div id="connection-<%=user.getId() %>"
							class="row p-1 align-items-center w-100 justify-content-between bg-white rounded mb-2 py-2 pl-2">
							<div class="col">
								<div class="row align-items-center">
									<img class="mr-4 rounded-circle" style="max-height: 38px" src=<%=user.getPicture()!=null && user.getPicture()!=""?user.getPicture():"img/user.svg" %>>
									<a href="Account?action=view&userId=<%=user.getId() %>" class="mr-4 text-secondary" style="width: 100px"><%=user.getName() + " " + user.getSurname() %></a>
								</div>
							</div>
							<div class="col text-right">
								<button id="dec-<%=user.getId() %>" class="remove-button btn btn-danger"><i class="fas fa-times"></i> Ukloni</button>
							</div>
						</div>
						
						<%
							}
						%>
							
					</div>
					<div class="tab-pane fade p-4" id="requests" role="tabpanel"
						aria-labelledby="requests-tab">

						<%
							for (User user : connectionBean.getRequests()) {
						%>
						<div id="request-<%=user.getId() %>"
							class="row p-1 align-items-center w-100 justify-content-between bg-white rounded mb-2 py-2 pl-2">
							<div class="col">
								<div class="row align-items-center">
									<img class="mr-4 rounded-circle" style="max-height: 38px" src=<%=user.getPicture()!=null && user.getPicture()!=""?user.getPicture():"img/user.svg" %>>
									<a href="Account?action=view&userId=<%=user.getId() %>" class="mr-4 text-secondary" style="width: 100px"><%=user.getName() + " " + user.getSurname() %></a>
								</div>
							</div>
							<div class="col text-right">
								<button id="acc-<%=user.getId() %>" class="accept-button btn btn-secondary mr-2"><i class="fas fa-check"></i> Prihvati</button>
								<button id="dec-<%=user.getId() %>" class="decline-button btn btn-danger"><i class="fas fa-times"></i> Ukloni</button>
							</div>
						</div>
						
						<%
							}
						%>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!-- templejti za dinamicko dodavnje elemenata -->
	<div id="connection-id" style="display:none"
		class=" conn-template div-conn row p-1 align-items-center w-100 justify-content-between bg-white rounded mb-2 py-2 pl-2">
		<div class="col">
			<div class="row align-items-center">
				<img id="connection-img" class="img-conn mr-4 rounded-circle" style="max-height: 38px" src="img/user.svg"/>
				<a id="userLink" href="Account?action=view&userId=userId"
					class="user-conn mr-4 text-secondary" style="width: 100px"></a>
			</div>
		</div>
		<div class="col text-right">
			<button id="removeButton"
				class="remove-button btn btn-danger">
				<i class="fas fa-times"></i> Ukloni
			</button>
		</div>
	</div>
	
	<div id="other-id" style="display: none;" class="other-template row p-1 align-items-center w-100 justify-content-between bg-white rounded mb-2 py-2 pl-2">
		<div class="col">
			<div class="row align-items-center">
				<img id="other-img" class="mr-4 rounded-circle" style="max-height: 38px" src="other-img">
				<a id="other-user-link" href="Account?action=view&userId=otherId" class="mr-4 text-secondary" style="width: 100px">Name and surname</a>
			</div>
		</div>
		<div class="col text-right">
			<button id="request-userId" class="other-user-btn"><i class="other-user-icon"></i> Other user button text</button>
		</div>
	</div>

	<!-- bootstrap se ucitava na kraju da ne bi ugrozavao performanse -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
		integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
		integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
		crossorigin="anonymous"></script>

	<!-- js fajlovi koji ukljucuju rad sa jQuerijem -->
	<script type="text/javascript" src="js/connections.js"></script>
</body>
</html>