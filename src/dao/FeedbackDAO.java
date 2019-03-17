package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.ConnectionPool;

public class FeedbackDAO {

	private static String GET_LIKES = "select count(*) from feedback where postId = ? and positive=1";
	private static String GET_DISLIKES = "select count(*) from feedback where postId = ? and positive=0";
	private static String LIKE = "insert into feedback values(?,?,1)";
	private static String DISLIKE = "insert into feedback values(?,?,0)";
	private static String REMOVE_FEEDBACK = "delete from feedback where userId = ? and postId = ?";
	private static String USER_LEFT_FEEDBACK = "select positive from feedback where postId=? and userId=?";
	
	public int selectUserFeedback(int postId, int userId) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		int result = 0;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(USER_LEFT_FEEDBACK);
			preparedStatement.setInt(1, postId);
			preparedStatement.setInt(2, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				boolean positive = resultSet.getBoolean(1);
				if(positive) result = 1;
				else result = 2;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
	
	public int getLikes(int id) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		int result = 0;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(GET_LIKES);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				result = resultSet.getInt(1);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
	
	public int getDislikes(int id) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		int result = 0;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(GET_DISLIKES);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				result = resultSet.getInt(1);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
	
	public boolean removeFeedback(int userId, int postId) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_FEEDBACK);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, postId);
			preparedStatement.executeUpdate();
			if(preparedStatement.getUpdateCount()>0) {
				result = true;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
	
	public boolean like(int userId, int postId) {
		//uklonimo postojeci like ili dislike
		removeFeedback(userId, postId);
		
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(LIKE);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, postId);
			preparedStatement.executeUpdate();
			if(preparedStatement.getUpdateCount()>0) {
				result = true;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
	
	public boolean dislike(int userId, int postId) {
		//uklonimo postojeci like ili dislike
		removeFeedback(userId, postId);
		
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(DISLIKE);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, postId);
			preparedStatement.executeUpdate();
			if(preparedStatement.getUpdateCount()>0) {
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
