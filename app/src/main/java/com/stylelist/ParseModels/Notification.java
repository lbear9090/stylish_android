package com.stylelist.ParseModels;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Notification")
public class Notification extends ParseObject {

	public Notification() {
		// A default constructor is required.
	}
	
	public ParseUser getFromUser(){
		try {
			return fetchIfNeeded().getParseUser("fromUser");
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	public ParseUser getToUser(){
		try {
			return fetchIfNeeded().getParseUser("toUser");
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	public ParseUser getUser() {
		try {
			return fetchIfNeeded().getParseUser("fromUser");
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	public String getType(){
		try {
			String type = fetchIfNeeded().getString("type");
			if (type == null) {
				return "";
			} else {
				return type;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}
	public String getContent(){
		try {
			String content = fetchIfNeeded().getString("content");
			if (content == null) {
			    return "";
            } else {
			    return content;
            }
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}
	public String getPostId() {
		try {
			String id = fetchIfNeeded().getString("postId");
			if (id == null) {
			    return "";
            } else {
			    return id;
            }
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	public Post getPost() {
		try {
			Post post = (Post) fetchIfNeeded().get("post");
			if (post == null) {
				return null;
			} else {
				return post;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setFromUser(ParseUser fromUser){
		put("fromUser", fromUser);
	}
	public void setToUser(ParseUser toUser){
		put("toUser", toUser);
	}
	public void setPost(Post parsePost){
		put("post", parsePost);
		put("postId", parsePost.getObjectId());
	}
	public void setType(String type){
		put("type", type);
	}
	public void setContent(String content){
		put("content", content);
	}
	
}
