package com.stylelist.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;
import com.stylelist.R;
import com.stylelist.Utils.Constants;
import com.stylelist.Utils.PaymentController;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

import static com.stylelist.Utils.StyleListApp.allActivityArray;
import static com.stylelist.Utils.StyleListApp.currentItemPost;
import static com.stylelist.Utils.StyleListApp.parentPost;

public class ItemPostDetailActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private CustomFontButton btnBack, btnBuy;
    private ImageView btnOwn, btnBookmark;
    private CircleImageView profileImageView;
    private CustomFontTextView txtTitle, txtItemName, txtItemCategory, txtItemDescription, txtUserName, txtFullName, txtTimeStamp, txtCondition, txtMeetValue, txtShippingValue, txtAddress, txtItemPrice, txtDepositPrice, txtShippingPrice, txtTotalPrice, txtMIP;
    private RelativeLayout depositPriceContainer;
    private ViewPager imageViewPager;
    private CircleIndicator pagerIndicator;
    private ArrayList<ParseFile> imageFiles = new ArrayList<>();

    private Switch switchMIP, switchShipping;

    public ProgressDialog mProgressDialog;
    private float totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_post_detail);

        btnBack = findViewById(R.id.btn_item_post_detail_cancel);
        btnBuy = findViewById(R.id.btn_item_post_buy);
        btnOwn = findViewById(R.id.btn_item_post_own);
        btnBookmark = findViewById(R.id.btn_item_post_bookmark);
        profileImageView = findViewById(R.id.image_item_post_profile);
        depositPriceContainer = findViewById(R.id.item_post_deposit_price_container);
        txtTitle = findViewById(R.id.text_item_post_sale_title);
//        txtItemNameView = findViewById(R.id.text_item_post_item_name_view);
//        txtOwnCount = findViewById(R.id.text_item_post_own_count);
//        txtItemPriceView = findViewById(R.id.text_item_post_price);
        txtItemName = findViewById(R.id.text_item_post_item_name);
        txtItemCategory = findViewById(R.id.text_item_post_hash_tag);
        txtItemDescription = findViewById(R.id.text_item_post_item_description);
        txtUserName = findViewById(R.id.text_item_post_username);
        txtFullName = findViewById(R.id.text_item_post_full_name);
        txtTimeStamp = findViewById(R.id.text_item_post_timestamp);
        txtCondition = findViewById(R.id.text_item_post_condition);
        txtMeetValue = findViewById(R.id.text_item_post_meet_in_value);
        txtShippingValue = findViewById(R.id.text_item_post_shipping_value);
        txtAddress = findViewById(R.id.text_item_post_address);
        txtItemPrice = findViewById(R.id.text_item_post_item_price);
        txtMIP = findViewById(R.id.txt_item_post_mip);
        txtDepositPrice = findViewById(R.id.text_item_post_deposit_price);
        txtShippingPrice = findViewById(R.id.text_item_post_shipping_price);
        txtTotalPrice = findViewById(R.id.text_item_post_total_price);
        imageViewPager = findViewById(R.id.pager_item_post_images);
        pagerIndicator = findViewById(R.id.pager_indicator_item_post_images);

        switchMIP = findViewById(R.id.switch_item_post_meet_in_value);
        switchShipping = findViewById(R.id.switch_item_post_shipping_value);

        switchMIP.setOnCheckedChangeListener(this);
        switchShipping.setOnCheckedChangeListener(this);
        initUI();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PaymentController.sharedInstance().stopPaypalService(this);
    }

    private void initUI() {
        float totalPrice = 5.0f;
        btnOwn.setOnClickListener(this);
        btnBookmark.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        showImages();
        try {
            Picasso.with(this).load(UtilFunctions.findPostById(currentItemPost.getPostId()).getUser().fetchIfNeeded().getParseFile("profilePictureSmall").getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(profileImageView);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtTitle.setText(currentItemPost.getHashTagName());
//        txtItemNameView.setText(currentItemPost.getHashTagName());
//        txtOwnCount.setText(String.valueOf(UtilFunctions.getOwnCount(currentItemPost.getObjectId())) + " people own this");
        txtItemName.setText(currentItemPost.getHashTagName());
        txtItemCategory.setText("#" + currentItemPost.getHashTag());
        txtItemDescription.setText(currentItemPost.getHashTagDescription());
        txtCondition.setText(currentItemPost.getConditionDetail());
        txtMIP.setText("Meet In Person - " + currentItemPost.getLocationCityInfo() + "(" + currentItemPost.getLocationCountryInfo() + ")");
        //txtAddress.setText(currentItemPost.getLocationCityInfo() + "(" + currentItemPost.getLocationCountryInfo() + ")");
        txtItemPrice.setText("$" + currentItemPost.getItemPrice());
        txtDepositPrice.setText("$" + currentItemPost.getDepositPrice());
        //txtShippingPrice.setText("$5.00");
        try {
            txtUserName.setText("@" + UtilFunctions.findPostById(currentItemPost.getPostId()).getUser().fetchIfNeeded().getString("usernameFix"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtFullName.setText(UtilFunctions.findPostById(currentItemPost.getPostId()).getUser().getString("displayName"));
        txtTimeStamp.setText(UtilFunctions.getTimeStamp(currentItemPost.getCreatedAt()));
        switch (currentItemPost.getSaleType()) {
            case Constants.FOR_SALE:
                //totalPrice = totalPrice + Float.valueOf(currentItemPost.getItemPrice());
//                txtItemPriceView.setText("BUY $" + currentItemPost.getItemPrice());
                depositPriceContainer.setVisibility(View.GONE);
                setDeliveryOption();
                setPrice(true);
                break;
            case Constants.FOR_HIRE:
//                txtItemPriceView.setText("HIRE $" + currentItemPost.getItemPrice());
                //totalPrice = totalPrice + Float.valueOf(currentItemPost.getItemPrice());
                //totalPrice = totalPrice + Float.valueOf(currentItemPost.getDepositPrice());
                setDeliveryOption();
                setPrice(true);
                break;
            default:
//                txtItemPriceView.setText("");
                break;
        }
        //String s = String.format("%.2f", totalPrice);
        //txtTotalPrice.setText("$" + s);
        //this.totalPrice = totalPrice;
        //txtShippingValue.setText(currentItemPost.getShippingValue());
        //txtMeetValue.setText(currentItemPost.getMeetValue());

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Submitting...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        btnOwn.setSelected(UtilFunctions.isOwned(currentItemPost.getObjectId()));
        btnBookmark.setSelected(UtilFunctions.isBookMarked(currentItemPost.getObjectId()));

        PaymentController.sharedInstance().startPaypalService(this);
    }

    private void setDeliveryOption(){
        String sMIP = currentItemPost.getMeetValue();
        String sShipping = currentItemPost.getShippingValue();

        Boolean isMIP = sMIP.equalsIgnoreCase("Yes");
        Boolean isShipping = sShipping.equalsIgnoreCase("Yes");

        if(isMIP && isShipping){
            switchMIP.setEnabled(true);
            switchShipping.setEnabled(true);

            switchMIP.setChecked(true);
            switchShipping.setChecked(false);
        }else{
            switchMIP.setEnabled(false);
            switchShipping.setEnabled(false);

            switchMIP.setChecked(isMIP);
            switchShipping.setChecked(isShipping);
        }
    }

    private void setPrice(Boolean isLoading){
        Boolean isMIP = switchMIP.isChecked();
        Boolean isShipping = switchShipping.isChecked();
        float itemPrice = Float.parseFloat(currentItemPost.getItemPrice());
        float depositPrice = Float.parseFloat(currentItemPost.getDepositPrice());

        float shipPrice = 0;

        if(currentItemPost.getSaleType() == Constants.FOR_SALE)
            depositPrice = 0;

        if(isMIP){
            shipPrice = 0;
        }else if(isShipping){
            String sInt = currentItemPost.getIntShipCost();
            String sDom = currentItemPost.getDomShipCost();
            String sCountry = currentItemPost.getLocationCountryInfo();

            ParseUser user = ParseUser.getCurrentUser();
            String sUserCountry = user.getString("Country");

            Boolean isSameCountry = sCountry.equalsIgnoreCase(sUserCountry);

            if(isSameCountry && (sDom != null && !sDom.equalsIgnoreCase(""))){
                shipPrice = Float.parseFloat(sDom);
            }else if(!isSameCountry && (sInt != null && !sInt.equalsIgnoreCase(""))){
                shipPrice = Float.parseFloat(sInt);
            }else{
                shipPrice = -1;
            }

            if(shipPrice < 0){
                Toast.makeText(this, "Sorry, Shipping method isn't eligible to you.", Toast.LENGTH_LONG).show();

                if(isLoading){
                    this.finish();
                    return;
                }else{
                    switchMIP.setChecked(true);
                    switchShipping.setChecked(false);
                    shipPrice = 0;
                }
            }
        }

        float totalPrice = itemPrice + depositPrice + shipPrice;
        txtShippingPrice.setText(String.format("$%.2f", shipPrice));
        txtTotalPrice.setText(String.format("$%.2f", totalPrice));
    }
    private void showImages() {
        imageFiles.clear();
        Post parent = UtilFunctions.findPostById(currentItemPost.getPostId());
        if (parent != null) {
            imageFiles.add(parent.getImage());
        }
        if (currentItemPost.getHashTagImageOne() != null) {
            imageFiles.add(currentItemPost.getHashTagImageOne());
        }
        if (currentItemPost.getHashTagImageTwo() != null) {
            imageFiles.add(currentItemPost.getHashTagImageTwo());
        }
        if (currentItemPost.getHashTagImageThree() != null) {
            imageFiles.add(currentItemPost.getHashTagImageThree());
        }
        if (currentItemPost.getHashTagImageFour() != null) {
            imageFiles.add(currentItemPost.getHashTagImageFour());
        }
        if (currentItemPost.getHashTagImageFive() != null) {
            imageFiles.add(currentItemPost.getHashTagImageFive());
        }
        if (imageFiles.size() > 0) {
            PagerAdapter pagerAdapter = new PagerAdapter() {
                @Override
                public int getCount() {
                    return imageFiles.size();
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    LayoutInflater mLayoutInflater = (LayoutInflater) ItemPostDetailActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View itemView = mLayoutInflater.inflate(R.layout.style_detail_example_item, container, false);
                    ImageView imageView = itemView.findViewById(R.id.image_style_detail_example);
                    Picasso.with(ItemPostDetailActivity.this).load(imageFiles.get(position).getUrl()).placeholder(R.drawable.placeholder_photo).into(imageView);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    container.addView(itemView);

                    return itemView;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }
            };
            imageViewPager.setAdapter(pagerAdapter);
            pagerIndicator.setViewPager(imageViewPager);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            onBackPressed();
        } else if (v == btnBookmark) {
            bookMarkAction();
        } else if (v == btnOwn) {
            ownAction();
        } else if (v == btnBuy) {
            buyAction();
        }
    }

    private void bookMarkAction() {
        mProgressDialog.show();
        if (UtilFunctions.isBookMarked(currentItemPost.getObjectId())) {
            ParseQuery<Notification> deleteQuery = new ParseQuery<>("Notification");
            deleteQuery.whereMatches("type", "bookmarked");
            deleteQuery.whereEqualTo("fromUser", ParseUser.getCurrentUser());
            deleteQuery.whereEqualTo("itemObjectId", currentItemPost.getObjectId());
            deleteQuery.getFirstInBackground(new GetCallback<Notification>() {
                public void done(Notification object, ParseException e) {
                    mProgressDialog.dismiss();
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        object.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                btnBookmark.setSelected(false);
                                UtilFunctions.removeBookMark(currentItemPost.getObjectId());
                            }
                        });
                    }
                }
            });
        } else {
            final Notification bookMark = new Notification();
            bookMark.put("itemObjectId", currentItemPost.getObjectId());
            bookMark.put("postId", currentItemPost.getPostId());
            bookMark.put("post", UtilFunctions.findPostById(currentItemPost.getPostId()));
            bookMark.put("toUser", UtilFunctions.findPostById(currentItemPost.getPostId()).getUser());
            bookMark.put("fromUser", ParseUser.getCurrentUser());
            bookMark.put("type", "bookmarked");
            bookMark.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    mProgressDialog.dismiss();
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        Log.e("Log", "Own submitted");
                        btnBookmark.setSelected(true);
                        allActivityArray.add(bookMark);
                    }
                }
            });
        }
    }

    private void ownAction() {
        mProgressDialog.show();
        if (UtilFunctions.isOwned(currentItemPost.getObjectId())) {
            ParseQuery<Notification> deleteQuery = new ParseQuery<>("Notification");
            deleteQuery.whereMatches("type", "owned");
            deleteQuery.whereEqualTo("fromUser", ParseUser.getCurrentUser());
            deleteQuery.whereEqualTo("itemObjectId", currentItemPost.getObjectId());
            deleteQuery.getFirstInBackground(new GetCallback<Notification>() {
                public void done(Notification object, ParseException e) {
                    mProgressDialog.dismiss();
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        object.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                btnOwn.setSelected(false);
                                UtilFunctions.removeOwn(currentItemPost.getObjectId());
                            }
                        });
                    }
                }
            });
        } else {
            final Notification ownNotification = new Notification();
            ownNotification.put("itemObjectId", currentItemPost.getObjectId());
            ownNotification.put("postId", currentItemPost.getPostId());
            ownNotification.put("post", UtilFunctions.findPostById(currentItemPost.getPostId()));
            ownNotification.put("toUser", UtilFunctions.findPostById(currentItemPost.getPostId()).getUser());
            ownNotification.put("fromUser", ParseUser.getCurrentUser());
            ownNotification.put("type", "owned");
            ownNotification.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    mProgressDialog.dismiss();
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        Log.e("Log", "Own submitted");
                        btnOwn.setSelected(true);
                        allActivityArray.add(ownNotification);
                    }
                }
            });
        }
    }

    // Todo: Integrate Payment method
    private void buyAction() {
        PaymentController.sharedInstance().buyItem(this, totalPrice, "USD", txtItemName.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PaymentController.sharedInstance().activityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView == switchMIP){
            if(isChecked && switchShipping.isChecked()){
                switchShipping.setChecked(false);
            }else if(!isChecked && !switchShipping.isChecked()){
                switchShipping.setChecked(true);
            }
            setPrice(false);
        }else if(buttonView == switchShipping){
            if(isChecked && switchMIP.isChecked()){
                switchMIP.setChecked(false);
            }else if(!isChecked && !switchMIP.isChecked()){
                switchMIP.setChecked(true);
            }
            setPrice(false);
        }
    }
}
