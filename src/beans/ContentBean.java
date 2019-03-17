package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.swing.internal.plaf.basic.resources.basic;

import dao.BlogDAO;
import dao.UserPostDAO;
import dto.Blog;
import dto.Post;
import dto.User;

public class ContentBean implements Serializable{
	
	private int loggedInUserId;
	private UserPostDAO userPostDAO = new UserPostDAO();
	private BlogDAO blogDAO = new BlogDAO();
	
	private Blog blogToShow = new Blog();
	
	

	public Blog getBlogToShow() {
		return blogToShow;
	}

	public void setBlogToShow(Blog blogToShow) {
		this.blogToShow = blogToShow;
	}

	public int getLoggedInUserId() {
		return loggedInUserId;
	}

	public void setLoggedInUserId(int loggedInUserId) {
		this.loggedInUserId = loggedInUserId;
	}
	
	public List<Post> getPosts(){
		List<Post> userPosts = userPostDAO.selectUserPosts(loggedInUserId);
		List<Post> eventPosts = new ArrayList<>(); //ovdje cemo dohvatiti sve dogadjaje
		List<Post> newsPosts = new ArrayList<>(); //ovdje cemo dohvatiti sve vijesti
		List<Post> allPosts = new ArrayList<>(); //svi postovi ce biti ovdje
		
		//sve ih dodamo u jednu listu
		for(Post p: userPosts) {
			allPosts.add(p);
		}
		for(Post p: eventPosts) {
			allPosts.add(p);
		}
		for(Post p: newsPosts) {
			allPosts.add(p);
		}
		
		//odvojimo popularne postove
		List<Post> popularPosts = allPosts.stream().sorted((a, b) -> (b.getLikes() - b.getDislikes()) - (a.getLikes() - a.getDislikes())).limit(5).collect(Collectors.toList());
		for(Post p: popularPosts) {
			allPosts.remove(p);
		}
		
		//sortiramo ostale postove
		List<Post> sortedOtherPosts = allPosts.stream().sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp())).collect(Collectors.toList());
		
		//sve dodamo u konacnu listu
		List<Post> finalList = new ArrayList<>();
		for(Post p: popularPosts) {
			finalList.add(p);
		}
		for(Post p: sortedOtherPosts) {
			finalList.add(p);
		}
		
		//Collections.reverse(finalList);
		
		return finalList;
		
	}
	
	public List<Blog> getLastFiveBlogs(){
		return blogDAO.selectLastFive();
	}
	
	public List<Blog> getAllBlogs(){
		return blogDAO.selectAll();
	}
	
}
