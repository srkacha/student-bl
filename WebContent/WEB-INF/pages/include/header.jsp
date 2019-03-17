<!-- header -->
<%@page import="beans.ConnectionBean"%>
<%@page import="beans.UserBean"%>

<%
	ConnectionBean connectionBean = (ConnectionBean)session.getAttribute("connectionBean");
	int reqCount = connectionBean.getNumberOfRequestsForUser();
	String picture = connectionBean.getLoggedInUser().getPicture();
	String firstName = connectionBean.getLoggedInUser().getName();
	String lastName = connectionBean.getLoggedInUser().getSurname();
%>
<nav class="navbar sticky-top navbar-expand-lg bg-dark navbar-dark">
	<!-- Brand -->
	<a class="navbar-brand" href="Home"><img height="75" width="75" src="https://image.flaticon.com/icons/svg/214/214349.svg"/></a>
    <a href="Home" class="navbar-text text-light btn"><h1>StudentBL</h1></a>

	<!-- Toggler/collapsibe Button -->
	<!-- Default dropleft button -->
	
	<div class="dropdown ml-auto">
		<button class="btn btn-secondary dropdown-toggle" type="button"
			id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true"
			aria-expanded="false">
			<img height="35" width="35" class="mr-1 rounded-circle" alt="" src=<%=picture==null || picture.equals("")?"img/user.svg":picture%>>
			<span class="pl-1"><%=firstName + " " + lastName%></span>
		</button>
		<div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
			<a class="dropdown-item" href="Account?action=me"><i class="fas fa-user"></i>&nbsp; Moj nalog</a> 
			<a class="dropdown-item" href="Account?action=edit"><i class="fas fa-user-edit"></i>&nbsp; Izmijeni nalog</a>
			<a class="dropdown-item" href="Connections"><i class="fas fa-users"></i>&nbsp; Konekcije <span id="dropdownRequestCount" class="badge badge-danger"><%=reqCount!=0?reqCount:"" %></span></a>  
			<a class="dropdown-item" href="Login?action=logout"><i class="fas fa-sign-out-alt"></i>&nbsp; Odjavi se</a>
		</div>
	</div>
</nav>