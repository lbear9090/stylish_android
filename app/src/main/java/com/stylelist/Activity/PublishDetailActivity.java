package com.stylelist.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.Interfaces.ImageUploadCallback;
import com.stylelist.ParseModels.ItemPost;
import com.stylelist.ParseModels.Post;
import com.stylelist.R;
import com.stylelist.Utils.Constants;
import com.stylelist.Utils.UtilFunctions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import faranjit.currency.edittext.CurrencyEditText;

import static com.stylelist.Utils.Constants.FOR_HIRE;
import static com.stylelist.Utils.Constants.FOR_INSPIRATION;
import static com.stylelist.Utils.Constants.FOR_SALE;
import static com.stylelist.Utils.StyleListApp.currentItemDeliverySetting;
import static com.stylelist.Utils.StyleListApp.currentItemRect;
import static com.stylelist.Utils.StyleListApp.currentItemViewRect;
import static com.stylelist.Utils.StyleListApp.editPhotoActivity;
import static com.stylelist.Utils.StyleListApp.editedPhoto;
import static com.stylelist.Utils.StyleListApp.mainActivity;
import static com.stylelist.Utils.StyleListApp.originalPhoto;
import static com.stylelist.Utils.StyleListApp.parentPost;
import static com.stylelist.Utils.StyleListApp.parentPostId;
import static com.stylelist.Utils.StyleListApp.preparingItemSaleType;
import static com.stylelist.Utils.StyleListApp.selectedStyles;

public class PublishDetailActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ImageUploadCallback {

    private CustomFontButton btnCancel, btnStraightPost, btnPostAnother, btnDelivery;
    private Button btnReadPaid, btnPhotoTips, btnHireAdvise;
    private EditText edtItemName, edtDescription;
    private CurrencyEditText edtPrice, edtDepositPrice;
    private Spinner categorySelector, locationSelector, conditionSelector;
    private ImageView img1, img2, img3, img4, img5, btnRemove1, btnRemove2, btnRemove3, btnRemove4, btnRemove5;
    private CustomFontTextView txtSaleTypeTitle, txtHireType, txtLocation;

    private RelativeLayout deliveryContainer, priceContainer, locationContainer, depositPriceContainer, conditionContainer;

    private int imageIndex = 0;

    private static int REQUEST_CAMERA = 0;
    private static int REQUEST_GALLERY = 1;

    private String strCountry = "";
    private String strCity = "";
    public ProgressDialog mProgressDialog;

    public byte[] photoByte;
    public byte[] originalPhotoByte;

    public ParseFile filteredPhotoFile;
    public ParseFile originalPhotoFile;
    private boolean straightPublish = true;
    private int imgCount;
    private ImageUploadCallback imageUploadCallback;

    private ItemPost itemPostObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_detail);

        btnCancel = findViewById(R.id.btn_publish_detail_cancel);
        btnStraightPost = findViewById(R.id.btn_detail_publish_straight);
        btnPostAnother = findViewById(R.id.btn_publish_detail_publish_another);
        btnReadPaid = findViewById(R.id.btn_publish_read_paid);
        btnPhotoTips = findViewById(R.id.btn_publish_item_photo_tips);
        btnHireAdvise = findViewById(R.id.btn_publish_item_hire_advise);
        btnDelivery = findViewById(R.id.btn_post_detail_delivery);
        edtDepositPrice = findViewById(R.id.edit_publish_deposit_price);
        edtItemName = findViewById(R.id.edit_publish_item_name);
        edtDescription = findViewById(R.id.edit_publish_item_description);
//        edtConditionDetail = findViewById(R.id.edit_publish_item_hire_condition);
        edtPrice = findViewById(R.id.edit_publish_item_price);
        categorySelector = findViewById(R.id.spinner_publish_category_selector);
//        deliverySelector = findViewById(R.id.spinner_publish_delivery_selector);
        locationSelector = findViewById(R.id.spinner_publish_location_selector);
        conditionSelector = findViewById(R.id.spinner_publish_item_hire_condition);
        img1 = findViewById(R.id.image_publish_detail_1);
        img2 = findViewById(R.id.image_publish_detail_2);
        img3 = findViewById(R.id.image_publish_detail_3);
        img4 = findViewById(R.id.image_publish_detail_4);
        img5 = findViewById(R.id.image_publish_detail_5);
        btnRemove1 = findViewById(R.id.btn_remove_image_1);
        btnRemove2 = findViewById(R.id.btn_remove_image_2);
        btnRemove3 = findViewById(R.id.btn_remove_image_3);
        btnRemove4 = findViewById(R.id.btn_remove_image_4);
        btnRemove5 = findViewById(R.id.btn_remove_image_5);
        deliveryContainer = findViewById(R.id.publish_detail_delivery_container);
        priceContainer = findViewById(R.id.publish_detail_price_container);
        locationContainer = findViewById(R.id.publish_detail_location_container);
        depositPriceContainer = findViewById(R.id.publish_detail_deposit_price_container);
        conditionContainer = findViewById(R.id.publish_detail_condition_container);
        txtSaleTypeTitle = findViewById(R.id.text_publish_item_sale_title);
        txtHireType = findViewById(R.id.text_publish_item_hire_description);
        txtLocation = findViewById(R.id.txt_publish_item_location);
        initUI();
    }

    private void initUI() {
        btnCancel.setOnClickListener(this);
        btnStraightPost.setOnClickListener(this);
        btnPostAnother.setOnClickListener(this);
        btnReadPaid.setOnClickListener(this);
        btnPhotoTips.setOnClickListener(this);
        btnHireAdvise.setOnClickListener(this);
        btnDelivery.setOnClickListener(this);
        categorySelector.setOnItemSelectedListener(this);
//        deliverySelector.setOnItemSelectedListener(this);
        locationSelector.setOnItemSelectedListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);

        btnRemove1.setOnClickListener(this);
        btnRemove2.setOnClickListener(this);
        btnRemove3.setOnClickListener(this);
        btnRemove4.setOnClickListener(this);
        btnRemove5.setOnClickListener(this);

        btnRemove1.setVisibility(View.GONE);
        btnRemove2.setVisibility(View.GONE);
        btnRemove3.setVisibility(View.GONE);
        btnRemove4.setVisibility(View.GONE);
        btnRemove5.setVisibility(View.GONE);

        prepareImages(editedPhoto);
        if (parentPostId == null) {
            prepareParentPostImage();
        }
        txtSaleTypeTitle.setText(preparingItemSaleType);
        switch (preparingItemSaleType) {
            case FOR_HIRE:
                txtHireType.setVisibility(View.VISIBLE);
                txtHireType.setText("& hire condition");
//                edtConditionDetail.setVisibility(View.VISIBLE);
                btnHireAdvise.setVisibility(View.VISIBLE);
                depositPriceContainer.setVisibility(View.VISIBLE);
                conditionContainer.setVisibility(View.VISIBLE);
                break;
            case FOR_SALE:
//                edtConditionDetail.setVisibility(View.GONE);
                btnHireAdvise.setVisibility(View.GONE);
                depositPriceContainer.setVisibility(View.GONE);
                txtHireType.setVisibility(View.GONE);
                conditionContainer.setVisibility(View.VISIBLE);
                break;
            case FOR_INSPIRATION:
//                edtConditionDetail.setVisibility(View.GONE);
                conditionContainer.setVisibility(View.GONE);
                btnHireAdvise.setVisibility(View.GONE);
                depositPriceContainer.setVisibility(View.GONE);
                txtHireType.setVisibility(View.GONE);
                locationContainer.setVisibility(View.GONE);
                deliveryContainer.setVisibility(View.GONE);
                priceContainer.setVisibility(View.GONE);
                btnReadPaid.setVisibility(View.GONE);
                break;
        }
        imageUploadCallback = this;

        strCountry = ParseUser.getCurrentUser().getString("Country");
        strCity = ParseUser.getCurrentUser().getString("Location");

        if(strCountry == null)
            strCountry = "";
        if(strCity == null)
            strCity = "";
        txtLocation.setText(strCity + "(" + strCountry + ")");

        String[] countryStringArray = getResources().getStringArray(R.array.countries);
        ArrayList<String> countries = new ArrayList<>(Arrays.asList(countryStringArray));
        if (ParseUser.getCurrentUser().getString("Country") != null) {
            String country = ParseUser.getCurrentUser().getString("Country");
            locationSelector.setSelection(countries.indexOf(country));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getClass() == CustomFontButton.class) {
            if (btnCancel == v) {
                editPhotoActivity.finish();
                finish();
            } else if (v == btnPostAnother) {
                publishAndAdd();
            } else if (v == btnStraightPost) {
                straightPublish();
            } else if (v == btnDelivery) {
                attemptDelivery();
            }
        } else if (v.getClass() == ImageView.class) {
            switch (v.getId()) {
                case R.id.image_publish_detail_1:
                    imageIndex = 0;
                    showCameraDialog();
                    break;
                case R.id.image_publish_detail_2:
                    imageIndex = 1;
                    showCameraDialog();
                    break;
                case R.id.image_publish_detail_3:
                    imageIndex = 2;
                    showCameraDialog();
                    break;
                case R.id.image_publish_detail_4:
                    imageIndex = 3;
                    showCameraDialog();
                    break;
                case R.id.image_publish_detail_5:
                    imageIndex = 4;
                    showCameraDialog();
                    break;
                case R.id.btn_remove_image_1:
                    v.setVisibility(View.GONE);
                    img1.setImageResource(R.drawable.ic_add_photo);
                    break;
                case R.id.btn_remove_image_2:
                    v.setVisibility(View.GONE);
                    img2.setImageResource(R.drawable.ic_add_photo);
                    break;
                case R.id.btn_remove_image_3:
                    v.setVisibility(View.GONE);
                    img3.setImageResource(R.drawable.ic_add_photo);
                    break;
                case R.id.btn_remove_image_4:
                    v.setVisibility(View.GONE);
                    img4.setImageResource(R.drawable.ic_add_photo);
                    break;
                case R.id.btn_remove_image_5:
                    v.setVisibility(View.GONE);
                    img5.setImageResource(R.drawable.ic_add_photo);
                    break;
            }
        } else if (v.getClass() == Button.class) {
            if (v == btnReadPaid) {

            } else {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentItemDeliverySetting != null) {
            String btnTitle = "";
            btnTitle = btnTitle + (currentItemDeliverySetting.isMeetPerson? "Meet in person": "");
            btnTitle = btnTitle + (btnTitle.equals("")? "":", ")
                    + (currentItemDeliverySetting.isDomesticShipping?"DS ":"")
                    + (!currentItemDeliverySetting.isDomesticShipping || currentItemDeliverySetting.domesticPrice.equals("0.00")?"":"$" + currentItemDeliverySetting.domesticPrice);
            btnTitle = btnTitle + (btnTitle.equals("")? "":",")
                    + (currentItemDeliverySetting.isInternationalShipping?"IS ":"")
                    + (!currentItemDeliverySetting.isInternationalShipping || currentItemDeliverySetting.internationalPrice.equals("0.00")?"":"$" + currentItemDeliverySetting.internationalPrice);
            btnDelivery.setText(btnTitle);
        }
    }

    private void attemptDelivery() {
        startActivity(new Intent(this, DeliveryActivity.class));
    }

    private void prepareImages(Bitmap bitmap) {
        Bitmap result = UtilFunctions.removeTransparentArea(bitmap);
        ByteArrayOutputStream photoStream = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.PNG, 100, photoStream);
        photoByte = photoStream.toByteArray();
    }

    private void prepareParentPostImage() {
        ByteArrayOutputStream photoStream = new ByteArrayOutputStream();
        originalPhoto.compress(Bitmap.CompressFormat.PNG, 100, photoStream);
        originalPhotoByte = photoStream.toByteArray();
    }

    private ParseFile getImageFile(Bitmap bitmap) {
        ByteArrayOutputStream photoStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, photoStream);
        byte[] photoByte = photoStream.toByteArray();
        ParseFile image = new ParseFile("file.png", photoByte);
        return image;
    }

    private boolean checkSaleTypeValidation() {
        if (!preparingItemSaleType.equals(FOR_INSPIRATION) && currentItemDeliverySetting == null)
            return false;
        else
            return true;
    }

    private void publishPost() {
        if (checkSaleTypeValidation()) {
            imgCount = 0;
            ImageView[] btnRemoves = {btnRemove1, btnRemove2, btnRemove3, btnRemove4, btnRemove5};
            for (ImageView btnRemove : btnRemoves) {
                if (btnRemove.getVisibility() == View.VISIBLE) {
                    imgCount++;
                }
            }

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Uploading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            if (parentPostId == null) {
                if (originalPhotoByte != null && originalPhotoByte.length > 0) {
                    originalPhotoFile = new ParseFile("origin.png", originalPhotoByte);
                    filteredPhotoFile = new ParseFile("filtered.png", photoByte);
                    originalPhotoFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            publishParentPost();
                        }
                    });
                }
            } else {
                if (photoByte != null && photoByte.length > 0) {
                    filteredPhotoFile = new ParseFile("filtered.png", photoByte);
                    filteredPhotoFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            publishItemPost(parentPostId);
                        }
                    });
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need to choose delivery method to proceed.")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void publishParentPost() {
        final Post parseObject = new Post();

        if (selectedStyles.size() > 0) {
            parseObject.put(Constants.POST_STYLE_TAG, selectedStyles);
        }
        parseObject.put("user", ParseUser.getCurrentUser());
        parseObject.put("image", originalPhotoFile);
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    parentPostId = parseObject.getObjectId();
                    parentPost = parseObject;
                    filteredPhotoFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            publishItemPost(parentPostId);
                        }
                    });
                } else {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    public void publishItemPost(String postId) {

        itemPostObject = new ItemPost();

        itemPostObject.put(Constants.ITEM_POST_POST_ID, postId);
        itemPostObject.put(Constants.ITEM_POST_FILTERED_IMAGE, filteredPhotoFile);
        itemPostObject.put(Constants.ITEM_POST_HASH_TAG, categorySelector.getSelectedItem().toString());
        itemPostObject.put(Constants.ITEM_POST_SALE_TYPE , preparingItemSaleType);
        itemPostObject.put(Constants.ITEM_POST_HASH_TAG_NAME, edtItemName.getText().toString());
        itemPostObject.put(Constants.ITEM_POST_X_POSITION, String.valueOf(currentItemRect.xPosition));
        itemPostObject.put(Constants.ITEM_POST_Y_POSITION, String.valueOf(currentItemRect.yPosition));
        itemPostObject.put(Constants.ITEM_POST_RECT_WIDTH, String.valueOf(currentItemRect.width));
        itemPostObject.put(Constants.ITEM_POST_RECT_HEIGHT, String.valueOf(currentItemRect.height));
        itemPostObject.put(Constants.ITEM_POST_VIEW_X_POSITION, String.valueOf(currentItemViewRect.xPosition));
        itemPostObject.put(Constants.ITEM_POST_VIEW_Y_POSITION, String.valueOf(currentItemViewRect.yPosition));
        if (!preparingItemSaleType.equals(FOR_INSPIRATION)) {
            if (preparingItemSaleType.equals(FOR_HIRE)) {
                try {
                    if (edtDepositPrice.getText().toString().equals(""))
                        itemPostObject.put(Constants.ITEM_POST_DEPOSIT_PRICE, "0.00");
                    else
                        itemPostObject.put(Constants.ITEM_POST_DEPOSIT_PRICE, String.valueOf(edtDepositPrice.getCurrencyDouble()));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
            itemPostObject.put(Constants.ITEM_POST_CONDITION_DETAIL, conditionSelector.getSelectedItem().toString());
            itemPostObject.put(Constants.ITEM_POST_LOCATION_CITY_INFO, strCity);
            itemPostObject.put(Constants.ITEM_POST_LOCATION_COUNTRY_INFO, strCountry);
            //itemPostObject.put(Constants.ITEM_POST_LOCATION_INFO, locationSelector.getSelectedItem().toString());
            itemPostObject.put(Constants.ITEM_POST_HASH_TAG_DESCRIPTION, edtDescription.getText().toString());
            itemPostObject.put(Constants.ITEM_POST_SHIPPING, currentItemDeliverySetting.isShipping?"Yes":"No");
            itemPostObject.put(Constants.ITEM_POST_MEET, currentItemDeliverySetting.isMeetPerson?"Yes":"No");
            if(currentItemDeliverySetting.isDomesticShipping)
                itemPostObject.put(Constants.ITEM_POST_DOMESTIC_COST, currentItemDeliverySetting.domesticPrice);
            else
                itemPostObject.put(Constants.ITEM_POST_DOMESTIC_COST, "");
            if(currentItemDeliverySetting.isInternationalShipping)
                itemPostObject.put(Constants.ITEM_POST_INTERNATIONAL_COST, currentItemDeliverySetting.internationalPrice);
            else
                itemPostObject.put(Constants.ITEM_POST_INTERNATIONAL_COST,"");
            try {
                if (!edtPrice.getText().toString().equals("")) {
                    itemPostObject.put(Constants.ITEM_POST_ITEM_PRICE, String.valueOf(edtPrice.getCurrencyDouble()));
                } else {
                    itemPostObject.put(Constants.ITEM_POST_ITEM_PRICE, "0.00");
                }
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        if (btnRemove1.getVisibility() == View.VISIBLE) {
            final ParseFile imgFile = getImageFile(((BitmapDrawable) img1.getDrawable()).getBitmap());
            imgFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    itemPostObject.put(Constants.ITEM_POST_IMAGE_ONE, imgFile);
                    imageUploadCallback.onSuccess();
                }
            });
        }

        if (btnRemove2.getVisibility() == View.VISIBLE) {
            final ParseFile imgFile = getImageFile(((BitmapDrawable) img2.getDrawable()).getBitmap());
            imgFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    itemPostObject.put(Constants.ITEM_POST_IMAGE_TWO, imgFile);
                    imageUploadCallback.onSuccess();
                }
            });
        }

        if (btnRemove3.getVisibility() == View.VISIBLE) {
            final ParseFile imgFile = getImageFile(((BitmapDrawable) img3.getDrawable()).getBitmap());
            imgFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    itemPostObject.put(Constants.ITEM_POST_IMAGE_THREE, imgFile);
                    imageUploadCallback.onSuccess();
                }
            });
        }

        if (btnRemove4.getVisibility() == View.VISIBLE) {
            final ParseFile imgFile = getImageFile(((BitmapDrawable) img4.getDrawable()).getBitmap());
            imgFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    itemPostObject.put(Constants.ITEM_POST_IMAGE_FOUR, imgFile);
                    imageUploadCallback.onSuccess();
                }
            });
        }

        if (btnRemove5.getVisibility() == View.VISIBLE) {
            final ParseFile imgFile = getImageFile(((BitmapDrawable) img5.getDrawable()).getBitmap());
            imgFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    itemPostObject.put(Constants.ITEM_POST_IMAGE_FIVE, imgFile);
                    imageUploadCallback.onSuccess();
                }
            });
        }

        if (imgCount == 0) {
            itemPostObject.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException e) {
                    mProgressDialog.dismiss();
                    if (e == null) {
                        currentItemDeliverySetting = null;
                        parentPost.add(Constants.POST_HASH_TAGS, categorySelector.getSelectedItem().toString());
                        parentPost.add(Constants.POST_SALE_TYPE, preparingItemSaleType);
                        //parentPost.add(Constants.POST_LOCATION, locationSelector.getSelectedItem().toString());
                        parentPost.add(Constants.POST_LOCATION_CITY, strCity);
                        parentPost.add(Constants.POST_LOCATION_COUNTRY, strCountry);
                        parentPost.add(Constants.POST_CONDITION, conditionSelector.getSelectedItem().toString());
                        parentPost.saveEventually();
                        if (straightPublish) {
                            parentPostId = null;
                            editPhotoActivity.finish();
                            finish();
                        } else {
                            editPhotoActivity.finish();
                            mainActivity.promptToEdit();
                            finish();
                        }
                    } else {
                        e.printStackTrace();
                        finish();
                    }
                }
            });
        }
    }

    private void straightPublish() {
        straightPublish = true;
        publishPost();
    }

    private void publishAndAdd() {
        straightPublish = false;
        publishPost();
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

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setExampleImages(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        setExampleImages(bitmap);
    }

    private void setExampleImages(Bitmap bitmap) {
        switch (imageIndex) {
            case 0:
                img1.setImageBitmap(bitmap);
                btnRemove1.setVisibility(View.VISIBLE);
                break;
            case 1:
                img2.setImageBitmap(bitmap);
                btnRemove2.setVisibility(View.VISIBLE);
                break;
            case 2:
                img3.setImageBitmap(bitmap);
                btnRemove3.setVisibility(View.VISIBLE);
                break;
            case 3:
                img4.setImageBitmap(bitmap);
                btnRemove4.setVisibility(View.VISIBLE);
                break;
            case 4:
                img5.setImageBitmap(bitmap);
                btnRemove5.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    @Override
    public void onSuccess() {
        imgCount--;
        if (imgCount == 0) {
            itemPostObject.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException e) {
                    mProgressDialog.dismiss();
                    if (e == null) {
                        parentPost.add(Constants.POST_HASH_TAGS, categorySelector.getSelectedItem().toString());
                        parentPost.add(Constants.POST_SALE_TYPE, preparingItemSaleType);
                        //parentPost.add(Constants.POST_LOCATION, locationSelector.getSelectedItem().toString());
                        parentPost.add(Constants.POST_LOCATION_CITY, strCity);
                        parentPost.add(Constants.POST_LOCATION_COUNTRY, strCountry);
                        parentPost.add(Constants.POST_CONDITION, conditionSelector.getSelectedItem().toString());
                        parentPost.saveEventually();
                        if (straightPublish) {
                            parentPostId = null;
                            editPhotoActivity.finish();
                            finish();
                        } else {
                            editPhotoActivity.finish();
                            mainActivity.promptToEdit();
                            finish();
                        }
                    } else {
                        e.printStackTrace();
                        finish();
                    }
                }
            });
        }
    }
}