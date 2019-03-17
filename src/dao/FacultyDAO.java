package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionPool;
import dto.Faculty;

public class FacultyDAO {
	
	private static String GET_FOR_ID = "select * from faculty where id=?";
	private static String GET_ALL = "select * from faculty";
	
	public Faculty getFacultyForId(int id) {
		Faculty result = new Faculty();
		ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		try {
			connection = connectionPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(GET_FOR_ID);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				result.setId(id);
				result.setName(resultSet.getString(2));
				result.setMaxYear(resultSet.getInt(3));
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			connectionPool.checkIn(connection);
		}
		
		return result;
	}
	
	public List<Faculty> getAll(){
		List<Faculty> result = new ArrayList<>();
		ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		try {
			connection = connectionPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				Faculty faculty = new Faculty();
				faculty.setId(resultSet.getInt(1));
				faculty.setName(resultSet.getString(2));
				faculty.setMaxYear(resultSet.getInt(3));
				result.add(faculty);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			connectionPool.checkIn(connection);
		}
		
		return result;
	}
}
