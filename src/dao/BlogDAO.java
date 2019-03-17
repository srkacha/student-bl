package dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import dto.Blog;
import dto.Comment;

import static com.mongodb.client.model.Filters.eq;

public class BlogDAO {
	
	private UserDAO userDAO = new UserDAO();
	
	public boolean insert(Blog blog) {
		boolean result = false;
		MongoClient mongoClient = null;
		try {
			mongoClient = MongoClients.create();
			MongoDatabase database = mongoClient.getDatabase("blogs");
			Document document = new Document("userId", blog.getUserId()).append("title", blog.getTitle()).append("content", blog.getContent()).append("timestamp", new Date()).append("comments", new ArrayList<Document>());
			MongoCollection<Document> blogs = database.getCollection("blog");
			blogs.insertOne(document);
			result = true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Blog> selectAll(){
		List<Blog> result = new ArrayList<>();
		MongoClient mongoClient = null;
		try {
			mongoClient = MongoClients.create();
			MongoDatabase database = mongoClient.getDatabase("blogs");
			MongoCollection<Document> blogs = database.getCollection("blog");
			MongoCursor<Document> cursor = blogs.find().sort(new BasicDBObject("timestamp", -1)).iterator();
			while(cursor.hasNext()) {
				Document document = cursor.next();
				Blog blog = new Blog();
				ObjectId objectId = document.getObjectId("_id");
				blog.setId(objectId.toString());
				blog.setUserId(document.getInteger("userId"));
				blog.setUser(userDAO.selectForId(blog.getUserId()));
				blog.setTitle(document.getString("title"));
				blog.setTimestamp(document.getDate("timestamp"));
				blog.setContent(document.getString("content"));
				List<Comment> comments = new ArrayList<>();
				Object blogCommentsObject = document.get("comments", new ArrayList<Document>());
				if(blogCommentsObject!=null) {
					List<Document> blogComments = (List<Document>)blogCommentsObject;
					for(Document d: blogComments) {
						Comment comment = new Comment();
						comment.setUserId(d.getInteger("userId"));
						comment.setUser(userDAO.selectForId(comment.getUserId()));
						comment.setTimestamp(d.getDate("timestamp"));
						comment.setContent(d.getString("content"));
						comments.add(comment);
					}
					blog.setComments(comments);
				}
				result.add(blog);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}
	
	public List<Blog> selectLastFive(){
		List<Blog> result = new ArrayList<>();
		MongoClient mongoClient = null;
		try {
			mongoClient = MongoClients.create();
			MongoDatabase database = mongoClient.getDatabase("blogs");
			MongoCollection<Document> blogs = database.getCollection("blog");
			MongoCursor<Document> cursor = blogs.find().sort(new BasicDBObject("timestamp", -1)).limit(5).iterator();
			while(cursor.hasNext()) {
				Document document = cursor.next();
				Blog blog = new Blog();
				ObjectId objectId = document.getObjectId("_id");
				blog.setId(objectId.toString());
				blog.setUserId(document.getInteger("userId"));
				blog.setUser(userDAO.selectForId(blog.getUserId()));
				blog.setTitle(document.getString("title"));
				blog.setTimestamp(document.getDate("timestamp"));
				blog.setContent(document.getString("content"));
				List<Comment> comments = new ArrayList<>();
				Object blogCommentsObject = document.get("comments", new ArrayList<Document>());
				if(blogCommentsObject!=null) {
					List<Document> blogComments = (List<Document>)blogCommentsObject;
					for(Document d: blogComments) {
						Comment comment = new Comment();
						comment.setUserId(d.getInteger("userId"));
						comment.setUser(userDAO.selectForId(comment.getUserId()));
						comment.setTimestamp(d.getDate("timestamp"));
						comment.setContent(d.getString("content"));
						comments.add(comment);
					}
					blog.setComments(comments);
				}
				result.add(blog);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}
	
	public Blog getById(String id) {
		Blog result = null;
		MongoClient mongoClient = null;
		try {
			mongoClient = MongoClients.create();
			MongoDatabase database = mongoClient.getDatabase("blogs");
			MongoCollection<Document> blogs = database.getCollection("blog");
			Document document = blogs.find(eq("_id", new ObjectId(id))).first();
			if(document != null) {
				Blog blog = new Blog();
				ObjectId objectId = document.getObjectId("_id");
				blog.setId(objectId.toString());
				blog.setUserId(document.getInteger("userId"));
				blog.setUser(userDAO.selectForId(blog.getUserId()));
				blog.setTitle(document.getString("title"));
				blog.setTimestamp(document.getDate("timestamp"));
				blog.setContent(document.getString("content"));
				List<Comment> comments = new ArrayList<>();
				Object blogCommentsObject = document.get("comments", new ArrayList<Document>());
				if(blogCommentsObject!=null) {
					List<Document> blogComments = (List<Document>)blogCommentsObject;
					for(Document d: blogComments) {
						Comment comment = new Comment();
						comment.setUserId(d.getInteger("userId"));
						comment.setUser(userDAO.selectForId(comment.getUserId()));
						comment.setTimestamp(d.getDate("timestamp"));
						comment.setContent(d.getString("content"));
						comments.add(comment);
					}
					Collections.reverse(comments);
					blog.setComments(comments);
				}
				result = blog;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}
	
	public boolean addComment(String postId, Comment comment) {
		boolean result = false;
		MongoClient mongoClient = null;
		try {
			mongoClient = MongoClients.create();
			MongoDatabase database = mongoClient.getDatabase("blogs");
			MongoCollection<Document> blogs = database.getCollection("blog");
			Document document = blogs.find(eq("_id", new ObjectId(postId))).first();
			if(document!=null) {
				List<Document> comments = (List<Document>)document.get("comments", new ArrayList<Document>());
				Document c = new Document("userId", comment.getUserId()).append("timestamp", comment.getTimestamp()).append("content", comment.getContent());
				comments.add(c);
				blogs.updateOne(eq("_id", new ObjectId(postId)), new Document("$set", new Document("comments", comments)));
				result = true;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	

}
