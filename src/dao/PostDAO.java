package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import db.ConnectionPool;

public class PostDAO {
	
	private static String INSERT_POST = "insert into post(timestamp, deleted) values(?,0)";
	
	public int insert() {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		int result = 0;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_POST, Statement.RETURN_GENERATED_KEYS);
			Timestamp now = new Timestamp(new Date().getTime());
			preparedStatement.setTimestamp(1, now);
			preparedStatement.executeUpdate();
			if(preparedStatement.getUpdateCount() > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				if(resultSet.next()) {
					result = resultSet.getInt(1);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}

}
