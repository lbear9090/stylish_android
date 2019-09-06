//package codelight.social_network.Activity;
//
//import android.Manifest;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.Point;
//import android.graphics.drawable.ColorDrawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.Display;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.desmond.squarecamera.CameraActivity;
//import com.desmond.squarecamera.ImageUtility;
//import com.parse.ParseACL;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseObject;
//import com.parse.ParseUser;
//import com.parse.SaveCallback;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import codelight.social_network.ParseModels.Activity;
//import codelight.social_network.ParseModels.Hashtags;
//import lux.socialnetwork.R;
//import ly.img.android.ui.activities.CameraPreviewActivity;
//import ly.img.android.ui.activities.PhotoEditorIntent;
//
//
//public class PhotoActivity extends AppCompatActivity {
//
//    private ImageView ImageView;
//    public Button Cancel;
//    private EditText FirstComment;
//    public Button Submit;
//    private String TAG = "Log";
//    private String extra;
//    public Bitmap thumbnailBit;
////    public ParseFile photo;
////    public ParseFile thumbnail;
//    public byte[] photos;
//    public byte[] thumbnails;
//    public ProgressDialog mProgressDialog;
//    public ParseObject PhotoObject;
//
//    private String ImagePath;
//    Uri photoUri;
//    Bitmap bitmap1;
//
//    public File file;
//
//
//    private static final int REQUEST_CAMERA = 0;
//    private static final int REQUEST_CAMERA_PERMISSION = 1;
//    private static final int SELECT_PICTURE = 2;
//    private static final int EDITOR_OUTPUT = 3;
//    private Point mSize;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_photo);
//
//        Display display = getWindowManager().getDefaultDisplay();
//        mSize = new Point();
//        display.getSize(mSize);
//
//
//        ParseObject.registerSubclass(Activity.class);
//        ParseObject.registerSubclass(Hashtags.class);
//
//
//        Cancel = (Button) findViewById(R.id.cancel);
//        Submit = (Button) findViewById(R.id.submit);
//        FirstComment = (EditText) findViewById(R.id.first_comment);
//        ImageView = (ImageView) findViewById(R.id.imageView);
//
//        android.support.v7.app.ActionBar bar = getSupportActionBar();
//        if(bar != null) {
//            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2B85D3")));
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            getWindow().setStatusBarColor(Color.parseColor("#2B85D3"));
//
//        LinearLayout myLayout = new LinearLayout(this);
//        myLayout.setOrientation(LinearLayout.VERTICAL);
//
//        Bundle extras = getIntent().getExtras();
//        if(extras != null) {
//            extra = extras.getString("Chosen");
//        }
//
//        Cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e(TAG, "Cancel Clicked");
//                goToMainActivity();
//            }
//        });
//
//
//        Submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mProgressDialog = new ProgressDialog(PhotoActivity.this);
//                // Set ProgressDialog title
//                mProgressDialog.setTitle("Upload...");
//                // Set ProgressDialog message
//                mProgressDialog.setIndeterminate(false);
//                // Show ProgressDialog
//                mProgressDialog.show();
//
//
//                Log.e(TAG, "Submit Clicked");
//                if(photos != null && photos.length > 0 && thumbnails != null && thumbnails.length > 0) {
//                    photo = new ParseFile("file", photos);
//                    thumbnail = new ParseFile("file", thumbnails);
//                }
//                Submit(photo, thumbnail);
//            }
//        });
//
//        if(ImageView.getDrawable()==null){
//            ImageView.setBackground(new ColorDrawable(Color.parseColor("#FF797979")));
//            Submit.setText("Take Post");
//            Submit.setTextColor(Color.RED);
//            FirstComment.setHintTextColor(Color.RED);
//            FirstComment.setHint("You need a post");
//            FirstComment.setEnabled(false);
//
//            Submit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    requestForCameraPermission();
//                }
//            });
//        }
//
//        if(extra.equals("camera")){
//            requestForCameraPermission();
//        }
//        else{
//            Intent intent = new Intent(Intent.ACTION_PICK,
//                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//            intent.setType("image/*");
//            intent.putExtra("crop", "true");
//            intent.putExtra("scale", true);
//            intent.putExtra("outputX", 400);
//            intent.putExtra("outputY", 400);
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            intent.putExtra("return-data", true);
//            startActivityForResult(intent, SELECT_PICTURE);
//        }
//
//    }
//
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != RESULT_OK) {
//            Log.e("Post","No resultCode!");
//            return;
//        }else if(requestCode == SELECT_PICTURE || requestCode == REQUEST_CAMERA){
//            photoUri = data.getData();
//            ImagePath = getPath(photoUri);
//            Log.e("Photo1","Data: " + data + "Uri: "+ photoUri + " Path:" + ImagePath);
//
//            // Editor activity intent.
//            new PhotoEditorIntent(this)
//                    .setSourceImagePath(ImagePath)
//                    .setExportDir(PhotoEditorIntent.Directory.DCIM, "EditorImage")
//                    .setExportPrefix("result_")
//                    .destroySourceAfterSave(true)
//                    .startActivityForResult(EDITOR_OUTPUT);
//
//        }else if (requestCode == EDITOR_OUTPUT){
//            Log.e("Post","ResultCode EditorOutput!");
//            //photoUri = data.getData();
//            ImagePath = data.getStringExtra(CameraPreviewActivity.RESULT_IMAGE_PATH);
//            Log.e("Post","Data: " + data + "Uri: "+ photoUri + " Path:" + ImagePath);
//            bitmap1 = ImageUtility.decodeSampledBitmapFromPath(ImagePath, mSize.x, mSize.y);
//            setImage(bitmap1);
//        }
//
//
//
//
//                Submit.setText("Submit");
//                Submit.setTextColor(Color.WHITE);
//                FirstComment.setHintTextColor(Color.WHITE);
//                FirstComment.setHint("✎ Write a comment");
//                FirstComment.setEnabled(true);
//
//            Submit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mProgressDialog = new ProgressDialog(PhotoActivity.this);
//                    // Set ProgressDialog title
//                    mProgressDialog.setTitle("Upload...");
//                    // Set ProgressDialog message
//                    mProgressDialog.setIndeterminate(false);
//                    // Show ProgressDialog
//                    mProgressDialog.show();
//
//
//                    Log.e(TAG, "Submit Clicked");
//                    if (photos != null && photos.length > 0 && thumbnails != null && thumbnails.length > 0) {
//                        photo = new ParseFile("file", photos);
//                        thumbnail = new ParseFile("file", thumbnails);
//                    }
//                    Submit(photo, thumbnail);
//                }
//            });
//
//
//
//
//            super.onActivityResult(requestCode, resultCode, data);
//
//    }
//
//    public void requestForCameraPermission() {
//        final String permission = Manifest.permission.CAMERA;
//        if (ContextCompat.checkSelfPermission(PhotoActivity.this, permission)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(PhotoActivity.this, permission)) {
//                showPermissionRationaleDialog("Test", permission);
//            } else {
//                requestForPermission(permission);
//            }
//        } else {
//            launch();
//        }
//    }
//
//    public String getPath(Uri uri) {
//        // just some safety built in
//        if( uri == null ) {
//            return null;
//        }
//        // try to retrieve the image from the media store first
//        // this will only work for images selected from gallery
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        if( cursor != null ){
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        }
//        // this is our fallback here
//        return uri.getPath();
//    }
//
//    private void showPermissionRationaleDialog(final String message, final String permission) {
//        new AlertDialog.Builder(PhotoActivity.this)
//                .setMessage(message)
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        PhotoActivity.this.requestForPermission(permission);
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .create()
//                .show();
//    }
//
//    private void requestForPermission(final String permission) {
//        ActivityCompat.requestPermissions(PhotoActivity.this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
//    }
//
//    private void launch() {
//        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
//        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CAMERA_PERMISSION:
//                final int numOfRequest = grantResults.length;
//                final boolean isGranted = numOfRequest == 1
//                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
//                if (isGranted) {
//                    launch();
//                }
//                break;
//
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }
//
//    private void goToMainActivity() {
//        Intent Intent = new Intent(this, OldMainActivity.class);
//        startActivity(Intent);
//        finish();
//    }
//
//    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
//                                   boolean filter) {
//        float ratio = Math.min(
//                maxImageSize / realImage.getWidth(),
//                maxImageSize / realImage.getHeight());
//        int width = Math.round(ratio * realImage.getWidth());
//        int height = Math.round(ratio * realImage.getHeight());
//
//        return Bitmap.createScaledBitmap(realImage, width,
//                height, filter);
//    }
//
//    private void setImage(Bitmap bitmap) {
//        bitmap = scaleDown(bitmap, 560, true);
//        ByteArrayOutputStream PhotoStreamP = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, PhotoStreamP);
//        photos = PhotoStreamP.toByteArray();
//
//        thumbnailBit = scaleDown(bitmap, 86, true);
//        ByteArrayOutputStream PhotoStreamT = new ByteArrayOutputStream();
//        thumbnailBit.compress(Bitmap.CompressFormat.PNG, 100, PhotoStreamT);
//        thumbnails = PhotoStreamT.toByteArray();
//
//        ImageView.setImageBitmap(bitmap);
//    }
//
//    public void Submit(ParseFile file, ParseFile thumbnail){
//        Log.e(TAG, "Starting Submit_Method");
//        PhotoObject = ParseObject.create("Post");
//        PhotoObject.put("user", ParseUser.getCurrentUser());
//        PhotoObject.put("image", file);
//        PhotoObject.put("thumbnail", thumbnail);
//        PhotoObject.saveInBackground(new SaveCallback() {
//
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    String PhotoObjectId = PhotoObject.getObjectId();
//
//                    Log.e(TAG, "ObjId = " + PhotoObjectId);
//
//                    String firstComment = FirstComment.getText().toString().trim();
//
//                    if (firstComment.length() < 1) {
//                        mProgressDialog.dismiss();
//                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PhotoActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT);
//                        builder.setMessage("Your post has been uploaded!")
//                                .setTitle("Yay!")
//                                .setPositiveButton(android.R.string.ok, null);
//                        Log.e(TAG, "Everything worked fine! Let's go back to the OldMainActivity!");
//                        goToMainActivity();
//
//                    } else {
//                        submitWithFirstComment(PhotoObject);
//                    }
//                } else {
//                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PhotoActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT);
//                    builder.setMessage("Please try again")
//                            .setTitle("Upload error.")
//                            .setPositiveButton(android.R.string.ok, null);
//
//                    Log.e(TAG, "We've got some Error here: " + e);
//                }
//            }
//        });
//    }
//
//
//
//    private void submitWithFirstComment(ParseObject photo) {
//
//
//        List<String> hashTags = new ArrayList<>();
//        ParseUser currentUser = ParseUser.getCurrentUser();
//        String firstComment = FirstComment.getText().toString().trim();
//        String regexPattern = "(#\\w+)";
//        Pattern p = Pattern.compile(regexPattern);
//        Matcher m = p.matcher(firstComment);
//        while (m.find()) {
//            String hashtag = m.group(1);
//            String NoHashtag = hashtag.replace("#", "");
//            NoHashtag = NoHashtag.toLowerCase();
//            hashTags.add(NoHashtag);
//        }
//
//        Log.i(TAG, "11");
//        if (firstComment.length() > 0) {
//            Activity comment = new Activity();
//            comment.setContent(firstComment);
//            comment.setToUser(currentUser);
//            comment.setFromUser(currentUser);
//            if(hashTags.size() != 0){
//                comment.put("hashtags", hashTags);
//
//            }
//            comment.setType(Activity.COMMENT);
//            comment.put("post", photo);
//            Log.i(TAG, "1");
//            ParseACL acl = new ParseACL(currentUser);
//            acl.setPublicReadAccess(true);
//            comment.setACL(acl);
//
//            comment.saveInBackground(new SaveCallback() {
//
//                @Override
//                public void done(ParseException e) {
//                    if(e == null){
//                        mProgressDialog.dismiss();
//
//                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PhotoActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT);
//                        builder.setMessage("Your post has been uploaded!")
//                                .setTitle("Jaa!")
//                                .setPositiveButton(android.R.string.ok, null);
//                        Log.e(TAG, "Everything worked fine! Let's go back to the OldMainActivity!");
//                        goToMainActivity();
//                    }
//                    else {
//                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PhotoActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT);
//                        builder.setMessage("Please try again")
//                                .setTitle("Upload error.")
//                                .setPositiveButton(android.R.string.ok, null);
//                        Log.e(TAG, "We've got some Error here in CommentSubmit: " + e);
//                    }
//                }
//
//            });
//
//            Matcher me = p.matcher(firstComment);
//
//            while (me.find()) {
//                String hashtext = me.group(1);
//                hashtext = hashtext.replace("#", "");
//
//                Log.e(TAG, "Hashtag = " + hashtext);
//                Hashtags Hashtag = new Hashtags();
//                Hashtag.setHashtag(hashtext);
//                Hashtag.saveInBackground(new SaveCallback() {
//
//                    @Override
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            Log.e(TAG, "HashtagSubmit = OK");
//                            mProgressDialog.dismiss();
//                        } else {
//                            Log.e(TAG, "HashtagSubmit = ERROR");
//                            Log.e(TAG, "HashtagSubmit error = " + e);
//                        }
//                    }
//
//                });
//            }
//
//        }
//
//    }
//}
