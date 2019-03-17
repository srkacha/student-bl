package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.ConnectionPool;

public class ConnectionDAO {
	
	private static String COUNT_REQ = "select count(*) from connection where userTwoId=? and connectionType=1";
	private static String REMOVE_CONNECTION = "delete from connection where (userOneId=? and userTwoId=?) or (userOneId=? and userTwoId=?)";
	private static String SEND_REQUEST = "insert into connection values(?,?,1)";
	private static String ACCEPT_REQUEST = "update connection set connectionType=2 where userOneId=? and userTwoId=?";
	private static String GET_CONN_TYPE = "select connectionType from connection where (userOneId=? and userTwoId=?) or (userOneId=? and userTwoId=?)";
	
	
	public int countRequestsForId(int id) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		int result = 0;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(COUNT_REQ);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				int value = resultSet.getInt(1);
				result = value;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
	
	public int getConnectionType(int idOne, int idTwo) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		int result = 0;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(GET_CONN_TYPE);
			preparedStatement.setInt(1, idOne);
			preparedStatement.setInt(2, idTwo);
			preparedStatement.setInt(3, idTwo);
			preparedStatement.setInt(4, idOne);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				int value = resultSet.getInt(1);
				result = value;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
	
	public boolean removeConnection(int idOne, int idTwo) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_CONNECTION);
			preparedStatement.setInt(1, idOne);
			preparedStatement.setInt(2, idTwo);
			preparedStatement.setInt(3, idTwo);
			preparedStatement.setInt(4, idOne);
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
	
	public boolean sendRequest(int idOne, int idTwo) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(SEND_REQUEST);
			preparedStatement.setInt(1, idOne);
			preparedStatement.setInt(2, idTwo);
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
	
	public boolean acceptRequest(int idAccepter, int idSender) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(ACCEPT_REQUEST);
			preparedStatement.setInt(1, idSender);
			preparedStatement.setInt(2, idAccepter);
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
