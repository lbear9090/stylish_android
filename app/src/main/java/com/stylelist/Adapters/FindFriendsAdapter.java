package com.stylelist.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;
import com.stylelist.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Yannick Erpelding on 30.07.15.
 */


public class FindFriendsAdapter extends ParseQueryAdapter<ParseUser> {

    public ParseFile thumbnailFile;
//    ParseImageView ProfilePicView;
//    public TextView UsernameView;
//    public static TextView PhotoNum;
    public static ParseUser User;
    public static boolean followAction = true;
//    public static Button FollowBtn;
    private Context context;

    public FindFriendsAdapter(Context context) {
        super(context, new QueryFactory<ParseUser>() {
            public ParseQuery<ParseUser> create() {


                ParseQuery<ParseUser> UserQuery = new ParseQuery<ParseUser>("_User");
//                if(FindFriendsActivity.getSerchedUsername() != null){
//                    UserQuery.whereStartsWith("displayName_lower", FindFriendsActivity.getSerchedUsername());
//                }
                UserQuery.orderByDescending("created_at");
                return UserQuery;

            }
        });
        this.context = context;
    }

    //@Override
    public View getItemView(final ParseUser PUser, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.find_friends_list, null);
        }
        super.getItemView(User, v, parent);

        User = PUser;

        CircleImageView profileImageView = v.findViewById(R.id.image_profile_friend_list);
        CustomFontTextView txtUserName = v.findViewById(R.id.text_username_friend_list);
        final CustomFontTextView txtPhotoNumber= v.findViewById(R.id.text_photo_number_friend_list);
        final CustomFontButton btnFollow = v.findViewById(R.id.btn_follow_friend_list);

        btnFollow.setEnabled(false);
        btnFollow.setClickable(false);

        //Get PhotoNumber
        ParseQuery<Post> TotalLikeQuery = ParseQuery.getQuery("Post");
        TotalLikeQuery.whereEqualTo("user", User);
        TotalLikeQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        TotalLikeQuery.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    txtPhotoNumber.setText(count + " Photos");
                    //	Log.i(SocialNetworkApplication.TAG, "Set PhotoNumber Success");
                    Log.e("Log", "Photos:" + count);
                } else {
                    e.printStackTrace();
                    //	Log.i(SocialNetworkApplication.TAG, "Set PhotoNumber Error");
                    Log.e("Log", "PhotoError:" + e);
                    txtPhotoNumber.setText("0 Photos");
                }
            }

        });


        try {
            txtUserName.setText((String) User.fetchIfNeeded().get("displayName"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //See if CurrentUser has already followed the User
        ParseQuery<Notification> UserPhotoLike = new ParseQuery<>("_User");
        UserPhotoLike.whereEqualTo("User", User);
        UserPhotoLike.whereEqualTo("type", "like");
        UserPhotoLike.whereEqualTo("fromUser", ParseUser.getCurrentUser());
        UserPhotoLike.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        UserPhotoLike.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    if(count == 0){
                        btnFollow.setBackgroundResource(R.drawable.rounded_corners_follow);
                    }
                    else{
                        btnFollow.setBackgroundResource(R.drawable.rounded_corners_following);
                    }
                } else {
                    e.printStackTrace();

                }
            }

        });

        thumbnailFile = User.getParseFile("profilePictureSmall");
        if (thumbnailFile != null) {
            Picasso.with(context).load(thumbnailFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(profileImageView);
        }
        else {
            profileImageView.setImageResource(R.drawable.avatarplaceholderprofile);
        }
        return v;
    }

    public static void FollowButton(int position, ListView list){
        View v = list.getChildAt(position -
                list.getFirstVisiblePosition());
        int follow;
        if(v == null)
            return;
        if(followAction){
            CustomFontButton btnFollow = v.findViewById(R.id.btn_follow_friend_list);
            Object tag = btnFollow.getTag();
            follow = R.drawable.rounded_corners_follow;

            if(tag != null ) {
                if (((Integer) tag).intValue() == follow || btnFollow.getText() == "Follow") {
                    //If FollowButton is gray
                    follow = R.drawable.rounded_corners_following;
                    submitFollow();
                    btnFollow.setText("Following");
                    btnFollow.setTextColor(Color.WHITE);
                } else if (((Integer) tag).intValue() != follow || btnFollow.getText() == "Following") {
                    //If FollowButton is blue
                    follow = R.drawable.rounded_corners_follow;
                    deleteFollow();
                    btnFollow.setText("Follow");
                    btnFollow.setTextColor(Color.BLACK);
                } else {
                    //If no resource can be found
                    follow = R.drawable.rounded_corners_following;
                    submitFollow();
                    Log.e("lol", "i'm giving up1");
                    btnFollow.setText("Following");
                    btnFollow.setTextColor(Color.WHITE);
                }
            }
            else {
                //If no resource can be found
                follow = R.drawable.rounded_corners_following;
                submitFollow();
                Log.e("lol", "i'm giving up");
                btnFollow.setText("Following");
            }

            btnFollow.setTag(follow);
            btnFollow.setBackgroundResource(follow);

            Log.e("Log", "Follow-Button clicked");

            //We don't want any button spam, so we have to disable the action a few seconds
            followAction = false;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 3s = 3000ms
                    followAction = true;
                }
            }, 2000);
        }
    }

    private static void submitFollow() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        Notification followActivity = new Notification();
        followActivity.setFromUser(currentUser);
        followActivity.setToUser(User);
        followActivity.setType("follow");
        ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        acl.setWriteAccess(ParseUser.getCurrentUser(), true);
        acl.setReadAccess(ParseUser.getCurrentUser(), true);
        followActivity.setACL(acl);
        followActivity.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    Log.e("Log", "Follow submitted");
                }
            }
        });
    }

    public static void deleteFollow() {
        Log.e("Log", " delete follow");

        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> followQueryDelete = new ParseQuery<ParseObject>("Notification");
        followQueryDelete.whereMatches("type", "follow");
        followQueryDelete.whereEqualTo("fromUser", currentUser);
        followQueryDelete.whereEqualTo("toUser", User);
        followQueryDelete.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    Log.i("FollowDelete", "Download Follow Failed");

                } else {
                    Log.i("FollowDelete", "Deleting Follow success");
                    try {
                        object.delete();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                        Log.i("", "Error");
                    }
                }
            }
        });
    }
}


