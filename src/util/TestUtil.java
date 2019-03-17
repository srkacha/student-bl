package util;

import java.util.Date;

import beans.ContentBean;
import dao.BlogDAO;
import dto.Blog;
import dto.Comment;
import dto.Post;

public class TestUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Blog blog = new Blog();
		blog.setUserId(2);
		blog.setTitle("Test blog 2");
		blog.setContent("Test content za blog 2");
		BlogDAO blogDAO = new BlogDAO();
		Comment comment = new Comment();
		comment.setUserId(2);
		comment.setContent("hahahahahha");
		comment.setTimestamp(new Date());
		System.out.println(blogDAO.getById("5c8cc401314f3816ecc2f4df").getComments().get(0).getContent());
	}

}
