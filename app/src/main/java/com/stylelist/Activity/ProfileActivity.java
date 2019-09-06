//package codelight.social_network.Activity;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.parse.CountCallback;
//import com.parse.GetCallback;
//import com.parse.GetDataCallback;
//import com.parse.ParseACL;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseImageView;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//import com.parse.ParseUser;
//import com.parse.SaveCallback;
//import com.readystatesoftware.systembartint.SystemBarTintManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import codelight.social_network.ParseModels.Activity;
//import codelight.social_network.ParseModels.Post;
//import lux.socialnetwork.R;
//
////import codelight.social_network.Adapters.ProfileAdapter;
//
//public class ProfileActivity extends android.app.Activity {
//
//    private static final int MAX_ROWS = 50;
//    private int lastTopValue = 0;
//
//    private List<String> modelList = new ArrayList<>();
//    private ListView listView;
//    private ParseImageView Title;
////    private ProfileAdapter adapter;
//
//
//    private Button EditButton;
//    private Button FollowButton;
//    private TextView LikesNum;
//    private TextView Info;
//    private TextView PhotoNum;
//    private TextView FollowerNum;
//    private TextView FollowingNum;
//    private TextView Username;
//    private TextView Mention;
//    private ParseImageView ProfilePicture;
//    ParseFile thumbnailFile;
//    ParseFile TitleFile;
//
//    public static ParseUser PhotoUser;
//    public static ParseUser ProfileUser;
//
//    public byte[] data;
//    public static String PhotoID;
//    public static String UserID;
//
//
//    public String PUsername;
//    public String CUsername;
//
//    public static boolean FollowAction = true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//        listView = (ListView) findViewById(R.id.list);
//
////        ActionBar bar = getActionBar();
////        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2B85D3")));
////        bar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#2B85D3")));
////        bar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Profil</font>"));//
//
//
//        //Set statusBar blue
//       SystemBarTintManager mTintManager = new SystemBarTintManager(this);
//        mTintManager.setStatusBarTintEnabled(true);
//        mTintManager.setTintColor(Color.parseColor("#2777BE"));
//
//       PhotoID = getIntent().getStringExtra("PhotoID");
//        UserID = getIntent().getStringExtra("UserID");
//        Log.e("Profile","UserId: " + UserID + "PhotoId: "+ PhotoID);
//
//
//
//        // inflate custom header and attach it to the list
//        LayoutInflater inflater = getLayoutInflater();
//        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.fragment_profile_header, listView, false);
//        listView.addHeaderView(header, null, false);
////        adapter = new ProfileAdapter(this);
////        if(adapter != null) {
////            listView.setAdapter(adapter);
////        }
//
//
//        FollowButton = (Button) header.findViewById(R.id.FollowButton);
//        EditButton = (Button) header.findViewById(R.id.EditButton);
//        Title = (ParseImageView) header.findViewById(R.id.TitleImage);
//        ProfilePicture = (ParseImageView) findViewById(R.id.user_thumbnail);
//        PhotoNum = (TextView) header.findViewById(R.id.Photo_number);
//        Username = (TextView) header.findViewById(R.id.Displayname_user);
//        Mention = (TextView) header.findViewById(R.id.Mention);
//        Info = (TextView) header.findViewById(R.id.Info_text);
//        FollowerNum = (TextView) header.findViewById(R.id.Follower_number);
//        FollowingNum = (TextView) header.findViewById(R.id.Following_number);
//
//        FollowButton.setVisibility(View.GONE);
//        EditButton.setVisibility(View.GONE);
//
//
//        Log.e("Profile", "PhotoID: " + PhotoID);
//        Log.e("Profile", "UserID: " + UserID);
//        if (PhotoID == null){
//            ProfileUser = getUserWithUser(UserID);
//        }
//        else{
//            ProfileUser = getUserWithPhoto(PhotoID);
//        }
//
//
//    try {
//        PUsername = ProfileUser.fetchIfNeeded().get("displayName").toString();
//        CUsername = ParseUser.getCurrentUser().fetchIfNeeded().get("displayName").toString();
//    } catch (ParseException e) {
//        e.printStackTrace();
//    }
//
//
//
//        Log.i("Profile", "Usercompare:___ ProfileUser: " + PUsername + " CurrentUser: " + CUsername);
//
//        if(PUsername.equals(CUsername)) {
//            EditButton.setVisibility(View.VISIBLE);
//            FollowButton.setVisibility(View.INVISIBLE);
//            Log.i("Profile", "This user");
//            EditButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    gotToProfileEditActivity(v);
//
//                }
//            });
//
//        }
//        else{
//            Log.i("Profile", "Other user");
//            FollowButton.setVisibility(View.VISIBLE);
//            EditButton.setVisibility(View.GONE);
//            final ParseQuery<Post> FollowQuery = ParseQuery.getQuery("Activity");
//            FollowQuery.whereEqualTo("fromUser", ProfileUser);
//            FollowQuery.whereEqualTo("toUser", ParseUser.getCurrentUser());
//            FollowQuery.whereEqualTo("type", "follow");
//            FollowQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
//            FollowQuery.countInBackground(new CountCallback() {
//                public void done(int count, ParseException e) {
//                    if (e == null) {
//                        if (count >= 0) {
//                            FollowButton.setText("Following");
//                            FollowButton.setTextColor(Color.parseColor("#FFFFFF"));
//                            FollowButton.setBackgroundResource(R.drawable.rounded_corners_following);
//                        } else {
//                            FollowButton.setText("Followen");
//                            FollowButton.setTextColor(Color.parseColor("#000000"));
//                            FollowButton.setBackgroundResource(R.drawable.rounded_corners_followen);
//                        }
//                    } else {
//                        e.printStackTrace();
//                    }
//                }
//
//            });
//            FollowButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//
//
//                }
//            });
//        }
//
//
//        try {
//            thumbnailFile = ProfileUser.fetchIfNeeded().getParseFile("profilePictureMedium");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (thumbnailFile != null) {
//            ProfilePicture.setParseFile(thumbnailFile);
//            ProfilePicture.loadInBackground(new GetDataCallback() {
//                @Override
//                public void done(byte[] data, ParseException e) {
//                    if (e == null) {
//
//                    } else {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
//            ProfilePicture.setImageResource(R.drawable.avatarplaceholderprofile);
//        }
//
//        TitleFile = ProfileUser.getParseFile("headerPictureMedium");
//        if (TitleFile != null) {
//            Title.setParseFile(TitleFile);
//            Title.loadInBackground(new GetDataCallback() {
//                @Override
//                public void done(byte[] data, ParseException e) {
//                    if (e == null) {
//
//                    } else {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
//            //Title.setImageResource(R.drawable.avatarplaceholderprofile);
//        }
//
//
//        //Get Username
//        Username.setText(ProfileUser.get("displayName").toString());
//
//        //Get Mention
////        Mention.setText("@" + ProfileUser.get("usernameFix").toString());
//
//        //Get UserInfo
//        if (ProfileUser.get("UserInfo") != null){
//            Info.setText(ProfileUser.get("UserInfo").toString());
//
//        }
//
//        //Get PhotoNumber
//        ParseQuery<Post> TotalLikeQuery = ParseQuery.getQuery("Post");
//        TotalLikeQuery.whereEqualTo("user", ProfileUser);
//        TotalLikeQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
//        TotalLikeQuery.countInBackground(new CountCallback() {
//            public void done(int count, ParseException e) {
//                if (e == null) {
//                    PhotoNum.setText(count + " Photos");
//                    //	Log.i(SocialNetworkApplication.TAG, "Set PhotoNumber Success");
//                    //	Log.i(SocialNetworkApplication.TAG, "Fotoen:" + count);
//                } else {
//                    e.printStackTrace();
//                    //	Log.i(SocialNetworkApplication.TAG, "Set PhotoNumber Error");
//                    //	Log.i(SocialNetworkApplication.TAG, "Fotoen:" + count);
//                    PhotoNum.setText("0 Photos");
//                }
//            }
//
//        });
//
//        //Get FollowerNumber
//        ParseQuery<Post> FollowerQuery = ParseQuery.getQuery("Activity");
//        FollowerQuery.whereEqualTo("toUser", ProfileUser);
//        FollowerQuery.whereEqualTo("type", "follow");
//        FollowerQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
//        FollowerQuery.countInBackground(new CountCallback() {
//            public void done(int count, ParseException e) {
//                if (e == null) {
//                    FollowerNum.setText(count + " Followers");
//                    Log.i("Profile", "Set FollowerNumber Success, Follower:" + count);
//                    //	Log.i(SocialNetworkApplication.TAG, "Fotoen:" + count);
//                } else {
//                    e.printStackTrace();
//                    Log.i("Profile", "Set FollowerNumber Error");
//                    //	Log.i(SocialNetworkApplication.TAG, "Fotoen:" + count);
//                    FollowerNum.setText("0 Followers");
//                }
//            }
//
//        });
//
//        //Get FollowingNumber
//        ParseQuery<Post> FollowingQuery = ParseQuery.getQuery("Activity");
//        FollowingQuery.whereEqualTo("fromUser", ProfileUser);
//        FollowingQuery.whereEqualTo("type", "follow");
//        FollowingQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
//        FollowingQuery.countInBackground(new CountCallback() {
//            public void done(int count, ParseException e) {
//                if (e == null) {
//                    FollowingNum.setText(count + " Following");
//                    Log.i("Profile", "Set FollowingNumber Success, Following:" + count);
//                    //	Log.i(SocialNetworkApplication.TAG, "Fotoen:" + count);
//                } else {
//                    e.printStackTrace();
//                    Log.i("Profile", "Set FollowingNumber Error");
//                    //	Log.i(SocialNetworkApplication.TAG, "Fotoen:" + count);
//                    FollowingNum.setText("0 Following");
//                }
//            }
//
//        });
//
//
//        FollowButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("Log", "Follow clicked");
//                Log.e("Log", "FollowAction:" + FollowAction);
//                if (FollowAction == true) {
//
//                    if (FollowButton.getText().equals("Follow")) {
//                        FollowButton.setText("Following");
//                        FollowButton.setTextColor(Color.parseColor("#FFFFFF"));
//                        FollowButton.setBackgroundResource(R.drawable.rounded_corners_following);
//                        Log.d("Log", "1: submit follow follow");
//
//                        submitFollow();
//                    } else if (FollowButton.getText().equals("Following")) {
//                        FollowButton.setText("Follow");
//                        FollowButton.setTextColor(Color.parseColor("#000000"));
//                        FollowButton.setBackgroundResource(R.drawable.rounded_corners_followen);
//                        deleteFollow();
//                        Log.d("Log", "1: delete follow");
//
//                    }
//
//                    int duration = Toast.LENGTH_SHORT;
//                    Toast toast = Toast.makeText(getApplicationContext(), "Now you follow " + ProfileUser.get("displayName"), duration);
//                    toast.show();
//
//                    //We don't want any button spam, so we have to disable the action a few seconds
//                    FollowAction = false;
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            // Do something after 3s = 3000ms
//                            FollowAction = true;
//                        }
//                    }, 2000);
//                }
//            }
//        });
//
//
//    }
//
//
//
//    private static void submitFollow() {
//        ParseUser currentUser = ParseUser.getCurrentUser();
//
//        Activity followActivity = new Activity();
//        followActivity.setFromUser(currentUser);
//        followActivity.setToUser(ProfileUser);
//        followActivity.setType("follow");
//        ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
//        acl.setPublicReadAccess(true);
//        acl.setPublicWriteAccess(true);
//        acl.setWriteAccess(ParseUser.getCurrentUser(), true);
//        acl.setReadAccess(ParseUser.getCurrentUser(), true);
//        followActivity.setACL(acl);
//        followActivity.saveInBackground(new SaveCallback() {
//            public void done(ParseException e) {
//                if (e != null) {
//                    e.printStackTrace();
//                } else {
//                    Log.e("Log", "Follow submitted");
//                }
//            }
//        });
//    }
//
//    public static void deleteFollow() {
//        Log.d("Log", " delete follow");
//
//        ParseUser currentUser = ParseUser.getCurrentUser();
//
//        ParseQuery<ParseObject> FollowQueryDelete = new ParseQuery<ParseObject>("Activity");
//        FollowQueryDelete.whereMatches("type", "follow");
//        FollowQueryDelete.whereEqualTo("fromUser", currentUser);
//        FollowQueryDelete.whereEqualTo("toUser", ProfileUser);
//        FollowQueryDelete.getFirstInBackground(new GetCallback<ParseObject>() {
//            public void done(ParseObject object, ParseException e) {
//                if (e != null) {
//                    e.printStackTrace();
//                    Log.i("LikeDelete", "Download Follow Failed");
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
//    }
//
//    private void gotToProfileEditActivity(View v) {
//        Intent myIntent = new Intent(v.getContext(), ProfileEditActivity.class);
//        v.getContext().startActivity(myIntent);
//        Log.i("Log", "Go to ProfileEdit");
//    }
//
//    @Override
//    public void onBackPressed() {
//        goToMainActivity();
//    }
//
//    private void goToMainActivity() {
////        Intent Intent = new Intent(this, OldMainActivity.class);
////        startActivity(Intent);
////        finish(); // This closes the login screen so it's not on the back stack
//    }
//
//    public static ParseUser getUserWithPhoto(String PhotoID){
//
//            ParseQuery<Post> UserQuery = ParseQuery.getQuery(Post.class);
//            UserQuery.whereEqualTo("objectId", PhotoID);
//            try {
//                PhotoUser = UserQuery.getFirst().getUser();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//
//        return PhotoUser;
//    }
//
//    public static ParseUser getUserWithUser(String UserID){
//       //PhotoUser = NotificationListAdapter.getUser();
//
//        ParseQuery<ParseUser> UserQuery = ParseQuery.getQuery("_User");
//        UserQuery.whereEqualTo("objectId", UserID);
//        try {
//            PhotoUser = UserQuery.getFirst();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return PhotoUser;
//    }
//
//    public static String getPhotoId(){
//        return PhotoID;
//    }
//
//    public static String getUserId(){
//        return UserID;
//    }
//
//    public static ParseUser getUser(){
//        return ProfileUser ;
//    }
//
//        // we take the background image and button reference from the header
//        //listView.setOnScrollListener(this);
//
//
//    /*@Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        Rect rect = new Rect();
//        backgroundImage.getLocalVisibleRect(rect);
//        if (lastTopValue != rect.top) {
//            lastTopValue = rect.top;
//            backgroundImage.setY((float) (rect.top / 2.0));
//        }
//    }*/
//
//
//}
