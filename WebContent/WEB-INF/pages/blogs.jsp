<%@page import="java.text.SimpleDateFormat"%>
<%@page import="dto.User"%>
<%@page import="beans.ContentBean"%>
<%@page import="dto.Blog"%>
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
        		<div class="col-md-8 rounded bg-light p-2 mt-2">
        			<div class="form-inline">
        				<h1 class="text-dark"><i class="fas fa-blog"></i> Blogovi</h1>
        				<button class="btn btn-secondary ml-auto" data-toggle="modal" data-target="#myModal"><i class="fas fa-plus"></i> Novi blog</button>
        			</div>
        			
        			<div class="modal" id="myModal">
						<div class="modal-dialog">
							<div class="modal-content">
	
								<!-- Modal Header -->
								<div class="modal-header">
									<h4 class="modal-title">Novi blog</h4>
									<button type="button" class="close" data-dismiss="modal">&times;</button>
								</div>
	
								<!-- Modal body -->
								<div class="modal-body text-left">
									<form id="newBlogForm" action="Blogs" method="post">
										<div class="form-gorup">
											<label>Naslov:</label>
											<input type="title" class="form-control" name="title">
										</div>
										<div class="form-gorup">
											<label>Sadržaj:</label>
											<textarea type="content" class="form-control" name="content" rows="8"></textarea>
										</div>
										<input type="hidden" name="action" value="newBlog">
										<br>
										<label id="blogMessage" class="text-danger"></label>
										<br>
										
									</form>
								</div>
	
								<!-- Modal footer -->
								<div class="modal-footer">
									<button id="createBlogButton" type="button" class="btn btn-secondary">Kreiraj</button>
									<button type="button" class="btn btn-danger"
										data-dismiss="modal">Zatvori</button>
								</div>
							</div>
						</div>
					</div>
        			
        			
        			<hr>
        			<div class="blog-list-div">
        				<%
        					ContentBean contentBean = (ContentBean)session.getAttribute("contentBean");
        					for(Blog blog: contentBean.getAllBlogs()){
        						String id = blog.getId();
        						User user = blog.getUser();
        						String nameAndSurname = user.getName() + " " + user.getSurname();
        						int userId = user.getId();
        						String userImage = user.getPicture()==null || user.getPicture().equals("")?"img/user.svg":user.getPicture();
        						String timestamp = new SimpleDateFormat("dd MMM yyyy HH:mm").format(blog.getTimestamp());
        						String title = blog.getTitle();
        				%>
        					<div id="blog-body" class="bg-white rounded p-1">
	        					<div id="blog-header" class="bg-secondary text-white form-inline p-1 rounded">
	        						<img alt="" src="<%=userImage %>" height="20" width="20" class="rounded-circle mr-1">
	        						<a style="text-decoration: none" href="Account?action=view&userId=<%=userId %>" class="text-white"><%=nameAndSurname %></a>
	        						<span class="ml-auto"><%=timestamp %></span>
	        					</div>
	        					<div id="blog-title" class="p-2">
	        						<h3><a style="text-decoration: none" href="Blogs?action=view&blogId=<%=id %>" class="text-dark"><%=title %></a></h3>
	        					</div>
        					</div>
        				<%
        					}
        				%>
        			</div>
        		</div>
        	</div>
        </div>
        
        <!-- template za dodavanje novokreiranog bloga -->
        <div id="blog-body" class="bg-white rounded p-1 mb-2" style="display: none">
			<div id="blog-header" class="bg-secondary text-white form-inline p-1 rounded">
				<img alt="" src="img/user.svg" height="20" width="20" class="rounded-circle mr-1">
				<a href="#" class="text-white">Srdjan Jovic</a>
				<span class="ml-auto">10 Feb 2019 13:23</span>
			</div>
			<div id="blog-title" class="p-2">
				<h3><a href="#" class="text-dark">Naslov novog bloga</a></h3>
			</div>
		</div>
        
        <!-- bootstrap se ucitava na kraju da ne bi ugrozavao performanse -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        
        <!-- js fajlovi koji ukljucuju rad sa jQuerijem -->
        <script type="text/javascript" src="js/blogs.js"></script>
    </body>
</html>