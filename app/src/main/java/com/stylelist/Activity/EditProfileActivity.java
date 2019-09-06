package com.stylelist.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.stylelist.R;
import com.stylelist.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private EditText edtName, edtMention, edtBio, edtCity, edtWebsite, edtEmail, edtPayPalEmail;
    private Button btnStyles, btnDone, btnSave;
    private Spinner genderSelector, countrySelector;
//    private TextView txtBirthday;
    private ImageView headerImage;
    private CircleImageView profileImage;
    private ParseFile headerFile, profileFile;

    private static int REQUEST_CAMERA = 0;
    private static int REQUEST_GALLERY = 1;

    private boolean isHeader = true;
    private boolean isHeaderEdited = false;
    private boolean isProfilePhotoEdited = false;
    private ProgressDialog mProgressDialog;

    private ParseUser currentUser;
    private ArrayList<String> myStyles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_edit_profile);

        edtName = findViewById(R.id.edit_edit_profile_name);
        edtMention = findViewById(R.id.edit_edit_profile_mention);
        edtBio = findViewById(R.id.edit_edit_profile_bio);
        edtCity = findViewById(R.id.edit_edit_profile_city);
        edtWebsite = findViewById(R.id.edit_edit_profile_website);
        edtEmail = findViewById(R.id.edit_edit_profile_email);
        edtPayPalEmail = findViewById(R.id.edit_edit_profile_paypal_email);
        btnStyles = findViewById(R.id.btn_edit_profile_styles);
        btnDone = findViewById(R.id.btn_edit_profile_done);
        btnSave = findViewById(R.id.btn_edit_profile_save);
        genderSelector = findViewById(R.id.edit_profile_gender_selector);
        countrySelector = findViewById(R.id.spinner_edit_profile_location_selector);
//        txtBirthday = findViewById(R.id.text_edit_profile_birthday);
        headerImage = findViewById(R.id.image_edit_profile_header);
        profileImage = findViewById(R.id.image_edit_proifle_profile);

        btnSave.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        btnStyles.setOnClickListener(this);
//        txtBirthday.setOnClickListener(this);

        headerImage.setOnClickListener(this);
        profileImage.setOnClickListener(this);

        genderSelector.setOnItemSelectedListener(this);
        countrySelector.setOnItemSelectedListener(this);

        initUI();
    }

    private void initUI() {
        try {
            ParseUser.getCurrentUser().fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        edtName.setText(ParseUser.getCurrentUser().get("displayName").toString());
        if (ParseUser.getCurrentUser().get("UserInfo") != null) {
            edtBio.setText(ParseUser.getCurrentUser().get("UserInfo").toString());
        }
        edtEmail.setText(ParseUser.getCurrentUser().getString("email"));
        if (ParseUser.getCurrentUser().getString("Location") != null) {
            edtCity.setText(ParseUser.getCurrentUser().getString("Location"));
        }
        if (ParseUser.getCurrentUser().getString("Website") != null)
            edtWebsite.setText(ParseUser.getCurrentUser().getString("Website"));
//        if (ParseUser.getCurrentUser().getString("Birthday") != null) {
//            txtBirthday.setText(ParseUser.getCurrentUser().getString("Birthday"));
//        }
        if (ParseUser.getCurrentUser().getString("PaypalAddress") != null) {
            edtPayPalEmail.setText(ParseUser.getCurrentUser().getString("PaypalAddress"));
        }

        ParseFile profileBackgroundImage = ParseUser.getCurrentUser().getParseFile("headerPictureMedium");
        if (profileBackgroundImage != null) {
            Picasso.with(this).load(profileBackgroundImage.getUrl()).placeholder(R.drawable.placeholder_photo).into(headerImage);
        }

        ParseFile profilePhotoImage = ParseUser.getCurrentUser().getParseFile("profilePictureSmall");
        if (profilePhotoImage != null) {
            Picasso.with(this).load(profilePhotoImage.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(profileImage);
        }

        if (ParseUser.getCurrentUser().getString("StyleGender") != null) {
            String gender = ParseUser.getCurrentUser().getString("StyleGender");
            if (gender.equals("female") || gender.equals("Female")) {
                genderSelector.setSelection(1);
            }
        }

        String[] countryStringArray = getResources().getStringArray(R.array.countries);
        ArrayList<String> countries = new ArrayList<>(Arrays.asList(countryStringArray));
        if (ParseUser.getCurrentUser().getString("Country") != null) {
            String country = ParseUser.getCurrentUser().getString("Country");
            countrySelector.setSelection(countries.indexOf(country));
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Saving...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        currentUser = ParseUser.getCurrentUser();
        try {
            if (currentUser.fetchIfNeeded().get("Style") != null) {
                myStyles.addAll((Collection<? extends String>) currentUser.fetchIfNeeded().get("Style"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmailValid(String emailText) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return emailText.matches(emailPattern);

    }

    private boolean checkFieldValidation() {
//        {country}, {city}, {paypal address}, {gender}, {styles to follow}
        if (edtCity.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need to enter your city in order to proceed.")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
        if (edtPayPalEmail.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need to enter your paypal address in order to proceed.")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
        if (!isEmailValid(edtEmail.getText().toString())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please make sure that your email is correct!")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
        if (!isEmailValid(edtPayPalEmail.getText().toString())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please make sure that your paypal email is correct!")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
        try {
            if (currentUser.fetchIfNeeded().get("Style") != null) {
                myStyles.clear();
                myStyles.addAll((Collection<? extends String>) currentUser.fetchIfNeeded().get("Style"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (myStyles.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need to enter your styles to follow in order to proceed.")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
        return true;
    }

    private void showCameraDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_camera_gallery);
        RelativeLayout btnCamera = dialog.findViewById(R.id.btn_dialog_take_photo);
        RelativeLayout btnGallery = dialog.findViewById(R.id.btn_dialog_choose_photo);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPhoto(true);
                dialog.dismiss();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPhoto(false);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void requestPhoto(boolean isCamera) {
        if (isCamera) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setImages(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        setImages(bitmap);
    }

    private void setImages(Bitmap bitmap) {
        if (isHeader) {
            isHeaderEdited = true;
            headerImage.setImageBitmap(bitmap);
        } else {
            isProfilePhotoEdited = true;
            profileImage.setImageBitmap(bitmap);
        }
    }

    private void showCalendar() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, 1960, 0, 1);
        datePickerDialog.show();
    }

    private ParseFile getImageFile(Bitmap bitmap) {
        ByteArrayOutputStream photoStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, photoStream);
        byte[] photoByte = photoStream.toByteArray();
        ParseFile image = new ParseFile("file.png", photoByte);
        return image;
    }

    private void saveProfilePhoto() {
        if (checkFieldValidation()) {
            mProgressDialog.show();
            if (!isHeaderEdited && !isProfilePhotoEdited) {
                saveProfileFields();
            } else if (isHeaderEdited) {
                headerFile = getImageFile(((BitmapDrawable) headerImage.getDrawable()).getBitmap());
                headerFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            if (isProfilePhotoEdited) {
                                profileFile = getImageFile(((BitmapDrawable) profileImage.getDrawable()).getBitmap());
                                profileFile.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            saveProfileFields();
                                        }
                                    }
                                });
                            } else {
                                saveProfileFields();
                            }
                        }
                    }
                });
            }
        }
    }

    private void saveProfileFields() {
        try {
            currentUser.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        currentUser.put("displayName", edtName.getText().toString());
        currentUser.put("UserInfo", edtBio.getText().toString());
        currentUser.put("Location", edtCity.getText().toString());
        currentUser.put("email", edtEmail.getText().toString());
        currentUser.put("Website", edtWebsite.getText().toString());
//        currentUser.put("Birthday", txtBirthday.getText());
        currentUser.put("Country", countrySelector.getSelectedItem().toString());
        currentUser.put("StyleGender", genderSelector.getSelectedItem().toString());
        currentUser.put("PaypalAddress", edtPayPalEmail.getText().toString());
        if (headerFile != null) {
            currentUser.put("headerPictureSmall", headerFile);
            currentUser.put("headerPictureMedium", headerFile);
        }
        if (profileFile != null) {
            currentUser.put("profilePictureSmall", profileFile);
            currentUser.put("profilePictureMedium", profileFile);
        }
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            saveProfilePhoto();
        } else if (v == btnDone) {
            finish();
        } else if (v == btnStyles) {
            Intent intent = new Intent(this, MyStyleActivity.class);
            intent.putExtra(Constants.PARENT_ACTIVITY, Constants.PARENT_IS_PROFILE);
            startActivity(intent);
//        } else if (v == txtBirthday) {
//            showCalendar();
        } else if (v == headerImage) {
            isHeader = true;
            showCameraDialog();
        } else if (v == profileImage) {
            isHeader = false;
            showCameraDialog();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        if (parent == genderSelector) {
            currentUser.put("StyleGender", genderSelector.getSelectedItem().toString());
            currentUser.saveInBackground();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd.yyyy", Locale.US);
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, dayOfMonth);
//        txtBirthday.setText(simpleDateFormat.format(newDate.getTime()));
    }
}
