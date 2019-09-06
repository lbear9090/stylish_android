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
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
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


public class LoginActivity extends Activity {

    public AccessToken accessToken;
    private EditText edtEmail, edtPassword;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Facebook init
        FacebookSdk.setApplicationId("949988298379222");

        //All Interface settings
        setContentView(R.layout.activity_login);

        CustomFontTextView mTitleTextView = findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.login);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RelativeLayout btnFacebookLogin = findViewById(R.id.btn_login_facebook);
        Button btnLogin = findViewById(R.id.btn_login_button);
        edtEmail = findViewById(R.id.edit_login_email);
        edtPassword = findViewById(R.id.edit_login_password_field);

        //When FacebookButton is clicked
        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptFacebookLogin();
            }
        });

        //When LoginButton is clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if there is something in the EditTexts
                if (edtEmail.getText().toString().length() < 1 || edtPassword.getText().toString().length() < 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                    builder.setMessage("Passt w.e.g. op dass der all Informatiounen aginn hutt!")
                            .setTitle("Moment...")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (!isEmailValid(edtEmail.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                    builder.setMessage("Please make sure that your email is correct!")
                            .setTitle("Hold on...")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    attemptParseLogin(edtEmail.getText().toString(), edtPassword.getText().toString());
                }
            }
        });
    }

    private static boolean isEmailValid(String emailText) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return emailText.matches(emailPattern);

    }

    private void attemptFacebookLogin() {
        final List<String> permissions = Arrays.asList("public_profile", "email");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {
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
                                            goToMainActivity();
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
                                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
    private void goToMainActivity() {
        Intent Intent = new Intent(this, MainActivity.class);
        startActivity(Intent);
        finish();
    }

    private void attemptParseLogin(String s1, String s2) {

        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Login...");
        dialog.show();

        //Starting ParseLogin
        ParseUser.logInInBackground(s1, s2, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    goToMainActivity();
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    //Handling ParseErrors
                    String ErrorText;
                    switch (e.getCode()) {
                        case ParseException.USERNAME_TAKEN:
                            ErrorText = "This username already exists.";
                            break;
                        case ParseException.OBJECT_NOT_FOUND:
                            ErrorText = "Your username and/or password is incorrect.";
                            break;
                        case ParseException.CONNECTION_FAILED:
                            ErrorText = "No connection. Please try again later.";
                            break;
                        default:
                            ErrorText = "LoginError: " + e;
                            break;
                    }
                    //Starting Error window and telling the User what is wrong with his input
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                    builder.setMessage(ErrorText)
                            .setTitle("Login error.")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialoge = builder.create();
                    dialoge.show();
                }
            }
        });

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
