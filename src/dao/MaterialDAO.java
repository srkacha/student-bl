package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionPool;
import dto.Material;

public class MaterialDAO {
	
	private UserDAO userDAO = new UserDAO();
	
	private static String INSERT = "insert into material(timestamp, path, description, userId) values(?,?,?,?)";
	private static String SELECT_ALL = "select * from material order by timestamp desc";
	private static String SELECT_LAST_THREE = "select * from material order by timestamp desc limit 3";
	
	public boolean insert(Material material) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
			preparedStatement.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
			preparedStatement.setString(2, material.getPath());
			preparedStatement.setString(3, material.getDescription());
			preparedStatement.setInt(4, material.getUserId());
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
	
	public List<Material> selectAll(){
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		List<Material> result = new ArrayList<>();
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				Material material = new Material();
				material.setId(resultSet.getInt(1));
				material.setTimestamp(resultSet.getTimestamp(2));
				material.setPath(resultSet.getString(3));
				material.setDescription(resultSet.getString(4));
				material.setUserId(resultSet.getInt(5));
				material.setUser(userDAO.selectForId(material.getUserId()));
				
				result.add(material);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
	
	public List<Material> selectLastThree(){
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		List<Material> result = new ArrayList<>();
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LAST_THREE);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				Material material = new Material();
				material.setId(resultSet.getInt(1));
				material.setTimestamp(resultSet.getDate(2));
				material.setPath(resultSet.getString(3));
				material.setDescription(resultSet.getString(4));
				material.setUserId(resultSet.getInt(5));
				material.setUser(userDAO.selectForId(material.getUserId()));
				
				result.add(material);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
}
