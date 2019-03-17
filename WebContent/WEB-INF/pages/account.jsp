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
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="style/app.css">
    <script type="text/javascript" src="js/login.js"></script>
</head>
    <body>
        <%@include file="include/header.jsp" %>
        
        <div class="container-fluid">
            <div class="row justify-content-center">
                <div class="col-md-5 bg-light rounded p-4 m-4 text-center text-dark">
                	<%
                		User userToShow = ((UserBean)session.getAttribute("userBean")).getUserToShow();
                		String imageURL = userToShow.getPicture();
                	%>
                    <img class="rounded-circle" src=<%= imageURL!=null && !imageURL.equals("")?imageURL:"img/user.svg" %> style="width: 60%"><br>
                    <h3>@<%= ((UserBean)session.getAttribute("userBean")).getUserToShow().getUsername() %></h3>
                </div>
                <div class="col-md-5 p-4 m-4 rounded bg-light text-dark">
                	<h2><%= ((UserBean)session.getAttribute("userBean")).getUserToShow().getName() %> <%= ((UserBean)session.getAttribute("userBean")).getUserToShow().getSurname() %> </h2>
                	<hr>
	               	<%
		               	String mail = userToShow.getMail();
		           		String faculty = userToShow.getFaculty().getName();
		           		String desc = userToShow.getDescription();
		           		int year = userToShow.getYearOfStudy();
                		if(userToShow.getConnectionType() == 2){
                	%>
                	<p>Email adresa: <%=mail  %></p>
                	<p>Fakultet: <%= faculty!=null?faculty:"N/A" %></p>
                	<p>Godina studija: <%= year!=0?year:"N/A" %></p>
                	<p>Opis:</p>
                	<textarea class="rounded border-0" rows="7" readonly="readonly" style="width: 100%"
                		wrap="soft"><%=desc!=null?desc:"" %></textarea>
                	<%
                		}else{
                	%>
                		<div class="jumbotron text-center bg-secondary text-white">
                			<h1>Niste povezani!</h1>
                			<p>Uspostavite konekciju i saznajete više o korisniku</p>
                			<button class="btn btn-primary">Pošalji zahtjev</button>
                		</div>
                	<%
                		}
                	%>
                </div>
            </div>
        </div>
        
        <!-- bootstrap se ucitava na kraju da ne bi ugrozavao performanse -->
        <script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

        <!-- js fajlovi koji ukljucuju rad sa jQuerijem -->
        
    </body>
</html>