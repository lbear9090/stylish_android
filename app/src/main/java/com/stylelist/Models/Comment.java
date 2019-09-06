package com.stylelist.Models;

import com.parse.ParseUser;

import com.stylelist.ParseModels.Post;

/**
 * Created by security on 2/21/2018.
 */

public class Comment extends BaseModel {

    public ParseUser toUser;
    public ParseUser fromUser;
    public Post post;
    public String content;
}
