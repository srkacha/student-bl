package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.ConnectionPool;
import dto.Post;
import dto.User;
import dto.UserPost;

public class UserPostDAO {
	
	private PostDAO postDAO = new PostDAO();
	private UserDAO userDAO = new UserDAO();
	private FeedbackDAO feedbackDAO = new FeedbackDAO();

	private static String SELECT_USER_POSTS = "select * from post inner join userpost on post.id = userpost.postId where post.deleted=0 and userpost.userId = ?"
			+ " or userpost.userId in (select id from user inner join connection on user.id = connection.userOneId where connection.userTwoId = ? and connection.connectionType=2 and user.blocked=0" 
			+ " union select id from user inner join connection on user.id = connection.userTwoId where connection.userOneId = ? and connection.connectionType=2 and user.blocked=0)";
	private static String INSERT_USER_POST = "insert into userpost values(?,?,?,?,?)";
	
	public List<Post> selectUserPosts(int id){
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		List<Post> resultList = new ArrayList<>();
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_POSTS);
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, id);
			preparedStatement.setInt(3, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				UserPost userPost = new UserPost();
				userPost.setId(resultSet.getInt(1));
				userPost.setTimestamp(resultSet.getTimestamp(2));
				userPost.setDeleted(false);
				userPost.setContent(resultSet.getString(4));
				userPost.setLink(resultSet.getString(5));
				userPost.setYoutubeLink(resultSet.getBoolean(6));
				userPost.setPostId(resultSet.getInt(7));
				userPost.setUserId(resultSet.getInt(8));
				userPost.setUser(userDAO.selectForId(userPost.getUserId()));
				//lajkovi i dislajkovi
				userPost.setLikes(feedbackDAO.getLikes(userPost.getId()));
				userPost.setDislikes(feedbackDAO.getDislikes(userPost.getId()));
				userPost.setFeedback(feedbackDAO.selectUserFeedback(userPost.getId(), id));
				
				resultList.add(userPost);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return resultList;
	}
	
	public boolean insert(UserPost userPost) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		
		//prvo kreiramo post
		int generatedId = postDAO.insert();
		//prekidamo ako se ne generise novi post objekat
		if(generatedId == 0) return false;
		
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_POST);
			preparedStatement.setString(1, userPost.getContent());
			preparedStatement.setString(2, userPost.getLink());
			preparedStatement.setBoolean(3, userPost.isYoutubeLink());
			preparedStatement.setInt(4, generatedId);
			preparedStatement.setInt(5, userPost.getUserId());
			preparedStatement.executeUpdate();
			if(preparedStatement.getUpdateCount() > 0) {
				result = true;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
}
