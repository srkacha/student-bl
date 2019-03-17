<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String loginMessage = (String)session.getAttribute("loginMessage");
	if(loginMessage == null) loginMessage = "";
	String nameMessage = (String)session.getAttribute("nameMessage");
	if(nameMessage == null) nameMessage = "";
	String surnameMessage = (String)session.getAttribute("surnameMessage");
	if(surnameMessage == null) surnameMessage = "";
	String mailMessage = (String)session.getAttribute("mailMessage");
	if(mailMessage == null) mailMessage = "";
	String usernameMessage = (String)session.getAttribute("usernameMessage");
	if(usernameMessage == null) usernameMessage = "";
	String passwordMessage = (String)session.getAttribute("passwordMessage");
	if(passwordMessage == null) passwordMessage = "";
	String rePasswordMessage = (String)session.getAttribute("rePasswordMessage");
	if(rePasswordMessage == null) rePasswordMessage = "";
%>
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
        <!-- header -->
        <div class="bg-dark text-light container-fluid">
            <div class="navbar navbar-expand-lg bg-dark navbar-dark">
            	<a class="navbar-brand" href="#"><img height="75" width="75" src="https://image.flaticon.com/icons/svg/214/214349.svg"/></a>
                <div class="navbar-text text-light"><h1>StudentBL</h1></div>
                <form class="form-inline navbar-form ml-auto" action="Login" method="POST">
                    <div class="form-gorup">
                        <input type="text" class="form-control" placeholder="Korisničko ime" name="username"/>
                        <input type="password" class="form-control mr-1" placeholder="Lozinka" name="password"/>
                        <input type="hidden" name="action" value="login"/>
                        <button type="submit" class="btn btn-light text-light" style="background-color:#cf6766">Prijavi se</button>
                        <div class="text-danger"><%= loginMessage %></div>
                    </div>
                    
                </form>
            </div>
        </div>
        <!-- page content -->
        <div class="container-fluid home-bck">
            <div class="row">
                <div class="col-lg-7">
                    <div class=" welcome mt-4 text-center py-5 rounded">
                        <img src="https://image.flaticon.com/icons/svg/214/214349.svg" style="width:30%">
                        <h1>Dobrodošli na StudentBL</h1>
                        <p>Najposjećeniji studentski forum za studente Banjalučkog univerziteta. Povežite se sa prijateljima, podijelite svoje mišljenje, svoje ideje, svoje materijale.</p>
                    </div>
                </div>
                <div class="col-lg-4 p-3 my-4 ml-2 rounded bg-secondary text-light" style="opacity:0.9">
                    <h3>Kreiraj nalog</h3>
                    <br>
                    <form style="max-width: 600px" method="POST" action="Login">
                        <div class="form-group">
                            <label>Ime: <span id="nameMessage" style="color:#cf6766"><%=nameMessage%></span></label>
                            <input id="name" type="text" class="form-control" name="name" onfocusout="nameEntered()">
                        </div>
                        <div class="form-group">
                            <label>Prezime: <span id="surnameMessage" style="color:#cf6766"><%=surnameMessage%></span></label>
                            <input id="surname" type="text" class="form-control" name="surname" onfocusout="surnameEntered()">
                        </div>
                        <div class="form-group">
                            <label>Korisničko ime: <span id="usernameMessage" style="color:#cf6766"><%=usernameMessage%></span></label>
                            <input id="username" type="text" class="form-control" name="username" onfocusout="usernameEntered()">
                        </div>
                        <div class="form-group">
                            <label>Mail: <span id="mailMessage" style="color:#cf6766"><%=mailMessage%></span></label>
                            <input id="mail" type="text" class="form-control" name="mail" onfocusout="mailEntered()">
                        </div>
                        <div class="form-group">
                            <label>Lozinka: <span id="passwordMessage" style="color:#cf6766"><%=passwordMessage%></span></label>
                            <input id="password" type="password" class="form-control" name="password" onfocusout="passwordEntered()">
                        </div>
                        <div class="form-group">
                            <label>Ista lozinka: <span id="rePasswordMessage" style="color:#cf6766"><%=rePasswordMessage%></span></label>
                            <input id="rePassword" type="password" class="form-control" name="rePassword" onfocusout="rePasswordEntered()">
                        </div>
                        <input type="hidden" name="action" value="register"/>
                        <button type="submit" class="btn btn-light text-light" style="background-color:#cf6766">Registruj se</button>
                    </form>
                </div>
            </div>
        </div>
        
        <!-- bootstrap se ucitava na kraju da ne bi ugrozavao performanse -->
        <script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    </body>
</html>