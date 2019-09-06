//package codelight.social_network.Adapters;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.parse.CountCallback;
//import com.parse.GetDataCallback;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseImageView;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//import com.parse.ParseQueryAdapter;
//import com.parse.ParseUser;
//
//import java.util.Arrays;
//import java.util.Date;
//
//import codelight.social_network.Activity.ProfileActivity;
//import codelight.social_network.ParseModels.Activity;
//import codelight.social_network.ParseModels.Post;
//import lux.socialnetwork.R;
//
//public class ProfileAdapter extends ParseQueryAdapter<Post> {
//
//    private TextView LikesNum;
//    private TextView CommentsNum;
//    private LinearLayout LikeBtn;
//    private LinearLayout CommentBtn;
//    private LinearLayout ShareButton;
//    private ImageView ImageLike;
//    //CharSequence LikeButtonText;
//    ParseImageView ProfilePicView;
//    TextView UsernameView;
//    TextView TimeStamp;
//    ParseImageView PhotoView;
//    ParseFile thumbnailFile;
//    ParseUser PhotoUser;
//
//    public static Post post;
//
//    public long elapsedYears;
//    public long elapsedMonths;
//    public long elapsedWeeks;
//    public long elapsedDays ;
//    public long elapsedHours ;
//    public long elapsedMinutes;
//    public long elapsedSeconds;
//
//
//    String FinalTimeStamp;
//
//
//    public ProfileAdapter(Context context) {
//
//        super(context, new ParseQueryAdapter.QueryFactory<Post>() {
//
//            public ParseQuery create() {
//
//                String PhotoId = ProfileActivity.getPhotoId();
//                String UserId = ProfileActivity.getUserId();
//                ParseUser ProfileUser;
//
//                if(PhotoId == null){
//                    ProfileUser = ProfileActivity.getUserWithUser(UserId);
//                }
//                else{
//                    ProfileUser = ProfileActivity.getUserWithPhoto(PhotoId);
//                }
//                // Get the the ProfileUser's photos
//                ParseQuery<Post> photosFromCurrentUserQuery = new ParseQuery<Post>("Post");
//                photosFromCurrentUserQuery.whereEqualTo("user", ProfileUser);
//                photosFromCurrentUserQuery.whereExists("image");
//
//                // We create a final compound query that will find all of the photos that were
//                // taken by the user's friends or by the user
//                ParseQuery<Post> query = ParseQuery.or(Arrays.asList(photosFromCurrentUserQuery));
//                query.include("user");
//                query.orderByDescending("createdAt");
////                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
//
//                return query;
//            }
//        });
//
//    }
//
//    //@Override
//    public View getItemView(final Post pPost, View v, ViewGroup parent) {
//        if (v == null) {
//            v = View.inflate(getContext(), R.layout.main_item, null);
//        }
//        super.getItemView(pPost, v, parent);
//
////        ParseObject.registerSubclass(Activity.class);
//        //int position = v.getVerticalScrollbarPosition();
//
//        //Log.e("OldPostListAdapter", "Loading Picture Position:::  " + position);
//
//
//
//        ParseUser currentUser = ParseUser.getCurrentUser();
//
//        PhotoUser = pPost.getUser();
//
//        //UI
//        UsernameView = (TextView) v.findViewById(R.id.user_name);
//        TimeStamp = (TextView) v.findViewById(R.id.timestamp);
//        ProfilePicView = (ParseImageView) v.findViewById(R.id.user_thumbnail);
//
//        PhotoView = (ParseImageView) v.findViewById(R.id.photo);
//
//        LikesNum = (TextView) v.findViewById(R.id.like_number);
//        CommentsNum = (TextView) v.findViewById(R.id.comment_number);
//
//        LikeBtn = (LinearLayout) v.findViewById(R.id.LayoutLike);
//        LikeBtn.setEnabled(false);
//        LikeBtn.setClickable(false);
//        ImageLike = (ImageView) v.findViewById(R.id.imgLike);
//        CommentBtn = (LinearLayout) v.findViewById(R.id.LayoutComment);
//        ShareButton = (LinearLayout) v.findViewById(R.id.LayoutShare);
//
//
//        final ParseUser PhotoUser = pPost.getUser();
//
//        //Set username
//        UsernameView.setText((String) PhotoUser.get("displayName"));
//
//        //Set Timestamp
//        Date then = pPost.getCreatedAt();
//        Date now = new Date();
//        now.getTime();
//        TimeStamp.setText(Timestamp(then, now));
//        TimeStamp.setGravity(Gravity.RIGHT);
//
//
//
//        thumbnailFile = PhotoUser.getParseFile("profilePictureSmall");
//        if (thumbnailFile != null) {
//            ProfilePicView.setParseFile(thumbnailFile);
//            ProfilePicView.loadInBackground(new GetDataCallback() {
//                @Override
//                public void done(byte[] data, ParseException e) {
//                    if(e == null){
//
//                    }
//                    else {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//        else {
//
//            ProfilePicView.setImageResource(R.drawable.avatarplaceholderprofile);
//        }
//
//        //Set Post
//        PhotoView = (ParseImageView) v.findViewById(R.id.photo);
//        PhotoView.setPlaceholder(getContext().getResources().getDrawable(R.drawable.placeholder_photo));
//        ParseFile photoFile = pPost.getImage();
//        PhotoView.setParseFile(photoFile);
//        PhotoView.loadInBackground();
//        PhotoView.setClickable(true);
//        PhotoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToComments(v, pPost);
//
//            }
//        });
//
//
//
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
//                        LikesNum.setText("0 Likes");
//                    }
//                    if (count == 1)
//                        LikesNum.setText(count + "Like");
//                    else {
//                        LikesNum.setText(count + "Likes");
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
//        //Set CommentsNumber
//        ParseQuery<Activity> photoComments = new ParseQuery<Activity>("Activity");
//        photoComments.whereEqualTo("post", pPost);
//        photoComments.whereEqualTo("type", "comment");
//        photoComments.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
//        photoComments.countInBackground(new CountCallback() {
//            public void done(int count, ParseException e) {
//                if (e == null) {
//                    if (count == 0) {
//                        CommentsNum.setText("0 Comments");
//                    }
//                    if (count == 1)
//                        CommentsNum.setText(count + "Comment");
//                    else {
//                        CommentsNum.setText(count + "Comments");
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
//        ParseQuery<Activity> UserPhotoLike = new ParseQuery<Activity>("Activity");
//        UserPhotoLike.whereEqualTo("post", pPost);
//        UserPhotoLike.whereEqualTo("type", "like");
//        UserPhotoLike.whereEqualTo("fromUser", currentUser);
//        UserPhotoLike.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
//        UserPhotoLike.countInBackground(new CountCallback() {
//            public void done(int count, ParseException e) {
//                if (e == null) {
//                    if(count == 0){
//                        ImageLike.setImageResource(R.drawable.buttonlike);
//                    }
//                    else{
//                        ImageLike.setImageResource(R.drawable.buttonlikeselected);
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
//        CommentBtn.setClickable(true);
//        CommentBtn.isEnabled();
//        CommentBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToComments(v, pPost);
//
//            }
//        });
//
//
//        ShareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareImage(v, pPost);
//
//            }
//
//        });
//
//        post = pPost;
//        return v;
//    }
//
//
//    private void shareImage(View v, Post pPost) {
//
//        String url = pPost.getImage().getUrl();
//        Log.d("Log", "ULR = " + url);
//        Uri uri =  Uri.parse(url);
//
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("image/*");
//
//        intent.putExtra(Intent.EXTRA_TEXT, "example");
//        intent.putExtra(Intent.EXTRA_TITLE, "example");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "example");
//        intent.putExtra(Intent.EXTRA_STREAM, uri);
//
//        Intent openInChooser = new Intent(intent);
//        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, uri);
//        v.getContext().startActivity(openInChooser);
//
//        /*
//        Intent share = new Intent(Intent.ACTION_SEND);
//
//        // If you want to share a png image only, you can do:
//        // setType("image/png"); OR for jpeg: setType("image/jpeg");
//        share.setType("image/*");
//
//        // Make sure you put example png image named myImage.png in your
//        // directory
//        String imagePath = Environment.getExternalStorageDirectory()
//                + "/buttonlike.png;
//
//        File imageFileToShare = new File(pPost);
//
//        Uri uri = Uri.fromFile(imageFileToShare);
//        share.putExtra(Intent.EXTRA_STREAM, uri);
//
//        v.getContext().startActivity(Intent.createChooser(share, "Share Image!"));*/
//    }
//
//
//    public void goToComments(View v, Post pPost){
////        String Extra = pPost.getObjectId();
////        Intent myIntent = new Intent(v.getContext(), CommentActivity.class);
////        myIntent.putExtra( "ID", pPost.getUser().getObjectId() );
////        myIntent.putExtra( "post", Extra );
////        myIntent.putExtra("displayName", (String) pPost.getUser().get("displayName"));
////        v.getContext().startActivity(myIntent);
//        Log.i("Log", "Go to Comments");
//    }
//
//
//
//
//    public String Timestamp(Date startDate, Date endDate) {
//
//        //milliseconds
//        long different = endDate.getTime() - startDate.getTime();
//
//
//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;
//        long weeksInMilli = daysInMilli * 7;
//        long monthsInMilli = daysInMilli * 30;
//        long yearsInMilli = monthsInMilli * 12;
//
//
//        elapsedYears = different / yearsInMilli;
//        different = different % yearsInMilli;
//
//        elapsedMonths = different / monthsInMilli;
//        different = different % monthsInMilli;
//
//        elapsedWeeks = different / weeksInMilli;
//        different = different % weeksInMilli;
//
//        elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;
//
//        elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        elapsedSeconds = different / secondsInMilli;
//
//
//        if (elapsedYears == 0 && elapsedMonths == 0 && elapsedWeeks == 0 && elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes == 0) {
//            //show only Seconds
//            FinalTimeStamp = "" + elapsedSeconds + "s";
//        }
//        else if (elapsedYears == 0 && elapsedMonths == 0 && elapsedWeeks == 0 && elapsedDays == 0 && elapsedHours == 0) {
//            //show only Minutes
//            FinalTimeStamp = "" + elapsedMinutes + "m";
//        }
//        else if (elapsedYears == 0 && elapsedMonths == 0 && elapsedWeeks == 0 && elapsedDays == 0){
//            //show only Hours
//            FinalTimeStamp = "" + elapsedHours + "h";
//        }
//
//        else if (elapsedYears == 0 && elapsedMonths == 0 && elapsedWeeks == 0){
//            //show only Days
//            FinalTimeStamp = "" + elapsedDays + "d";
//        }
//
//        else if (elapsedYears == 0 && elapsedMonths == 0){
//            //show only Weeks
//            if(elapsedWeeks <= 1){
//                FinalTimeStamp = "" + elapsedWeeks + "wk";
//            }
//            else {
//                FinalTimeStamp = "" + elapsedWeeks + "wks";
//            }
//        }
//
//        else if (elapsedYears == 0){
//            //show only Months
//            if(elapsedMonths <= 1) {
//                FinalTimeStamp = "" + elapsedMonths + "mo";
//            }
//            else{
//                FinalTimeStamp = "" + elapsedMonths + "mos";
//            }
//        }
//
//        else {
//            //show only Years
//            if(elapsedYears <= 1) {
//                FinalTimeStamp = "" + elapsedYears + "y";
//            }
//            else{
//                FinalTimeStamp = "" + elapsedYears + "ys";
//            }
//        }
//
//        return FinalTimeStamp;
//    }
//
//
//
//}
//
//
//
