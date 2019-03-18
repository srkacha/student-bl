package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLType;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionPool;
import dto.User;

public class UserDAO {
	
	private ConnectionDAO connectionDAO = new ConnectionDAO();
	private LoggingInfoDAO loggingInfoDAO = new LoggingInfoDAO();
	
	private static String USER_EXISTS = "select count(*) from user where username=?";
	private static String MAIL_EXISTS = "select count(*) from user where mail=?";
	private static String INSERT = "insert into user(name, surname, mail, passwordHash, username) values (?,?,?,?,?)";
	private static String SELECT = "select * from user where username=? and passwordHash=? and blocked=0";
	private static String SELECT_FOR_ID = "select * from user where id=?";
	private static String UPDATE = "update user set name=?, surname=?, mail=?, description=?, facultyId=?, yearOfStudy=?, picture=? where id=?";
	private static String UPDATE_PASSWORD = "update user set passwordHash=? where id=?";
	private static String SELECT_REQUESTS = "select * from user inner join connection on user.id = connection.userOneId where connection.userTwoId = ? and connection.connectionType=1 and user.blocked=0";
	private static String SELECT_CONNECTIONS = "(select * from user inner join connection on user.id = connection.userOneId where connection.userTwoId = ? and connection.connectionType=2 and user.blocked=0)"
												+ " union (select * from user inner join connection on user.id = connection.userTwoId where connection.userOneId = ? and connection.connectionType=2 and user.blocked=0)";
	private static String SELECT_OTHERS = "select * from user where id not in (select userOneId from connection where userTwoId=? union select userTwoId from connection where userOneId=? and connectionType=2) and id <> ? and blocked=0";
	private static String SELECT_OTHERS_FROM_FACULTY = "select * from user where facultyId=? and id not in (select userOneId from connection where userTwoId=? union select userTwoId from connection where userOneId=? and connectionType=2) and id <> ? and blocked=0";
	private static String IS_USER_ONLINE = "select count(*) from user inner join loggingInfo on user.id = loggingInfo.userId where user.id = ? and loginTimestamp=? and logoutTimestamp>UTC_TIMESTAMP()";
	
	public boolean isUserOnilne(int id) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(IS_USER_ONLINE);
			preparedStatement.setInt(1, id);
			preparedStatement.setTimestamp(2, loggingInfoDAO.getLastLogin(id));
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
	
	public List<User> selectOtherUsersFormFaculty(int id, int facultyId){
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		List<User> resultList = new ArrayList<>();
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_OTHERS_FROM_FACULTY);
			preparedStatement.setInt(1, facultyId);
			preparedStatement.setInt(2, id);
			preparedStatement.setInt(3, id);
			preparedStatement.setInt(4, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				User result = new User();
				result.setId(resultSet.getInt(1));
				result.setName(resultSet.getString(2));
				result.setSurname(resultSet.getString(3));
				result.setMail(resultSet.getString(4));
				result.setPasswordHash(resultSet.getString(5));
				result.setUsername(resultSet.getString(6));
				result.setFacultyId(resultSet.getInt(7));
				result.setYearOfStudy(resultSet.getInt(8));
				result.setPicture(resultSet.getString(9));
				result.setBlocked(resultSet.getBoolean(10));
				result.setDescription(resultSet.getString(11));
				result.setFaculty(new FacultyDAO().getFacultyForId(result.getFacultyId()));
				result.setConnectionType(connectionDAO.getConnectionType(result.getId(), id));
				resultList.add(result);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return resultList;
	}
	
	public List<User> selectAllOtherUsers(int id){
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		List<User> resultList = new ArrayList<>();
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_OTHERS);
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, id);
			preparedStatement.setInt(3, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				User result = new User();
				result.setId(resultSet.getInt(1));
				result.setName(resultSet.getString(2));
				result.setSurname(resultSet.getString(3));
				result.setMail(resultSet.getString(4));
				result.setPasswordHash(resultSet.getString(5));
				result.setUsername(resultSet.getString(6));
				result.setFacultyId(resultSet.getInt(7));
				result.setYearOfStudy(resultSet.getInt(8));
				result.setPicture(resultSet.getString(9));
				result.setBlocked(resultSet.getBoolean(10));
				result.setDescription(resultSet.getString(11));
				result.setFaculty(new FacultyDAO().getFacultyForId(result.getFacultyId()));
				result.setConnectionType(connectionDAO.getConnectionType(result.getId(), id));
				resultList.add(result);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return resultList;
	}
	
	public List<User> selectConnections(int id){
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		List<User> resultList = new ArrayList<>();
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CONNECTIONS);
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				User result = new User();
				result.setId(resultSet.getInt(1));
				result.setName(resultSet.getString(2));
				result.setSurname(resultSet.getString(3));
				result.setMail(resultSet.getString(4));
				result.setPasswordHash(resultSet.getString(5));
				result.setUsername(resultSet.getString(6));
				result.setFacultyId(resultSet.getInt(7));
				result.setYearOfStudy(resultSet.getInt(8));
				result.setPicture(resultSet.getString(9));
				result.setBlocked(resultSet.getBoolean(10));
				result.setDescription(resultSet.getString(11));
				result.setFaculty(new FacultyDAO().getFacultyForId(result.getFacultyId()));
				result.setConnectionType(connectionDAO.getConnectionType(result.getId(), id));
				resultList.add(result);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return resultList;
	}
	
	public List<User> selectRequests(int id){
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		List<User> resultList = new ArrayList<>();
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REQUESTS);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				User result = new User();
				result.setId(resultSet.getInt(1));
				result.setName(resultSet.getString(2));
				result.setSurname(resultSet.getString(3));
				result.setMail(resultSet.getString(4));
				result.setPasswordHash(resultSet.getString(5));
				result.setUsername(resultSet.getString(6));
				result.setFacultyId(resultSet.getInt(7));
				result.setYearOfStudy(resultSet.getInt(8));
				result.setPicture(resultSet.getString(9));
				result.setBlocked(resultSet.getBoolean(10));
				result.setDescription(resultSet.getString(11));
				result.setFaculty(new FacultyDAO().getFacultyForId(result.getFacultyId()));
				result.setConnectionType(connectionDAO.getConnectionType(result.getId(), id));
				resultList.add(result);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return resultList;
	}
	
	public User select(String username, String passwordHash) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		User result = null;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, passwordHash);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				result = new User();
				result.setId(resultSet.getInt(1));
				result.setName(resultSet.getString(2));
				result.setSurname(resultSet.getString(3));
				result.setMail(resultSet.getString(4));
				result.setPasswordHash(resultSet.getString(5));
				result.setUsername(resultSet.getString(6));
				result.setFacultyId(resultSet.getInt(7));
				result.setYearOfStudy(resultSet.getInt(8));
				result.setPicture(resultSet.getString(9));
				result.setBlocked(resultSet.getBoolean(10));
				result.setDescription(resultSet.getString(11));
				result.setFaculty(new FacultyDAO().getFacultyForId(result.getFacultyId()));
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
	
	public User selectForId(int id) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		User result = null;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FOR_ID);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				result = new User();
				result.setId(resultSet.getInt(1));
				result.setName(resultSet.getString(2));
				result.setSurname(resultSet.getString(3));
				result.setMail(resultSet.getString(4));
				result.setPasswordHash(resultSet.getString(5));
				result.setUsername(resultSet.getString(6));
				result.setFacultyId(resultSet.getInt(7));
				result.setYearOfStudy(resultSet.getInt(8));
				result.setPicture(resultSet.getString(9));
				result.setBlocked(resultSet.getBoolean(10));
				result.setDescription(resultSet.getString(11));
				result.setFaculty(new FacultyDAO().getFacultyForId(result.getFacultyId()));
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			cPool.checkIn(connection);
		}
		return result;
	}
	
	public boolean insert(User user) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getSurname());
			preparedStatement.setString(3, user.getMail());
			preparedStatement.setString(4, user.getPasswordHash());
			preparedStatement.setString(5, user.getUsername());
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
	
	public boolean usernameExists(String username) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = true;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(USER_EXISTS);
			preparedStatement.setString(1, username);
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
	
	public boolean mailExists(String mail) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = true;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(MAIL_EXISTS);
			preparedStatement.setString(1, mail);
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
	
	public boolean update(User user) {
		
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getSurname());
			preparedStatement.setString(3, user.getMail());
			preparedStatement.setString(4, user.getDescription());
			if(user.getFacultyId() == 0) {
				preparedStatement.setNull(5, Types.INTEGER);
			}else preparedStatement.setInt(5, user.getFacultyId());
			
			preparedStatement.setInt(6, user.getYearOfStudy());
			preparedStatement.setString(7, user.getPicture());
			preparedStatement.setInt(8, user.getId());
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
	
	public boolean updatePassword(int id, String passwordHash) {
		ConnectionPool cPool = ConnectionPool.getConnectionPool();
		Connection connection = null;
		boolean result = false;
		try {
			connection = cPool.checkOut();
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD);
			preparedStatement.setString(1, passwordHash);
			preparedStatement.setInt(2, id);
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
