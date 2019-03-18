package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import db.ConnectionPool;

public class LoggingInfoDAO {
	
	private static String CHECK = "select count(*) from loggingInfo where userId=?";
	private static String INSERT = "insert into loggingInfo values(?,?,?)";
	private static String UPDATE = "update loggingInfo set loginTimestamp=?, logoutTimestamp=? where userId=?";
	private static String UPDATE_LOGOUT = "update loggingInfo set logoutTimestamp=? where userId=? and loginTimestamp=?";
	private static String GET_LAST_LOGIN = "select loginTimestamp from loggingInfo where userId=? order by loginTimestamp desc limit 1";
	
	public Timestamp getLastLogin(int id) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		Timestamp result = new Timestamp(new Date().getTime());
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(GET_LAST_LOGIN);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				result = resultSet.getTimestamp(1);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
	
	public boolean refreshLogout(int id) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LOGOUT);
			Timestamp now = new Timestamp(new Date().getTime() + 30*60*1000);
			preparedStatement.setTimestamp(1, now);
			preparedStatement.setInt(2, id);
			preparedStatement.setTimestamp(3, getLastLogin(id));
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
	
	//kod prvog logina
	public boolean userExists(int id) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = true;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(CHECK);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				int value = resultSet.getInt(1);
				if(value > 0) result = true;
				else result =false;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
	
	//login
	public boolean insert(int id) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
			Timestamp now = new Timestamp(new Date().getTime());
			Timestamp end = new Timestamp(now.getTime() + 30*60*1000);
			preparedStatement.setTimestamp(1, now);
			preparedStatement.setTimestamp(2, end);
			preparedStatement.setInt(3, id);
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
	
	//logout
	public boolean update(int id) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
			Timestamp from = new Timestamp(new Date().getTime());
			Timestamp to = new Timestamp(from.getTime() + 30*60*1000);
			preparedStatement.setTimestamp(1, from);
			preparedStatement.setTimestamp(2, to);
			preparedStatement.setInt(3, id);
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
	
	public boolean update_logout(int id) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LOGOUT);
			Timestamp now = new Timestamp(new Date().getTime());
			preparedStatement.setTimestamp(1, now);
			preparedStatement.setInt(2, id);
			preparedStatement.setTimestamp(3, getLastLogin(id));
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
