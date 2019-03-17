package dto;

public class UserPost extends Post{
	
	private int userId;
	private String content;
	private String link;
	private boolean isYoutubeLink;
	private int postId;
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public boolean isYoutubeLink() {
		return isYoutubeLink;
	}
	public void setYoutubeLink(boolean isYoutubeLink) {
		this.isYoutubeLink = isYoutubeLink;
	}
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	
	
}
