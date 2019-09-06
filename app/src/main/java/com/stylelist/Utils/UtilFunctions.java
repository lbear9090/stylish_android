package com.stylelist.Utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import com.parse.ParseUser;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import static com.stylelist.Utils.StyleListApp.allActivityArray;
import static com.stylelist.Utils.StyleListApp.allPostArray;
import static com.stylelist.Utils.StyleListApp.allUsers;

/**
 * Created by security on 2/14/2018.
 */

public class UtilFunctions {


    public static ParseUser findUserWithId(String id) {
        for (ParseUser user : allUsers) {
            if (user.getObjectId().equals(id))
                return user;
        }
        return null;
    }

    public static Post findPostById(String id) {
        for (Post post : allPostArray) {
            if (post.getObjectId().equals(id))
                return post;
        }
        return null;
    }

    public static int getPostCount(ParseUser user) {
        int result = 0;
        for (Post post : allPostArray) {
            ParseUser tempUser = post.getUser();
            if (tempUser != null && tempUser.getObjectId().equals(user.getObjectId()))
                result++;
        }
        return result;
    }

    public static boolean isFollowing(ParseUser parseUser) {
        ArrayList<ParseUser> followingUsers = getFollowingUsers(ParseUser.getCurrentUser());
        for (ParseUser user : followingUsers) {
            if (user.getObjectId().equals(parseUser.getObjectId()))
                return true;
        }
        return false;
    }

    public static ArrayList<ParseUser> getFollowingUsers(ParseUser user) {
        ArrayList<ParseUser> followingUsers = new ArrayList<>();
        for (Notification notification : allActivityArray) {
            if (notification.getType() != null && notification.getType().equals("follow")) {
                ParseUser fromUser = notification.getFromUser();
                if (fromUser.getObjectId().equals(user.getObjectId())) {
                    ParseUser toUser = notification.getToUser();
                    followingUsers.add(toUser);
                }
            }
        }
        return followingUsers;
    }

    public static ArrayList<ParseUser> getFollowerUsers(ParseUser user) {
        ArrayList<ParseUser> followerUsers = new ArrayList<>();
        for (Notification notification : allActivityArray) {
            if (notification.getType() != null && notification.getType().equals("follow")) {
                ParseUser toUser = notification.getToUser();
                if (toUser.getObjectId().equals(user.getObjectId())) {
                    ParseUser fromUser = notification.getFromUser();
                    followerUsers.add(fromUser);
                }
            }
        }
        return followerUsers;
    }

    public static int getFollowerCount(ParseUser user) {
        int result = 0;
        for (int i = 0; i < allActivityArray.size(); i++) {
            if (allActivityArray.get(i).getToUser() != null && allActivityArray.get(i).getToUser().getObjectId().equals(user.getObjectId()) && allActivityArray.get(i).getType().equals("follow")) {
                result++;
            }
        }
        return result;
    }

    public static int getFollowingCount(ParseUser user) {
        int result = 0;
        for (int i = 0; i < allActivityArray.size(); i++) {
            if (allActivityArray.get(i).getFromUser() != null && allActivityArray.get(i).getFromUser().getObjectId().equals(user.getObjectId()) && allActivityArray.get(i).getType().equals("follow")) {
                result++;
            }
        }
        return result;
    }

    public static int getOwnCount(String itemId) {
        int count = 0;
        for (int i = 0; i < allActivityArray.size(); i++) {
            if (allActivityArray.get(i).getType().equals("owned")
                    && allActivityArray.get(i).getString("itemObjectId").equals(itemId)) {
                count++;
            }
        }
        return count;
    }

    public static ArrayList<Post> getBookMarkPosts() {
        ArrayList<Post> result = new ArrayList<>();
        for (Notification notification : allActivityArray) {
            if (notification.getType().equals("bookmarked") && notification.getFromUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                result.add(UtilFunctions.findPostById(notification.getPostId()));
                //result.add(notification.getPost());
            }
        }
        return result;
    }

    public static boolean isOwned(String itemId) {
        for (int i = 0; i < allActivityArray.size(); i++) {
            if (allActivityArray.get(i).getType().equals("owned")
                    && allActivityArray.get(i).getFromUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())
                    && allActivityArray.get(i).getString("itemObjectId").equals(itemId)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBookMarked(String itemId) {
        for (int i = 0; i < allActivityArray.size(); i++) {
            if (allActivityArray.get(i).getType().equals("bookmarked")
                    && allActivityArray.get(i).getFromUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())
                    && allActivityArray.get(i).getString("itemObjectId").equals(itemId)) {
                return true;
            }
        }
        return false;
    }

    public static void removeOwn(String itemId) {
        for (int i = 0; i < allActivityArray.size(); i++) {
            if (allActivityArray.get(i).getType().equals("owned")
                    && allActivityArray.get(i).getFromUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())
                    && allActivityArray.get(i).getString("itemObjectId").equals(itemId)) {
                allActivityArray.remove(i);
            }
        }
    }

    public static void removeBookMark(String itemId) {
        for (int i = 0; i < allActivityArray.size(); i++) {
            if (allActivityArray.get(i).getType().equals("bookmarked")
                    && allActivityArray.get(i).getFromUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())
                    && allActivityArray.get(i).getString("itemObjectId").equals(itemId)) {
                allActivityArray.remove(i);
            }
        }
    }

    public static void removeFollow(String userId) {
        int index = -1;
        for (Notification notification : allActivityArray) {
            if (notification.getType().equals("follow")
                    && notification.getFromUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())
                    && notification.getToUser().getObjectId().equals(userId)) {
                index = allActivityArray.indexOf(notification);
            }
        }
        if (index != -1)
            allActivityArray.remove(index);
    }

    public static String getTimeStamp(Date createdAt) {

        Date now = new Date();

        long elapsedYears;
        long elapsedMonths;
        long elapsedWeeks;
        long elapsedDays ;
        long elapsedHours ;
        long elapsedMinutes;
        long elapsedSeconds;

        //milliseconds
        long different = now.getTime() - createdAt.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long weeksInMilli = daysInMilli * 7;
        long monthsInMilli = daysInMilli * 30;
        long yearsInMilli = monthsInMilli * 12;


        elapsedYears = different / yearsInMilli;
        different = different % yearsInMilli;

        elapsedMonths = different / monthsInMilli;
        different = different % monthsInMilli;

        elapsedWeeks = different / weeksInMilli;
        different = different % weeksInMilli;

        elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        elapsedSeconds = different / secondsInMilli;


        if (elapsedYears == 0 && elapsedMonths == 0 && elapsedWeeks == 0 && elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes == 0) {
            //show only Seconds
            return "" + elapsedSeconds + "s";
        } else if (elapsedYears == 0 && elapsedMonths == 0 && elapsedWeeks == 0 && elapsedDays == 0 && elapsedHours == 0) {
            //show only Minutes
            return "" + elapsedMinutes + "m";
        } else if (elapsedYears == 0 && elapsedMonths == 0 && elapsedWeeks == 0 && elapsedDays == 0){
            //show only Hours
            return "" + elapsedHours + "h";
        } else if (elapsedYears == 0 && elapsedMonths == 0 && elapsedWeeks == 0){
            //show only Days
            return "" + elapsedDays + "d";
        } else if (elapsedYears == 0 && elapsedMonths == 0){
            //show only Weeks
            if(elapsedWeeks <= 1){
                return  "" + elapsedWeeks + "wk";
            } else {
                return  "" + elapsedWeeks + "wks";
            }
        } else if (elapsedYears == 0){
            //show only Months
            if(elapsedMonths <= 1) {
                return  "" + elapsedMonths + "mo";
            } else{
                return  "" + elapsedMonths + "mos";
            }
        } else {
            //show only Years
            if(elapsedYears <= 1) {
                return  "" + elapsedYears + "y";
            } else{
                return  "" + elapsedYears + "ys";
            }
        }
    }

    public static String getTimeStampFromMilliSeconds(long milliseconds) {
        Date now = new Date();
        long elapsedYears;
        long elapsedMonths;
        long elapsedWeeks;
        long elapsedDays ;
        long elapsedHours ;
        long elapsedMinutes;
        long elapsedSeconds;

        //milliseconds
        long different = now.getTime() - milliseconds;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long weeksInMilli = daysInMilli * 7;
        long monthsInMilli = daysInMilli * 30;
        long yearsInMilli = monthsInMilli * 12;


        elapsedYears = different / yearsInMilli;
        different = different % yearsInMilli;

        elapsedMonths = different / monthsInMilli;
        different = different % monthsInMilli;

        elapsedWeeks = different / weeksInMilli;
        different = different % weeksInMilli;

        elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        elapsedSeconds = different / secondsInMilli;


        if (elapsedYears == 0 && elapsedMonths == 0 && elapsedWeeks == 0 && elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes == 0) {
            //show only Seconds
            return "" + elapsedSeconds + "s";
        } else if (elapsedYears == 0 && elapsedMonths == 0 && elapsedWeeks == 0 && elapsedDays == 0 && elapsedHours == 0) {
            //show only Minutes
            return "" + elapsedMinutes + "m";
        } else if (elapsedYears == 0 && elapsedMonths == 0 && elapsedWeeks == 0 && elapsedDays == 0){
            //show only Hours
            return "" + elapsedHours + "h";
        } else if (elapsedYears == 0 && elapsedMonths == 0 && elapsedWeeks == 0){
            //show only Days
            return "" + elapsedDays + "d";
        } else if (elapsedYears == 0 && elapsedMonths == 0){
            //show only Weeks
            if(elapsedWeeks <= 1){
                return  "" + elapsedWeeks + "wk";
            } else {
                return  "" + elapsedWeeks + "wks";
            }
        } else if (elapsedYears == 0){
            //show only Months
            if(elapsedMonths <= 1) {
                return  "" + elapsedMonths + "mo";
            } else{
                return  "" + elapsedMonths + "mos";
            }
        } else {
            //show only Years
            if(elapsedYears <= 1) {
                return  "" + elapsedYears + "y";
            } else{
                return  "" + elapsedYears + "ys";
            }
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        return Bitmap.createScaledBitmap(realImage, width,
                height, filter);
    }

    public static Uri getUriFromUrl(String url) {
        URL reUrl = null;
        try {
            reUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (reUrl != null) {
            Uri.Builder builder = new Uri.Builder()
                    .scheme(reUrl.getProtocol())
                    .authority(reUrl.getAuthority())
                    .appendPath(reUrl.getPath());
            return builder.build();
        } else {
            return null;
        }
    }

    public static String pathFromUri(Context context, Uri uri) {
        String[] data = {MediaStore.Audio.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "temp", null);
        return Uri.parse(path);
    }

    public static Bitmap removeTransparentArea(Bitmap picToCrop) {
        int[] pixels = new int[picToCrop.getHeight() * picToCrop.getWidth()];
        int marginTop = 0, marginBottom = 0, marginLeft = 0, marginRight = 0, i;
        picToCrop.getPixels(pixels, 0, picToCrop.getWidth(), 0, 0,
                picToCrop.getWidth(), picToCrop.getHeight());

        for (i = 0; i < pixels.length; i++) {
            if (pixels[i] != Color.TRANSPARENT) {
                marginTop = i / picToCrop.getWidth();
                break;
            }
        }

        outerLoop1: for (i = 0; i < picToCrop.getWidth(); i++) {
            for (int j = i; j < pixels.length; j += picToCrop.getWidth()) {
                if (pixels[j] != Color.TRANSPARENT) {
                    marginLeft = j % picToCrop.getWidth();
                    break outerLoop1;
                }
            }
        }

        for (i = pixels.length - 1; i >= 0; i--) {
            if (pixels[i] != Color.TRANSPARENT) {
                marginBottom = (pixels.length - i) / picToCrop.getWidth();
                break;
            }
        }

        outerLoop2: for (i = pixels.length - 1; i >= 0; i--) {
            for (int j = i; j >= 0; j -= picToCrop.getWidth()) {
                if (pixels[j] != Color.TRANSPARENT) {
                    marginRight = picToCrop.getWidth()
                            - (j % picToCrop.getWidth());
                    break outerLoop2;
                }
            }
        }

        return Bitmap.createBitmap(picToCrop, marginLeft, marginTop,
                picToCrop.getWidth() - marginLeft - marginRight,
                picToCrop.getHeight() - marginTop - marginBottom);
    }
}
