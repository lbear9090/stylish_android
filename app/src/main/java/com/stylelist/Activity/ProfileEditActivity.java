//package codelight.social_network.Activity;
//
//import android.app.ActionBar;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.provider.MediaStore;
//import android.os.Bundle;
//import android.text.Html;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//
//import com.parse.GetDataCallback;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseImageView;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//import com.parse.ParseUser;
//import com.parse.SaveCallback;
//import com.readystatesoftware.systembartint.SystemBarTintManager;
//
//import java.io.ByteArrayOutputStream;
//
//import lux.socialnetwork.R;
//
//
//public class ProfileEditActivity extends Activity {
//
//    public Button Save;
//
//    public ParseImageView Title;
//    public ParseImageView Thumbnail;
//
//
//    public EditText EditUsername;
//    public EditText EditMention;
//    public EditText EditBio;
//
//    public ParseFile thumbnailFile;
//    public ParseFile titleFile;
//    public static ParseUser ProfileUser;
//
//
//    private static final int PICK_FROM_CAMERA = 1;
//    private static final int PICK_FROM_GALLERY = 2;
//
//    public Bitmap photoBit;
//    public Bitmap thumbnailBit;
//
//    public ParseFile photoFile;
//    public ParseFile thumbnailFFile;
//
//    public byte[] photo;
//    public byte[] thumbnail;
//
//
//    public ParseObject foto;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile_edit);
//
//
//        ActionBar bar = getActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2B85D3")));
//        bar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#2B85D3")));
//        bar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Edit Profile</font>"));//
//
//
//        // create manager instance after the content view is set
//        SystemBarTintManager mTintManager = new SystemBarTintManager(this);
//        mTintManager.setStatusBarTintEnabled(true);
//        mTintManager.setTintColor(Color.parseColor("#2777BE"));
//
//
//        Save = (Button) findViewById(R.id.SaveButton);
//        EditUsername = (EditText) findViewById(R.id.EditUsername);
//        EditMention = (EditText) findViewById(R.id.EditMention);
//        EditBio = (EditText) findViewById(R.id.EditBio);
//        Title = (ParseImageView) findViewById(R.id.Title);
//        Thumbnail = (ParseImageView) findViewById(R.id.Thumbnail);
//
//        ProfileUser = ParseUser.getCurrentUser();
//
//
//
//        Thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChosenPhoto("camera");
//            }
//        });
//
//        Save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String Mention = EditMention.getText().toString();
//                Mention = Mention.replace("@", "");
//                Log.e("ProfileEdit", "Save User");
//                //ProfileUser.put("profilePictureSmall", thumbnailFile);
//                //ProfileUser.put("headerPictureSmall", titleFile);
//                ProfileUser.setUsername(String.valueOf(EditUsername.getText()));
//                ProfileUser.put("usernameFix", Mention);
//                ProfileUser.put("UserInfo", EditBio.getText().toString());
//                ProfileUser.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            Log.e("ProfileEdit", "Updated User");
//                        } else {
//                            Log.e("ProfileEdit", "Error while updating User: " + e);
//                        }
//                    }
//                });
//
//                goToProfileActivity();
//
//            }
//        });
//
//        //Get ProfilePicture
//        try {
//            thumbnailFile = ProfileUser.fetchIfNeeded().getParseFile("profilePictureSmall");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (thumbnailFile != null) {
//            Thumbnail.setParseFile(thumbnailFile);
//            Thumbnail.loadInBackground(new GetDataCallback() {
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
//
//            Thumbnail.setImageResource(R.drawable.avatarplaceholderprofile);
//        }
//
//
//        //Get TitlePicture
//        try {
//            titleFile = ProfileUser.fetchIfNeeded().getParseFile("headerPictureSmall");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (titleFile != null) {
//            Title.setParseFile(titleFile);
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
//        EditUsername.setText(ProfileUser.get("displayName").toString());
//
//        //Get Mention
//        EditMention.setText("@" + ProfileUser.get("usernameFix").toString());
//
//        //Get UserInfo
//        if(ProfileUser.get("UserInfo") != null)EditBio.setText(ProfileUser.get("UserInfo").toString());
//
//    }
//
//
//    public static ParseUser getUser(String UserID){
//
//        ParseQuery<ParseUser> UserQuery = ParseQuery.getQuery("_User");
//        UserQuery.whereEqualTo("objectId", UserID);
//        try {
//            ProfileUser = UserQuery.getFirst();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return ProfileUser;
//    }
//
//
//    private void goToProfileActivity() {
//        Intent Intent = new Intent(this, ProfileActivity.class);
//        startActivity(Intent);
//
//    }
//
//
//    public void ChosenPhoto(String extra) {
//        String gallery = "gallery";
//        String camera = "camera";
//
//
//
//        if (extra.equals(gallery)) {
//
//            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("image/*");
//            intent.putExtra("crop", "true");
//            intent.putExtra("return-data", true);
//            intent.putExtra("aspectX", 560);
//            intent.putExtra("aspectY", 560);
//            intent.putExtra("outputX", 560);
//            intent.putExtra("outputY", 560);
//            intent.putExtra("noFaceDetection", true);
//            intent.putExtra("screenOrientation", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//            startActivityForResult(intent, PICK_FROM_GALLERY);
//        } else if (extra.equals(camera)) {
//            Log.e("EditProfile", "Action1");
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            intent.putExtra("crop", "true");
//            intent.putExtra("aspectX", 560);
//            intent.putExtra("aspectY", 560);
//            intent.putExtra("outputX", 560);
//            intent.putExtra("outputY", 560);
//            intent.putExtra("noFaceDetection", true);
//            intent.putExtra("screenOrientation", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            intent.putExtra("return-data", true);
//            startActivityForResult(intent, PICK_FROM_CAMERA);
//        }
//
//    }
//
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        LinearLayout myLayout = new LinearLayout(this);
//        myLayout.setOrientation(LinearLayout.VERTICAL);
//
//        switch (requestCode) {
//            case PICK_FROM_GALLERY:
//                if (resultCode == RESULT_OK) {
//                    if (data != null) {
//                        Bundle extras = data.getExtras();
//
//                        photoBit = extras.getParcelable("data");
//                        ByteArrayOutputStream PhotoStream = new ByteArrayOutputStream();
//                        photoBit.compress(Bitmap.CompressFormat.PNG, 100, PhotoStream);
//                        photo = PhotoStream.toByteArray();
//
//                        thumbnailBit = Bitmap.createScaledBitmap(photoBit, 86, 86, true);
//                        thumbnailBit = extras.getParcelable("data");
//                        ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
//                        thumbnailBit.compress(Bitmap.CompressFormat.PNG, 100, thumbnailStream);
//                        thumbnail = thumbnailStream.toByteArray();
//
//                        Thumbnail.setImageBitmap(photoBit);
//                    }
//                }
//
//            case PICK_FROM_CAMERA:
//                if (resultCode == RESULT_OK) {
//                    if (data != null) {
//                        Bundle extras = data.getExtras();
//
//
//                        photoBit = extras.getParcelable("data");
//                        ByteArrayOutputStream PhotoStream = new ByteArrayOutputStream();
//                        photoBit.compress(Bitmap.CompressFormat.PNG, 100, PhotoStream);
//                        photo = PhotoStream.toByteArray();
//
//                        thumbnailBit = Bitmap.createScaledBitmap(photoBit, 86, 86, true);
//                        thumbnailBit = extras.getParcelable("data");
//                        ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
//                        thumbnailBit.compress(Bitmap.CompressFormat.PNG, 100, thumbnailStream);
//                        thumbnail = thumbnailStream.toByteArray();
//
//
//                        Thumbnail.setImageBitmap(photoBit);
//                    }
//                }
//
//        }
//
//    }
//}
