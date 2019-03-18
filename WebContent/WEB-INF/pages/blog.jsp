<%@page import="dto.Comment"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="dto.User"%>
<%@page import="dto.Blog"%>
<%@page import="beans.ContentBean"%>
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
        <%@include file="include/header.jsp"%>
        
        <%
        	ContentBean contentBean = (ContentBean)session.getAttribute("contentBean");
        	Blog blog = contentBean.getBlogToShow();
        	String blogTitle = blog.getTitle();
        	User creator = blog.getUser();
        	String nameAndSurname = creator.getName() + " " + creator.getSurname();
        	int userId = creator.getId();
        	String userPicture = creator.getPicture() == null || creator.getPicture().equals("")?"img/user.svg":creator.getPicture();
        	String blogTimestamp = new SimpleDateFormat("dd MMM yyyy HH:mm").format(blog.getTimestamp());
        	String blogContent = blog.getContent();
        %>
        
        <div class="container-fluid">
        	<div class="row justify-content-center">
        		<div class="col-md-8 bg-light p-4 my-4">
        			<div class="bg-secondary p-2 rounded text-white text-center">
        				<h1><%=blogTitle %></h1><br>
	        			<img alt="" src="<%=userPicture%>" height="30" width="30" class="rounded-circle">
	        			<a href="Account?action=view&userId=<%=userId %>" class="text-white" style="text-decoration: none"><h5 style="display: inline-block;"><%= nameAndSurname %></h5></a>
	        			<br>
	        			<span><%=blogTimestamp %></span>
        			</div>
        			
        			<br><br><br>
        			<div class="p-4 rounded">
        				<p><%=blogContent %></p>
        			</div>
        			<br>
        			<div class="form-inline px-4">
        				<h3>Komentari</h3>
        				<button id="collapseNewCommentButton" class="btn btn-secondary ml-auto" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">Novi komentar</button>
        			</div>
        			<div class="collapse mt-2 px-4" id="collapseExample">
						<form>
							<div class="form-group">
								<textarea id="newCommentContent" class="form-control" rows="3" style="width: 100%" name="content" placeholder="Sadržaj"></textarea>
							</div>
							<div class="form-group text-right">
								<label id="newCommentError" class="text-danger"></label>
								<button id="<%=blog.getId() %>" type="button" class="comment-button btn btn-secondary ml-1"><i class="fas fa-plus"></i> Objavi</button>
							</div>
						</form>
						<hr>
					</div>
        			<div id="comments-div" class="px-4 pt-2">
        				<div id="comments-div-body">
        				<%
        					for(Comment comment:blog.getComments()){
        						String commentContent = comment.getContent();
        						String commentTimestamp = new SimpleDateFormat("dd MMM yyyy HH:mm").format(comment.getTimestamp());
        						User commenter = comment.getUser();
        			        	String commenterNameAndSurname = commenter.getName() + " " + commenter.getSurname();
        			        	int commenterId = commenter.getId();
        			        	String commenterPicture = commenter.getPicture() == null || commenter.getPicture().equals("")?"img/user.svg":commenter.getPicture();
        				%>
        					<div id="comment-body" class="bg-white rounded p-1 mb-2">
								<div id="comment-header" class="bg-white text-white form-inline py-1 px-2 rounded">
									<img alt="" src="<%=commenterPicture %>" height="20" width="20" class="rounded-circle mr-1">
									<a href="Account?action=view&userId=<%=commenterId %>" class="text-dark" style="text-decoration: none;"><%=commenterNameAndSurname %></a>
									<span class="ml-auto text-dark"><%=commentTimestamp %></span>
								</div>
								<div id="comment-content-div" class="p-2">
									<p id="commentContent"><%=commentContent %><p>
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
        
        <!-- template za komentar kada ga budemo morali dodati dinamicki -->
        <div id="comment-body" class="bg-white rounded p-1 mb-2" style="display: none">
			<div id="comment-header" class="bg-white text-white form-inline py-1 px-2 rounded">
				<img alt="" src="img/user.svg" height="20" width="20" class="rounded-circle mr-1">
				<a href="#" class="text-dark" style="text-decoration: none">Srdjan Jovic</a>
				<span class="ml-auto text-dark">10 Feb 2019 13:23</span>
			</div>
			<div id="comment-content-div" class="p-2">
				<p id="commentContent">Sadrzaj komentara koji sam naposao samo da zauzima prostor.<p>
			</div>
		</div>
        
        <!-- bootstrap se ucitava na kraju da ne bi ugrozavao performanse -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        
        <!-- js fajlovi koji ukljucuju rad sa jQuerijem -->
        <script type="text/javascript" src="js/blog.js"></script>
    </body>
</html>