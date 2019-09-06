package com.stylelist.ParseModels;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("SensitiveData")
public class SensitiveData extends ParseObject {
    public static String EMAIL = "email";

    public ParseUser getFromUser() {
        return getParseUser("fromUser");
    }

    public ParseUser getToUser() {
        return getParseUser("toUser");
    }

    public String getType() {
        return getString("type");
    }

    public String getContent() {
        return getString("content");
    }

    public void setFromUser(ParseUser fromUser) {
        put("fromUser", fromUser);
    }

    public void setToUser(ParseUser toUser) {
        put("toUser", toUser);
    }

    public void setPhoto(Post parsePost) {
        put("parsePost", parsePost);
    }

    public void setType(String type) {
        put("type", type);
    }

    public void setContent(String content) {
        put("content", content);
    }

    public void setEmail(String email) {
        put("email", email);
    }

}
