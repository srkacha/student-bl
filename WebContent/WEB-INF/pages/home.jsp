<%@page import="dto.Blog"%>
<%@page import="dto.NewsPost"%>
<%@page import="dto.EventPost"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="dto.UserPost"%>
<%@page import="dto.Post"%>
<%@page import="java.util.List"%>
<%@page import="beans.ContentBean"%>
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
    <body onload="bodyOnLoad()">
        <%@include file="include/header.jsp" %>
        <div id="small-screen-online-users" class="container-fluid" style="display: none">
        	<div id="small-screen-online-row" class="row p-2 justify-content-center bg-white">
	        	<div class="col-md-12">
	        		<div class="bg-light mr-1 ml-1 rounded p-2">
	        			<div class="bg-dark px-2 py-1 rounded text-white"><h4>Aktivni korisnici</h4></div>
				        <div class="scrolling-wrapper" style="overflow-x: scroll; overflow-y: hidden; white-space: nowrap;">
							  <%
							  	for(User online: connectionBean.getOnlineConnections()){
							  %>
							  	<%
							  		String image = online.getPicture();
							  		image = image==null || "".equals(image)?"img/user.svg":image;
							  		String link = "Account?action=view&userId=" + online.getId();
							  		String name = online.getName() + " " + online.getSurname();
							  	%>
							  	<div class="p-2 text-center" style="display: inline-block;">
							  		<img class="rounded-circle" height="75" width="75" alt="" src="<%= image%>">
							  		<a href="<%= link%>" class="text-secondary" style="display: block;"><%=name %></a>
							  	</div>
							  <%
							  	}
							  %>
						</div>
					</div>
	        	</div>
        	</div>
        </div>
        
        <div class="container-fluid">
        	<div class="row p-2 justify-content-center bg-white">
        		<div id="onlineUsersDiv" class="col-md-3 order-1 order-md-1">
        			<div id="onlineUsersContent" class="bg-light mr-1 ml-1 rounded p-2 position-fixed" style="width:22%">
        				<div class="bg-dark px-2 py-1 rounded text-white"><h4><i class="fas fa-users"></i> Aktivni korisnici</h4></div>
        				<div id="online-user-list" class="mt-1" style="overflow-y: scroll;">
							<%
							  	for(User online: connectionBean.getOnlineConnections()){
							  %>
							  	<%
							  		String image = online.getPicture();
							  		image = image==null || "".equals(image)?"img/user.svg":image;
							  		String link = "Account?action=view&userId=" + online.getId();
							  		String name = online.getName() + " " + online.getSurname();
							  	%>
							  	<div class="p-2">
									<img class="rounded-circle mr-2" height="35" width="35" alt="" src="<%=image%>">
									<a href="<%=link%>" class="text-secondary"><%=name%></a>
								</div>
							  <%
							  	}
							  %>
							  
						</div>
					</div>
					
        		</div>
        		<div id="homeContentDiv" class="col-md-6 order-3 order-md-2">
        			<div class="bg-light mr-1 ml-1 rounded p-2">
        				<div class="bg-dark px-2 py-1 rounded text-white">
        					<div class="form-inline py-1">
        						<h4>Objave</h4>
        						<button id="newPostCollapse" class=" ml-auto btn btn-dark" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">Nova objava</button>
        					</div>
						</div>
						<div class="collapse mt-2" id="collapseExample">
							<form>
								<div class="form-group">
									<textarea id="newPostContent" class="form-control" rows="3" style="width: 100%" name="content" placeholder="Sadržaj"></textarea>
								</div>
								<div class="form-group">
									<input id="newPostLink" type="text" class="form-control" name="link" placeholder="Link">
								</div>
								<div class="form-group text-right">
									<label id="newPostError" class="text-danger"></label>
									<button id="newPostButton" type="button" class="btn btn-secondary ml-1"><i class="fas fa-plus"></i> Objavi</button>
								</div>
							</form>
							<hr>
						</div>
        			</div>
        			<div id="content-container" class="content-container">
        				<%
        					ContentBean contentBean = (ContentBean)session.getAttribute("contentBean");
        					List<Post> allPosts = contentBean.getPosts();
        					for(Post p: allPosts){
        				%>
        					<%
        						if(p instanceof UserPost){
        							UserPost userPost = (UserPost)p;
        							User poster = userPost.getUser();
        							int userId = poster.getId();
        							String image = poster.getPicture() == null || poster.getPicture().equals("")?"img/user.svg":poster.getPicture();
        							Timestamp timestamp = userPost.getTimestamp();
        							String time = new SimpleDateFormat("dd MMM yyyy HH:mm").format(timestamp);
        							int likes = userPost.getLikes();
        							int dislikes = userPost.getDislikes();
        							String content = userPost.getContent();
        							String link = userPost.getLink();
        							boolean isYT = userPost.isYoutubeLink();
        							int feedback = userPost.getFeedback();
        									
        					%>
        						<div class="bg-light rounded mr-1 ml-1 mt-1 px-3 py-2">
		        					<div class=" content-header-div rounded px-2 py-1 text-dark form-inline">
		        						<img alt="" src="<%=image %>" class="rounded-circle mr-1" height="25" width="25">
		        						<a href="Account?action=view&userId=<%=userId%>" class="text-dark"><%=poster.getName() + " " + poster.getSurname() %></a>
		        						<span id="post-date-span" class="ml-auto"><%=time %></span>
		        					</div>
		        					<div class="content-main-div px-2 py-4">
		        						<span id="post-content-span"><%=content %></span>
		        					</div>
		        					
		        					<%
		        						if(link!=null && !"".equals(link) && isYT ){
		        					%>
		        						<div class="embed-responsive embed-responsive-16by9 mb-4">
										<iframe class="embed-responsive-item"
											src="<%=link %>"
											allowfullscreen></iframe>
										</div>
		        					<%
		        						}else if(link!=null && !"".equals(link)){
		        					%>
		        						<div class="px-2 mb-4 text-dark">
		        							<p>Link:</p>
		        							<a href="<%=link%>"><%=link %></a>
		        						</div>
		        					<%
		        						}
		        					%>
			
									<div class="content-likes-div">
		        						<button id="like-<%=userPost.getId() %>" class="like-button btn"><i class="<%=feedback==1?"fas":"far" %> fa-thumbs-up"></i></button>
		        						<span id="likes-<%=userPost.getId()%>"><%=likes %></span>
		        						<button id="dislike-<%=userPost.getId() %>" class="dislike-button btn"><i class="<%=feedback==2?"fas":"far" %> fa-thumbs-down"></i></button>
		        						<span id="dislikes-<%=userPost.getId()%>"><%=dislikes %></span>
		        					</div>
			        			</div>
        					<%
        						}else if(p instanceof EventPost){
        							EventPost eventPost = (EventPost)p;
        					%>
        						Event post generator
        					<%
        						}else if(p instanceof NewsPost){
        							NewsPost newsPost = (NewsPost)p;
        					%>
        						News post generator
        					<%
        						}
        					%>
        					
        				<%
        					}
        				%>
        			</div>
        			
        		</div>
        		<div id="blogsAndFilesDiv" class="col-md-3 order-2 order-md-3">
        			<div class="bg-light mr-1 ml-1 rounded p-2">
        				<div class="bg-dark px-2 py-1 rounded text-white mb-2"><h4><i class="fas fa-blog"></i> <a style="text-decoration: none" href="Blogs?action=all" class="text-white">Blogovi</a></h4></div>
        				<div id="blogs-div" class="mb-2">
        					<%
        						for(Blog blog: contentBean.getLastFiveBlogs()){
        					%>
        						<div class="rounded p-2 mb-1 bg-white">
        							<a style="text-decoration: none" href="Blogs?action=view&blogId=<%=blog.getId() %>" id="blog-name" class="text-dark"><%=blog.getTitle() %></a>
        						</div>
        					<%
        						}
        					%>
        				</div>
        				<div class="bg-dark px-2 py-1 rounded text-white mb-2"><h4><i class="fas fa-file"></i> <a style="text-decoration: none" href="Files" class="text-white">Materijali</a></h4></div>
        				<div id="files-div">
        					File 1
        				</div>
        			</div>
        		</div>
        	</div>
        </div>
        
        <!-- bootstrap se ucitava na kraju da ne bi ugrozavao performanse -->
        <script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        
        <!-- script fajlovi koje sam ja pisao -->
        <script type="text/javascript" src="js/home.js"></script>
    </body>
</html>