package com.stylelist.Utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.stylelist.Activity.MainActivity;
import com.stylelist.EditPhotoUtils.Base.EditPhotoActivity;
import com.stylelist.Models.DeliveryModel;
import com.stylelist.Models.ItemRect;
import com.stylelist.Models.Style;
import com.stylelist.ParseModels.Hashtags;
import com.stylelist.ParseModels.ItemPost;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;

import java.util.ArrayList;


public class StyleListApp extends Application {

    public static ArrayList<Post> allPostArray = new ArrayList<>();
    public static ArrayList<Notification> allActivityArray = new ArrayList<>();
    public static ArrayList<ParseUser> allUsers = new ArrayList<>();
    public static ArrayList<ItemPost> allItemPosts = new ArrayList<>();

    public static ArrayList<Style> manStyles = new ArrayList<>();
    public static ArrayList<Style> womanStyles = new ArrayList<>();

    public static MainActivity mainActivity;
    public static EditPhotoActivity editPhotoActivity;

    public static Bitmap originalPhoto;
    public static Bitmap editedPhoto;
    public static Bitmap drawAreaBitmap;
    public static boolean isPhotoEdited = false;
    public static ArrayList<String> selectedStyles = new ArrayList<>();
    public static String parentPostId;
    public static Post parentPost;
    public static ItemRect currentItemRect, currentItemViewRect;
    // For Item publishing
    public static String preparingItemSaleType;
//    public static String preparingItemTagName;
    public static DeliveryModel currentItemDeliverySetting;
    public static ItemPost currentItemPost;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        //Register ParseMethods
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Notification.class);
        ParseObject.registerSubclass(Hashtags.class);
        ParseObject.registerSubclass(ItemPost.class);
        /*
        //****THIS IS THE OLD VERSION OF THE PARSE INITIALISATION, IF YOU DON'T WANT TO MIGRATE TO BACK4APP, THEN COMMENT OUT THE FOLLOWING:
        // Add your initialization code here
        Parse.initialize(this, "dk6SeFe6vRDKOghKJseDVCmeXVfcN9zpvnzmlFWe", "yTcYZYJXlw6kRx9Ol0seKJbChCb6Q2b9CX8AacjI");
        */

        //THIS IS THE NEW CODE TO MIGRATE TO BACK4APP.COM
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("JRVwfK7NC0xSWeGOVgEtyP7aJSa1LRXVY6Nejf1T")
                        .clientKey("8rq09cq9ZlRQD6Pza0ZwvDpPWlO8rUVOEJvY8FHm")
                        .server("https://parseapi.back4app.com").build()
        );
        //Facebook init
        ParseFacebookUtils.initialize(getApplicationContext());
        //Facebook init
        FacebookSdk.setApplicationId("949988298379222");
        //Enable automatic user
        ParseUser.enableAutomaticUser();

        for (int i = 0; i < Constants.MAN_STYLE_NAME.length; i++) {
            Style tempStyle = new Style();
            tempStyle.styleName = Constants.MAN_STYLE_NAME[i];
            tempStyle.styleDescription = Constants.MAN_STYLE_DESCRIPTION[i];
            tempStyle.styleImage = Constants.MAN_STYLE_IMAGE[i][0];
            manStyles.add(tempStyle);
        }

        for (int i = 0; i < Constants.WOMAN_STYLE_NAME.length; i++) {
            Style tempStyle = new Style();
            tempStyle.styleName = Constants.WOMAN_STYLE_NAME[i];
            tempStyle.styleDescription = Constants.WOMAN_STYLE_DESCRIPTION[i];
            tempStyle.styleImage = Constants.WOMAN_STYLE_IMAGE[i][0];
            womanStyles.add(tempStyle);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
