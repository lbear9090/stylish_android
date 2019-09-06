//package codelight.social_network.Activity;
//
//import android.app.ActionBar;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.Html;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.getbase.floatingactionbutton.FloatingActionButton;
//import com.navdrawer.SimpleSideDrawer;
//import com.parse.GetDataCallback;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseImageView;
//import com.parse.ParseUser;
//import com.readystatesoftware.systembartint.SystemBarTintManager;
//
//import codelight.social_network.Adapters.OldPostListAdapter;
//import lux.socialnetwork.R;
//
//public class OldMainActivity extends Activity {
//
//
//    private OldPostListAdapter Adapter;
//    private String TAG = "Log";
//    private String chosen;
//    public ListView ListView;
//    private FloatingActionButton CameraFloatButton;
//    private FloatingActionButton GalleryFloatButton;
//    private SimpleSideDrawer LeftNavigation;
//
//    public ParseImageView ProfilePictureView;
//    public TextView Username;
//    public RelativeLayout Profile;
//    public RelativeLayout FindFriends;
//    public RelativeLayout Settings;
//    public RelativeLayout Logout;
//
//    public ParseFile thumbnailFile;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
//        setContentView(R.layout.activity_old_main);
//
////        ParseObject.registerSubclass(Post.class);
//
//        ParseUser current = ParseUser.getCurrentUser();
//        if(current.getObjectId() == null){
//            goToLoginActivity();
//        }
//        Log.e("Main", "User:" + current.getObjectId());
//        Log.e("Main", "UserDisplayName:" + current.get("displayName").toString());
//        LeftNavigation = new SimpleSideDrawer(this);
//        LeftNavigation.setLeftBehindContentView(R.layout.navigation_left);
//
//        ProfilePictureView = (ParseImageView) LeftNavigation.findViewById(R.id.Thumbnail);
//        Username = (TextView) LeftNavigation.findViewById(R.id.Username);
//        Profile = (RelativeLayout) LeftNavigation.findViewById(R.id.Profile);
//        FindFriends = (RelativeLayout) LeftNavigation.findViewById(R.id.FindFriends);
//        Settings = (RelativeLayout) LeftNavigation.findViewById(R.id.Options);
//        Logout = (RelativeLayout) LeftNavigation.findViewById(R.id.Logout);
//
//        Profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToProfile(v, ParseUser.getCurrentUser());
//
//            }
//        });
//
//        Username.setText(ParseUser.getCurrentUser().get("displayName").toString());
//
//        FindFriends.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        Settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToSettingsActivity();
//
//            }
//        });
//
//        Logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SaveLogout();
//
//            }
//        });
//
//        thumbnailFile = ParseUser.getCurrentUser().getParseFile("profilePictureSmall");
//
//        if (thumbnailFile != null) {
//            ProfilePictureView.setParseFile(thumbnailFile);
//            ProfilePictureView.loadInBackground(new GetDataCallback() {
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
//            ProfilePictureView.setImageResource(R.drawable.avatarplaceholderprofile);
//        }
//
//
//        CameraFloatButton = (FloatingActionButton) findViewById(R.id.camera);
//        GalleryFloatButton = (FloatingActionButton) findViewById(R.id.gallery);
//
//        CameraFloatButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chosen = "camera";
//                goToPhotoActivity(chosen);
//
//            }
//        });
//        GalleryFloatButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chosen = "gallery";
//                goToPhotoActivity(chosen);
//
//            }
//        });
//
//
//
//
//       /* ParsePush.subscribeInBackground("", new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.d(TAG, "successfully subscribed to the broadcast channel.");
//                } else {
//                    Log.e(TAG, "failed to subscribe . Error: for push", e);
//                }
//            }
//        });*/
//
//        ListView = (ListView) findViewById(R.id.ListView);
//
//
////        Adapter = new OldPostListAdapter(this);
//        ListView.setAdapter(Adapter);
//
//        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                // Post clicked == parent.getItemAtPosition(position)
//                Log.d("Post clicked", "Position: " + position);
//                OldPostListAdapter.likeButton(position, ListView);
//            }
//        });
//
//        Adapter.loadObjects();
//
//        //Reload Action
//        RefreshAnimation = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
//        RefreshAnimation.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh(SwipyRefreshLayoutDirection direction) {
//                Log.d("Log", "Refresh triggered at "
//                        + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Hide the refresh after 2sec
//                        OldMainActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                RefreshAnimation.setRefreshing(false);
//                                updateHomeList();
//                            }
//                        });
//                    }
//                }, 2000);
//            }
//        });
//
//
//        ActionBar bar = getActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2B85D3")));
//        bar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#2B85D3")));
//        bar.setDisplayHomeAsUpEnabled(true);
//        bar.setDisplayUseLogoEnabled(true);
//        //bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
//        bar.setIcon(R.drawable.notif_white_icon);
//        bar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Social Network</font>"));//
//
//
//        // create manager instance after the content view is set
//        SystemBarTintManager mTintManager = new SystemBarTintManager(this);
//        mTintManager.setStatusBarTintEnabled(true);
//        mTintManager.setTintColor(Color.parseColor("#2777BE"));
//    }
//
//    @Override
//    public void onBackPressed() {
//    }
//
//    public void goToProfile(View v, ParseUser user){
//        Intent myIntent = new Intent(v.getContext(), ProfileActivity.class);
//        myIntent.putExtra("UserID", user.getObjectId());
//        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        v.getContext().startActivity(myIntent);
//        Log.i("Log", "Go to Profile");
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == R.id.action_notification) {
//            goToNotificationctivity();
//            return true;
//        }
//        else if (id == android.R.id.home) {
//            LeftNavigation.toggleLeftDrawer();
//            Log.d("Log", "ToggleDrawer");
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void goToNotificationctivity() {
//        //Log.i(SocialNetworkApplication.TAG, "entered showHomeListActivity");
//
//        Intent Intent = new Intent(OldMainActivity.this, NotificationActivity.class);
//        startActivity(Intent);
//        finish(); // This closes the login screen so it's not on the back stack
//    }
//
//    private void goToLoginActivity() {
//        //Log.i(SocialNetworkApplication.TAG, "entered showHomeListActivity");
//        Intent Intent = new Intent(this, LoginActivity.class);
//        startActivity(Intent);
//        finish(); // This closes the login screen so it's not on the back stack
//    }
//
//    private void goToPhotoActivity(String extra) {
//        //Log.i(SocialNetworkApplication.TAG, "entered showHomeListActivity");
////        Intent Intent = new Intent(this, PhotoActivity.class);
////        if(extra != null){
////            Intent.putExtra("Chosen", extra);
////        }
////        startActivity(Intent);
//    }
//
//    private void goToSettingsActivity() {
//        Intent Intent = new Intent(this, SettingsActivity.class);
//        startActivity(Intent);
//
//    }
//
//    private void SaveLogout() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Logout");
//        alertDialogBuilder
//                .setMessage("Are you sure?")
//                .setCancelable(false)
//                .setPositiveButton("Logout",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,int id) {
//                        ParseUser current = ParseUser.getCurrentUser();
//                        current.logOut();
//                        goToLoginActivity();
//                    }
//                })
//                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        // create alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//
//        // show it
//        alertDialog.show();
//    }
//
//
//
//    /*private void ChoosePhotoWay() {
//        final CharSequence[] items = { "Foto maachen", "Foto auswielen",
//                "Ofbriechen" };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(OldMainActivity.this);
//        builder.setTitle("Wéi wëllt der ET maachen?");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (items[item].equals("Foto maachen")) {
//                    chosen = "camera";
//                    goToPhotoActivity(chosen);
//                }
//
//                else if(items[item].equals("Foto auswielen")){
//                    chosen = "gallery";
//                    goToPhotoActivity(chosen);
//                }
//                else if(items[item].equals("Ofbriechen")){
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }*/
//
//
//
//    private void updateHomeList() {
//
//        ParseUser currentUser = ParseUser.getCurrentUser();
//        if (currentUser != null) {
//            // Check if the user is currently logged
//            // and show any cached content
//
//            Log.e(TAG, "CurrentUser in Home:" + currentUser);
//
//
//        } else {
//            // If the user is not logged in, go to the
//            // activity showing the login view.
//
//            goToLoginActivity();
//        }
//
//        ListView.smoothScrollToPosition(0);
//        ListView.setAdapter(Adapter);
//        Adapter.loadObjects();
//    }
//}
