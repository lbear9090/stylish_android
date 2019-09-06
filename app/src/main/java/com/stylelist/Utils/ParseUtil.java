package com.stylelist.Utils;

import android.content.Context;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.stylelist.Interfaces.ParseUtilCallBack;
import com.stylelist.Models.Result;
import com.stylelist.ParseModels.ItemPost;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;

import java.util.List;

/**
 * Created by security on 2/21/2018.
 */

public class ParseUtil {

    private Context context;
    private ParseUtilCallBack callBack;
    private int resultCount;

    public ParseUtil(Context context, ParseUtilCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public void getAllUsers() {
        ParseQuery<ParseUser> userQuery = new ParseQuery<ParseUser>("_User");
        userQuery.orderByDescending("createdAt");
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    Result<List<ParseUser>> result = new Result<>();
                    result.setT(objects);
                    callBack.onGetArray(result);
                }
            }
        });
    }

    public void getItemPostsForPost(String postId) {
        ParseQuery<ItemPost> postParseQuery = new ParseQuery<>("ItemPost");
        postParseQuery.whereEqualTo(Constants.ITEM_POST_POST_ID, postId);
        postParseQuery.orderByDescending("createdAt");
        postParseQuery.findInBackground(new FindCallback<ItemPost>() {
            @Override
            public void done(List<ItemPost> objects, ParseException e) {
                if (e == null) {
                    Result<List<ItemPost>> result = new Result<>();
                    result.setT(objects);
                    callBack.onGetArray(result);
                }
            }
        });
    }

    public void getAllPosts() {
        ParseQuery<Post> postParseQuery = new ParseQuery<Post>("Post");
        postParseQuery.orderByDescending("createdAt");
        postParseQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    Result<List<Post>> result = new Result<>();
                    result.setT(objects);
                    callBack.onGetArray(result);
                }
            }
        });
    }

    public void getAllActivities() {
        ParseQuery<Notification> activityParseQuery = new ParseQuery<>("Notification");
        activityParseQuery.orderByDescending("createdAt");
        activityParseQuery.findInBackground(new FindCallback<Notification>() {
            @Override
            public void done(List<Notification> objects, ParseException e) {
                if (e == null) {
                    Result<List<Notification>> result = new Result<>();
                    result.setT(objects);
                    callBack.onGetArray(result);
                }
            }
        });
    }

    public void getTrendingPosts() {
        ParseQuery<Post> postParseQuery = new ParseQuery<>("Post");
        postParseQuery.orderByDescending(Constants.POST_POPULAR_POINTS);
        postParseQuery.setLimit(20);
        postParseQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    Result<List<Post>> result = new Result<>();
                    result.setT(objects);
                    callBack.onGetArray(result);
                }
            }
        });
    }

    public void getCommentsForPost(Post post) {
        ParseQuery<Notification> commentQuery = new ParseQuery<>("Notification");
        commentQuery.whereEqualTo("post", post);
        commentQuery.whereEqualTo("type", "comment");
        commentQuery.orderByAscending("createdAt");
        commentQuery.findInBackground(new FindCallback<Notification>() {
            @Override
            public void done(List<Notification> objects, ParseException e) {
                if (e == null) {
                    Result<List<Notification>> result = new Result<>();
                    result.setT(objects);
                    callBack.onGetArray(result);
                }
            }
        });
    }

    public String getPostLikesCount(Post currentPost) {
        resultCount = 0;
        ParseQuery<Notification> photoLikes = new ParseQuery<>("Notification");
        photoLikes.whereEqualTo("post", currentPost);
        photoLikes.whereEqualTo("type", "like");
        photoLikes.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        photoLikes.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    resultCount = count;
                } else {
                    e.printStackTrace();
                }
            }
        });
        return String.valueOf(resultCount);
    }

    public int getPostCommentsCount(Post currentPost) {
        final int[] resultCount = {0};
        ParseQuery<Notification> photoComments = new ParseQuery<>("Notification");
        photoComments.whereEqualTo("post", currentPost);
        photoComments.whereEqualTo("type", "comment");
        photoComments.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        photoComments.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    resultCount[0] = count;
                } else {
                    e.printStackTrace();
                }
            }
        });
        return resultCount[0];
    }

//    public void getStyles() {
//        ParseQuery<StyleMan> styleManParseQuery = new ParseQuery<StyleMan>("StyleMan");
//        styleManParseQuery.orderByAscending("created_at");
//        styleManParseQuery.findInBackground(new FindCallback<StyleMan>() {
//            @Override
//            public void done(List<StyleMan> objects, ParseException e) {
//                if (e == null) {
//                    Result<List<StyleMan>> result = new Result<>();
//                    result.setT(objects);
//                    callBack.onGetArray(result);
//                }
//            }
//        });
//    }
//
//    public void getStyleExamples() {
//        ParseQuery<StyleManDetail> styleManParseQuery = new ParseQuery<StyleManDetail>("StyleManDetail");
//        styleManParseQuery.orderByAscending("created_at");
//        styleManParseQuery.findInBackground(new FindCallback<StyleManDetail>() {
//            @Override
//            public void done(List<StyleManDetail> objects, ParseException e) {
//                if (e == null) {
//                    Result<List<StyleManDetail>> result = new Result<>();
//                    result.setT(objects);
//                    callBack.onGetArray(result);
//                }
//            }
//        });
//    }
}
