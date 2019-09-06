//package codelight.social_network.Adapters;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ListView;
//
//import com.parse.CountCallback;
//import com.parse.GetCallback;
//import com.parse.ParseACL;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseImageView;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//import com.parse.ParseQueryAdapter;
//import com.parse.ParseUser;
//import com.parse.SaveCallback;
//import com.squareup.picasso.Picasso;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.Arrays;
//
//import codelight.social_network.CustomViews.CustomFontTextView;
//import codelight.social_network.Interfaces.PostListAdapterCallBack;
//import codelight.social_network.ParseModels.Activity;
//import codelight.social_network.ParseModels.Post;
//import codelight.social_network.Utils.UtilFunctions;
//import de.hdodenhof.circleimageview.CircleImageView;
//import lux.socialnetwork.R;
//
///**
// * Created by Yannick Erpelding on 30.07.15.
// */
//
//
//public class OldPostListAdapter extends ParseQueryAdapter<Post> {
//
//    public static Post post;
//    public static boolean likeAction = true;
//    //    private LinearLayout LikeBtn;
////    private LinearLayout CommentBtn;
////    private LinearLayout ShareButton;
////    private static ImageView ImageLike;
////    ParseImageView profileImageView;
//    ParseImageView postImageView;
//    ParseFile thumbnailFile;
//    ParseUser postUser;
//    private CustomFontTextView txtLikesNumber, txtCommentsNumber, txtUserName, txtTimeStamp, txtFullName;
//    private CircleImageView profileImageView;
//    private Uri.Builder builder;
//    private Context context;
//    private PostListAdapterCallBack callBack;
//
//    public OldPostListAdapter(Context context, PostListAdapterCallBack callBack) {
//        super(context, new ParseQueryAdapter.QueryFactory<Post>() {
//            public ParseQuery create() {
//
//
//                // First, query for the friends whom the current user follows
//                ParseQuery<Activity> followingActivitiesQuery = new ParseQuery<Activity>("Activity");
//                followingActivitiesQuery.whereMatches("type", "follow");
//                followingActivitiesQuery.whereEqualTo("fromUser", ParseUser.getCurrentUser());
//
//                // Get the photos from the Users returned in the previous query
//                ParseQuery<Post> photosFromFollowedUsersQuery = new ParseQuery<Post>("Post");
//                photosFromFollowedUsersQuery.whereMatchesKeyInQuery("user", "toUser", followingActivitiesQuery);
//                photosFromFollowedUsersQuery.whereExists("image");
//
//                // Get the current user's photos
//                ParseQuery<Post> photosFromCurrentUserQuery = new ParseQuery<Post>("Post");
//                photosFromCurrentUserQuery.whereEqualTo("user", ParseUser.getCurrentUser());
//                photosFromCurrentUserQuery.whereExists("image");
//
//                // We create a final compound query that will find all of the photos that were
//                // taken by the user's friends or by the user
//                ParseQuery<Post> query = ParseQuery.or(Arrays.asList(photosFromFollowedUsersQuery, photosFromCurrentUserQuery));
//                query.include("user");
//                query.orderByDescending("createdAt");
//                //query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
//
//                return query;
//            }
//        });
//        this.context = context;
//        this.callBack = callBack;
//    }
//
//    public static void likeButton(int position, ListView list) {
////        View v = list.getChildAt(position -
////                list.getFirstVisiblePosition());
////        int like;
////        if(v == null)
////            return;
////        if(likeAction == true){
////            LikesNum = (TextView) v.findViewById(R.id.like_number);
////
////            String likeText = LikesNum.getText().toString();
////            int likeCount = Integer.parseInt(likeText.replaceAll("[\\D]", ""));
////
////            ImageLike = (ImageView) v.findViewById(R.id.imgLike);
////            Object tag = ImageLike.getTag();
////            like = R.drawable.buttonlike;
////
////            if( tag != null && ((Integer)tag).intValue() == like) {
////                //If LikeButton is gray
////                like = R.drawable.buttonlikeselected;
////                submitLike(ImageLike);
////                likeCount++;
////            }
////            else if ( tag != null && ((Integer)tag).intValue() != like) {
////                //If LikeButton is red
////                like = R.drawable.buttonlike;
////                deleteLike();
////                likeCount = likeCount - 1;
////            }
////            else {
////                //If no resource can be found
////                like = R.drawable.buttonlikeselected;
////                submitLike(ImageLike);
////                likeCount++;
////            }
////
////            if (likeCount == 1)
////                LikesNum.setText(likeCount + "Like");
////            else {
////                LikesNum.setText(likeCount + "Likes");
////            }
////
////            ImageLike.setTag(like);
////            ImageLike.setImageResource(like);
////
////            Log.e("Log", "Like-Button clicked");
////
////            //We don't want any button spam, so we have to disable the action a few seconds
////            likeAction = false;
////            final Handler handler = new Handler();
////            handler.postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    // Do something after 3s = 3000ms
////                    likeAction = true;
////                }
////            }, 2000);
////        }
//    }
//
//    public static void deleteLike() {
//        ParseUser currentUser = ParseUser.getCurrentUser();
//
//        ParseQuery<ParseObject> LikeQueryDelete = ParseQuery.getQuery("Activity");
//        LikeQueryDelete.whereEqualTo("post", post);
//        LikeQueryDelete.whereEqualTo("type", Activity.LIKE);
//        LikeQueryDelete.whereEqualTo("fromUser", currentUser);
//        LikeQueryDelete.getFirstInBackground(new GetCallback<ParseObject>() {
//            public void done(ParseObject object, ParseException e) {
//                if (e != null) {
//                    e.printStackTrace();
//                    Log.i("LikeDelete", "Download Like Failed");
//
//                } else {
//                    try {
//                        object.delete();
//                    } catch (ParseException e1) {
//                        e1.printStackTrace();
//                        Log.i("", "Error");
//                    }
//                }
//            }
//        });
//
//    }
//
//    private static void submitLike(ImageView Like) {
//        ParseUser currentUser = ParseUser.getCurrentUser();
//
//        Activity comment = new Activity();
//        comment.setToUser(post.getUser());
//        comment.setFromUser(currentUser);
//        comment.setType(Activity.LIKE);
//        comment.setType("like");
//        comment.setPhoto(post);
//        ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
//        acl.setPublicReadAccess(true);
//        comment.setACL(acl);
//        comment.saveInBackground(new SaveCallback() {
//            public void done(ParseException e) {
//                if (e != null) {
//                    e.printStackTrace();
//                } else {
//                    Log.d("Log", "Like submitted");
//                }
//            }
//        });
//
//        likeAnimation();
//
//    }
//
//    private static void likeAnimation() {
////        OvershootInterpolator overshoot = null;
////        ImageLike.setVisibility(View.VISIBLE);
////
////        ImageLike.setScaleY(3.1f);
////        ImageLike.setScaleX(3.1f);
////
////        AnimatorSet animatorSet = new AnimatorSet();
////
////        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(ImageLike, "scaleX", 0.2f, 1f);
////        bounceAnimX.setDuration(150);
////        bounceAnimX.setInterpolator(overshoot);
////
////        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(ImageLike, "scaleY", 0.2f, 1f);
////        bounceAnimY.setDuration(150);
////        bounceAnimY.setInterpolator(overshoot);
////        bounceAnimY.addListener(new AnimatorListenerAdapter() {
////            @Override
////            public void onAnimationStart(Animator animation) {
////            }
////        });
////
////        animatorSet.play(bounceAnimX).with(bounceAnimY);
////        animatorSet.start();
//    }
//
//    //@Override
//    public View getItemView(final Post pPost, View v, ViewGroup parent) {
//        if (v == null) {
//            v = View.inflate(getContext(), R.layout.home_post_item, null);
//        }
//        super.getItemView(pPost, v, parent);
//
////        ParseObject.registerSubclass(Activity.class);
//
//        postUser = pPost.getUser();
//
//        //UI
//        txtUserName = v.findViewById(R.id.text_post_item_username);
//        txtFullName = v.findViewById(R.id.text_post_item_full_name);
//        txtTimeStamp = v.findViewById(R.id.text_post_item_time_ago);
//        profileImageView = v.findViewById(R.id.image_post_item_profile);
////        ProfilePicView = (ParseImageView) v.findViewById(R.id.user_thumbnail);
//
//        postImageView = v.findViewById(R.id.image_post_item_post);
//
//        txtLikesNumber = v.findViewById(R.id.text_post_item_likes);
//        txtCommentsNumber = v.findViewById(R.id.text_post_item_comments);
//
////        LikeBtn = (LinearLayout) v.findViewById(R.id.LayoutLike);
////        LikeBtn.setEnabled(false);
////        LikeBtn.setClickable(false);
////        ImageLike = (ImageView) v.findViewById(R.id.imgLike);
////        CommentBtn = (LinearLayout) v.findViewById(R.id.LayoutComment);
////        ShareButton = (LinearLayout) v.findViewById(R.id.LayoutShare);
//        ParseUser photoUser = pPost.getUser();
//
//        //Set username
//        txtUserName.setText("@" + photoUser.get("usernameFix").toString());
//        txtFullName.setText(photoUser.get("displayName").toString());
//        txtUserName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToProfile(pPost);
//            }
//        });
//
//        txtTimeStamp.setText(UtilFunctions.getTimeStamp(pPost.getCreatedAt()));
//
//        //Set ProfilePic
////        profileImageView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                goToProfile(v, pPost);
////
////            }
////        });
//        thumbnailFile = photoUser.getParseFile("profilePictureSmall");
//        if (thumbnailFile != null) {
//            Picasso.with(context).load(thumbnailFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(profileImageView);
//        }
//
//        //Set Post
//        postImageView.setPlaceholder(getContext().getResources().getDrawable(R.drawable.placeholder_photo));
//        ParseFile photoFile = pPost.getImage();
//        postImageView.setParseFile(photoFile);
//        postImageView.loadInBackground();
//        postImageView.setClickable(true);
//        postImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToComments(pPost);
//            }
//        });
//
//        //Set LikesNumber
//        ParseQuery<Activity> photoLikes = new ParseQuery<Activity>("Activity");
//        photoLikes.whereEqualTo("post", pPost);
//        photoLikes.whereEqualTo("type", "like");
//        photoLikes.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
//        photoLikes.countInBackground(new CountCallback() {
//            public void done(int count, ParseException e) {
//                if (e == null) {
//                    if (count == 0) {
//                        txtLikesNumber.setText("0 Likes");
//                    }
//                    if (count == 1)
//                        txtLikesNumber.setText(count + " Like");
//                    else {
//                        txtLikesNumber.setText(count + " Likes");
//                    }
//                } else {
//                    e.printStackTrace();
//
//                }
//            }
//
//        });
//
//        //Set CommentsNumber
//        ParseQuery<Activity> photoComments = new ParseQuery<Activity>("Activity");
//        photoComments.whereEqualTo("post", pPost);
//        photoComments.whereEqualTo("type", "comment");
//        photoComments.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
//        photoComments.countInBackground(new CountCallback() {
//            public void done(int count, ParseException e) {
//                if (e == null) {
//                    if (count == 0) {
//                        txtCommentsNumber.setText("0 Comments");
//                    }
//                    if (count == 1)
//                        txtCommentsNumber.setText(count + " Comment");
//                    else {
//                        txtCommentsNumber.setText(count + " Comments");
//                    }
//                } else {
//                    e.printStackTrace();
//
//                }
//            }
//
//        });
//
//
//        //See if CurrentUser has already liked the picture
////        ParseQuery<Activity> UserPhotoLike = new ParseQuery<Activity>("Activity");
////        UserPhotoLike.whereEqualTo("post", pPost);
////        UserPhotoLike.whereEqualTo("type", "like");
////        UserPhotoLike.whereEqualTo("fromUser", currentUser);
////        UserPhotoLike.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
////        UserPhotoLike.countInBackground(new CountCallback() {
////            public void done(int count, ParseException e) {
////                if (e == null) {
////                    if(count == 0){
////                        ImageLike.setImageResource(R.drawable.buttonlike);
////                    }
////                    else{
////                        ImageLike.setImageResource(R.drawable.buttonlikeselected);
////                    }
////                } else {
////                    e.printStackTrace();
////
////                }
////            }
////
////        });
//
//
////        CommentBtn.setClickable(true);
////        CommentBtn.isEnabled();
////        CommentBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                goToComments(v, pPost);
////
////            }
////        });
////
////
////        ShareButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                shareImage(v, pPost);
////
////            }
////
////        });
//
//        post = pPost;
//        return v;
//    }
//
//    public Uri getUriFromUrl(String PUrl) {
//        URL url = null;
//        try {
//            url = new URL(PUrl);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        builder = new Uri.Builder()
//                .scheme(url.getProtocol())
//                .authority(url.getAuthority())
//                .appendPath(url.getPath());
//        return builder.build();
//    }
//
//    private void shareImage(View v, Post pPost) {
//
//        String PUrl = pPost.getImage().getUrl();
//        //Log.e("Log", "ULR = " + PUrl);
//
//        Uri uri = getUriFromUrl(PUrl);
//
//        if (uri == null) {
//            Log.e("Log", "Uri = " + uri);
//            Log.e("Log", "Uri = null");
//        }
//
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("image/*");
//
//        intent.putExtra(Intent.EXTRA_TEXT, "Yay! An awesome post from Social Network.");
//        intent.putExtra(Intent.EXTRA_TITLE, "Share this image");
//        intent.putExtra(Intent.EXTRA_STREAM, uri);
//
//        Intent openInChooser = new Intent(intent);
//        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, uri);
//        v.getContext().startActivity(openInChooser);
//
//    }
//
//
//    public void goToComments(Post pPost) {
////        String Extra = pPost.getObjectId();
////        Intent myIntent = new Intent(v.getContext(), CommentActivity.class);
////        myIntent.putExtra( "ID", pPost.getUser().getObjectId() );
////        myIntent.putExtra( "post", Extra );
////        myIntent.putExtra("displayName", (String) pPost.getUser().get("displayName"));
////        v.getContext().startActivity(myIntent);
////        Log.i("Log", "Go to Comments");
////        FragmentManager fragmentManager = getFragmentManager();
////        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////        fragmentTransaction.replace(R.id.fragment_container, PostDetailFragment);
////        fragmentTransaction.commit();
//        callBack.onClickComment(pPost);
//    }
//
//
//    public void goToProfile(Post pPost) {
////        Intent myIntent = new Intent(v.getContext(), ProfileActivity.class);
////        myIntent.putExtra("PhotoID", pPost.getObjectId());
////        myIntent.putExtra("UserID", pPost.getUser().toString());
////        v.getContext().startActivity(myIntent);
////        Log.i("Log", "Go to Profile");
//        callBack.onClickToProfile(pPost);
//    }
//}
//
//
