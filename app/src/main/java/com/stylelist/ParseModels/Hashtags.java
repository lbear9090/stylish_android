package com.stylelist.ParseModels;
import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("Hashtags")
public class Hashtags extends ParseObject {
    public static String HASHTAG = "hashtag";

    public void setHashtag(String hashtag) {
        put("hashtag", hashtag);
    }

}
