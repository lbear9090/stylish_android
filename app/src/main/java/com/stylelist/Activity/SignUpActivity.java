package com.stylelist.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;


public class SignUpActivity extends Activity {

    public AccessToken accessToken;

    private EditText edtFirstName, edtLastName, edtEmail, edtPassword;

    public ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        CustomFontTextView mTitleTextView = findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.signup);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edtFirstName = findViewById(R.id.edit_signup_first_name);
        edtLastName = findViewById(R.id.edit_signup_last_name);
        edtEmail = findViewById(R.id.edit_signup_email);
        edtPassword = findViewById(R.id.edit_signup_password);
        Button btnSignUp = findViewById(R.id.btn_signup_register);
        RelativeLayout btnSignUpWithFacebook = findViewById(R.id.btn_facebook_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if everything is okay with the strings from the EditTexts
                if (edtFirstName.getText().toString().length() < 1
                    || edtLastName.getText().toString().length() < 1
                    || edtEmail.getText().toString().length() < 1
                    || edtPassword.getText().toString().length() < 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                    builder.setMessage("Please make sure you have put in all information!")
                            .setTitle("Hold on...")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (!isEmailValid(edtEmail.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                    builder.setMessage("Please make sure that your email is correct!")
                            .setTitle("Hold on...")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    //Starting ProgressDialog(Loading window)
                    dialog = new ProgressDialog(SignUpActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                    dialog.setMessage("Signing up...");
                    dialog.show();
                    //Starting Parse signUp process
                    attemptSignUpProgress(edtFirstName.getText().toString(), edtLastName.getText().toString(), edtEmail.getText().toString(), edtPassword.getText().toString());
                }
            }
        });

        btnSignUpWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptFacebookLogin();
            }
        });
    }

    private void attemptFacebookLogin() {
        final List<String> permissions = Arrays.asList("public_profile", "email");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(SignUpActivity.this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.e("MyApp", "Uh oh. The user cancelled the Facebook login.: " + err);
                } else if (user.isNew()) {
                    Log.e("MyApp", "User is signing up and logging in through Facebook!");
                    getUserDetailFromFB();
                } else {
                    Log.e("MyApp", "User logged in through Facebook!");
                    goToMainActivity();
                }
            }
        });
    }

    private void getUserDetailFromFB() {
        final String[] email = {null};
        final String[] displayName = {null};
        final String[] id = {null};
        accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    id[0] = object.getString("id");
                    email[0] = object.getString("email");
                    displayName[0] = object.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new ProfilePhotoAsync(email[0], displayName[0], id[0]).execute();
                //saveNewParseUser(email[0], displayName[0], pictureUrl[0], id[0]);
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name, email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public Bitmap downloadImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("BitmapDownload", "Error getting bitmap", e);
        }
        return bm;
    }

    private void saveNewParseUser(String displayName, String email, Bitmap bitmap, String id) {

        //Take away space and Transform to lowercase
        String userNameFix = displayName.toLowerCase().replace(" ", "");
        //Transform to lowercase
        String userNameLower = displayName.toLowerCase();
        ParseFile profilePictureSmall = null;
        ParseFile profilePictureNormal = null;
        try {
            profilePictureSmall = new ParseFile("thumbnail.jpeg", createBiteArrayFromBitmap(Bitmap.createScaledBitmap(bitmap, 400, 400, false)));
            profilePictureNormal = new ParseFile("picture.jpeg", createBiteArrayFromBitmap(Bitmap.createScaledBitmap(cropBitmap(bitmap), 400, 400, false)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Setting up nwe ParseUser
        final ParseUser newUser = ParseUser.getCurrentUser();
        newUser.setUsername(displayName);
        newUser.setEmail(email);
        //NewUser.setPassword("$");
        newUser.put("UserInfo", "Place your bio here");
        newUser.put("username", email);
        newUser.put("usernameFix", userNameFix);
        newUser.put("displayName", displayName);
        newUser.put("displayName_lower", userNameLower);
        newUser.put("facebookId", id);
        newUser.put("profilePictureSmall", profilePictureSmall);
        newUser.put("profilePictureMedium", profilePictureNormal);

        final ParseFile finalProfilePictureNormal = profilePictureNormal;
        profilePictureSmall.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                // If successful add file to user and signUpInBackground
                Log.e("Log", "profilePictureSmall:" + "success");
                if (e == null) {
                    finalProfilePictureNormal.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            // If successful add file to user and signUpInBackground
                            Log.e("Log", "ProfilePictureNormal:" + "success");
                            if (e == null) {
                                newUser.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            //When everything is done, we can now go to the OldMainActivity
                                            goToEditProfileActivity();
                                        } else {
                                            String ErrorText = null;
                                            //dialog.dismiss();
                                            //Handling ParseErrors
                                            e.printStackTrace();
                                            Log.e("Log", "Error:" + e);
                                            switch (e.getCode()) {
                                                case ParseException.USERNAME_TAKEN:
                                                    ErrorText = "This username already exists.";
                                                    break;
                                                case ParseException.OBJECT_NOT_FOUND:
                                                    ErrorText = "Username and/or password is wrong";
                                                    break;
                                                case ParseException.CONNECTION_FAILED:
                                                    ErrorText = "No connection. Please try again later.";
                                                    break;
                                                default:
                                                    break;
                                            }

                                            String er;
                                            if (ErrorText != null) {
                                                er = ErrorText;
                                            } else {
                                                er = e.toString();
                                            }
                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                            builder.setMessage("Error:  " + er)
                                                    .setTitle("There's an Error somewhere...")
                                                    .setPositiveButton(android.R.string.ok, null);
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        }
                                    }
                                });
                            } else {
                                Log.e("Error", "ProfilePictureNormal:" + "fail. Error: " + e);
                            }
                        }
                    });
                } else {
                    Log.e("Error", "profilePictureSmall:" + "fail. Error: " + e);
                }

            }
        });
    }

    private byte[] createBiteArrayFromBitmap(Bitmap bitmap) throws IOException {
        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        return bos.toByteArray();
    }

    private Bitmap cropBitmap(Bitmap bitmap) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);

        //setting radius
        roundedBitmapDrawable.setCornerRadius(200.0f);
        roundedBitmapDrawable.setAntiAlias(true);

        return roundedBitmapDrawable.getBitmap();
    }

    private static boolean isEmailValid(String emailText) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return emailText.matches(emailPattern);

    }

    private void goToMainActivity() {
        Intent Intent = new Intent(this, MainActivity.class);
        startActivity(Intent);
        finish(); // This closes the login screen so it's not on the back stack
    }

    private void goToEditProfileActivity() {
        Intent Intent = new Intent(this, EditProfileActivity.class);
        startActivity(Intent);
        finish(); // This closes the login screen so it's not on the back stack
    }

    private void attemptSignUpProgress(String firstName, String lastName, String email, String password) {
        //Take away space and Transform to lowercase
        String userNameOnePiece = firstName + lastName;
        String userNameFix = userNameOnePiece.toLowerCase();

        //Setting up new ParseUser
        ParseUser newUser = new ParseUser();
        newUser.setUsername(email);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.put("usernameFix", userNameFix);
        newUser.put("displayName", firstName + " " + lastName);

        newUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    //When everything is done, we can now go to the OldMainActivity
                    goToEditProfileActivity();
                    dialog.dismiss();
                }
                else {
                    String ErrorText = null;
                    dialog.dismiss();
                    //Handling ParseErrors
                    e.printStackTrace();
                    Log.e("Log", "Error:" + e);
                    switch (e.getCode()) {
                        case ParseException.USERNAME_TAKEN:
                            ErrorText = "This username already exists.";
                            break;
                        case ParseException.OBJECT_NOT_FOUND:
                            ErrorText = "Username and/or password is wrong";
                            break;
                        case ParseException.CONNECTION_FAILED:
                            ErrorText = "No connection. Please try again later.";
                            break;
                        default:
                            break;
                    }

                    String er;
                    if(ErrorText != null){
                        er = ErrorText;
                    }
                    else{
                        er = e.toString();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("Error:  " + er)
                            .setTitle("There's an Error somewhere...")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    // Login failed. Look at the ParseException to see what happened.
                }
            }
        });
    }

    private class ProfilePhotoAsync extends AsyncTask<String, String, String> {
        private Bitmap bit;
        String email;
        String displayName;
        String id;

        private ProfilePhotoAsync(String email, String displayName, String id) {
            this.email = email;
            this.displayName = displayName;
            this.id = id;
        }

        @Override
        protected String doInBackground(String... params) {
            // Fetching data from URI and storing in bitmap
            String url = "https://graph.facebook.com/" + id + "/picture?type=large";
            bit = downloadImageBitmap(url);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            saveNewParseUser(displayName, email, bit, id);
        }
    }
}
