package dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public abstract class Post implements Serializable{
	
	private int id;
	private Timestamp timestamp;
	private boolean deleted;
	private int likes;
	private int dislikes;
	private int feedback;
	
	public int getFeedback() {
		return feedback;
	}
	public void setFeedback(int feedback) {
		this.feedback = feedback;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getDislikes() {
		return dislikes;
	}
	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Post) {
			Post temp = (Post)o;
			if(temp.getId() == this.id) return true;
			else return false;
		}else return false;
	}
	
	
}
