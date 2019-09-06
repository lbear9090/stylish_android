package com.stylelist.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.EditPhotoUtils.Base.Utils;
import com.stylelist.Models.ItemRect;
import com.stylelist.R;
import com.stylelist.Utils.UtilFunctions;

import java.io.ByteArrayOutputStream;

import static com.stylelist.Utils.StyleListApp.currentItemViewRect;
import static com.stylelist.Utils.StyleListApp.editedPhoto;
import static com.stylelist.Utils.StyleListApp.isPhotoEdited;
import static com.stylelist.Utils.StyleListApp.preparingItemSaleType;
import static com.stylelist.Utils.StyleListApp.selectedStyles;
import static com.stylelist.Utils.UtilFunctions.getOwnCount;
import static com.stylelist.Utils.UtilFunctions.scaleDown;

public class PreparePostActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnTouchListener {


    private CustomFontButton btnCancel, btnPublish;
    private EditText edtComment;
    private Spinner tagSelector;
    private ImageView imgPostImage;
    private TextView txtWhatCategory, draggingView;
    private RelativeLayout commentContainer;
    private LinearLayout saleTypeContainer;

    private boolean straightPublish = true;
    public ProgressDialog mProgressDialog;

    public byte[] photoByte;
    public byte[] thumbByte;

    public ParseFile photo;
    public ParseFile thumbnail;

    // values for dragging
    float dX;
    float dY;
    int lastAction;
    int top, bottom,right, left, width, height;
    int dTop, dBottom, dRight, dLeft, dWidth, dHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_post);

        initUI();
    }

    private void initUI() {
        btnCancel = findViewById(R.id.btn_prepare_post_cancel);
        btnPublish = findViewById(R.id.btn_prepare_publish);
        edtComment = findViewById(R.id.edit_prepare_post_comment);
        commentContainer = findViewById(R.id.prepare_post_comment_container);
        saleTypeContainer = findViewById(R.id.container_0001);
        imgPostImage = findViewById(R.id.image_prepare_post_post_image);
        tagSelector = findViewById(R.id.spinner_tag_selector);
        txtWhatCategory = findViewById(R.id.text_drag_item_description);
//        edtItemTagName = findViewById(R.id.edit_prepare_post_item_tag_name);
        draggingView = findViewById(R.id.text_prepare_post_dragging);

        if (!isPhotoEdited) {
//            edtItemTagName.setVisibility(View.GONE);
            draggingView.setVisibility(View.GONE);
            commentContainer.setVisibility(View.VISIBLE);
            saleTypeContainer.setVisibility(View.INVISIBLE);
            btnPublish.setText("Publish");
        } else {
            saleTypeContainer.setVisibility(View.VISIBLE);
            draggingView.setVisibility(View.VISIBLE);
            commentContainer.setVisibility(View.GONE);
//            edtItemTagName.setVisibility(View.VISIBLE);
            btnPublish.setText("Next");
        }

        btnCancel.setOnClickListener(this);
        btnPublish.setOnClickListener(this);

        Bitmap result = UtilFunctions.removeTransparentArea(editedPhoto);
        showPhoto(result);
        tagSelector.setOnItemSelectedListener(this);

        hideEditTexts();
        draggingView.setOnTouchListener(this);
    }

    private void hideEditTexts() {
//        edtItemTagName.setVisibility(View.GONE);
//        edtComment.setVisibility(View.GONE);
    }

    private void showPhoto(Bitmap bitmap) {
        prepareImages(bitmap);
        imgPostImage.setImageBitmap(bitmap);
    }

    private Bitmap getBitmap(String inputUrl) {
        Bitmap bitmap = Utils.getBitmapSdcard(inputUrl);
        bitmap = Utils.scaleDown(bitmap);
        return bitmap;
    }

    private void saveViewPosition() {
        currentItemViewRect = new ItemRect();
        currentItemViewRect.xPosition = (draggingView.getX() - dLeft)/dWidth;
        currentItemViewRect.yPosition = (draggingView.getY() - dTop)/dHeight;
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancel) {
            onBackPressed();
        } else if (v == btnPublish) {
            if (btnPublish.getText().toString().equals("Next")) {
                preparingItemSaleType = tagSelector.getSelectedItem().toString();
                saveViewPosition();
//                preparingItemTagName = edtItemTagName.getText().toString();
                Intent intent = new Intent(PreparePostActivity.this, PublishDetailActivity.class);
                startActivity(intent);
                finish();
            } else {
                publishPost();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        tagSelector.setSelection(0);
    }

    private void prepareImages(Bitmap bitmap) {
        ByteArrayOutputStream photoStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, photoStream);
        photoByte = photoStream.toByteArray();
        Bitmap thumbnailBit = scaleDown(bitmap, 86, true);
        ByteArrayOutputStream photoStreamThumb = new ByteArrayOutputStream();
        thumbnailBit.compress(Bitmap.CompressFormat.PNG, 100, photoStreamThumb);
        thumbByte = photoStreamThumb.toByteArray();
    }

    private void publishPost() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Uploading...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        if (photoByte != null && photoByte.length > 0 && thumbByte != null && thumbByte.length > 0) {
            photo = new ParseFile("file", photoByte);
            thumbnail = new ParseFile("file", thumbByte);
        }
        publish(photo, thumbnail);
    }

    public void publish(ParseFile file, ParseFile thumbnail){

        final ParseObject parseObject = ParseObject.create("Post");

        if (selectedStyles.size() > 0) {
            parseObject.put("Styles", selectedStyles);
        }
        parseObject.put("user", ParseUser.getCurrentUser());
        parseObject.put("image", file);
        parseObject.put("thumbnail", thumbnail);
        parseObject.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null) {
                    String firstComment = edtComment.getText().toString().trim();
                    if (firstComment.length() < 1) {
                        mProgressDialog.dismiss();
                        if (straightPublish) {
                            finish();
                        }
                    } else {
//                        submit with first comment
                        submitComment(parseObject);
                    }
                } else {
                    finish();
                }
            }
        });
    }

    private void submitComment(ParseObject post) {

        ParseObject comment = ParseObject.create("Notification");
        comment.put("content", edtComment.getText().toString());
        comment.put("toUser", ParseUser.getCurrentUser());
        comment.put("fromUser", ParseUser.getCurrentUser());
        comment.put("type", "comment");
        comment.put("post", post);
        comment.put("postId", post.getObjectId());

        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    mProgressDialog.dismiss();
                    finish();
                } else {
                    e.printStackTrace();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = draggingView.getX() - event.getRawX();
                dY = draggingView.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                draggingView.setY(event.getRawY() + dY);
                draggingView.setX(event.getRawX() + dX);
                if (draggingView.getX() + draggingView.getWidth() > dRight) {
                    draggingView.setX(dRight - draggingView.getWidth());
                }
                if (draggingView.getX() < dLeft) {
                    draggingView.setX(dLeft);
                }
                if (draggingView.getY() > dBottom) {
                    draggingView.setY(dBottom);
                }
                if (draggingView.getY() < dTop) {
                    draggingView.setY(dTop);
                }
                lastAction = MotionEvent.ACTION_MOVE;
                break;
            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getRectOfBitmap();
    }

    private void getRectOfBitmap() {
        top = imgPostImage.getTop();
        bottom = imgPostImage.getBottom();
        right = imgPostImage.getRight();
        left = imgPostImage.getLeft();
        width = imgPostImage.getMeasuredWidth();
        height = imgPostImage.getMeasuredHeight();
        float ratio = (float) width/(float) height;
        dWidth = imgPostImage.getDrawable().getIntrinsicWidth();
        dHeight = imgPostImage.getDrawable().getIntrinsicHeight();
        float dRatio = (float) dWidth/(float)dHeight;
        if (ratio > dRatio) {
            // Portrait
            dWidth = (int) (dWidth * ((float)height/(float) dHeight));
            dHeight = height;
            dBottom = bottom - top;
            dTop = top;
        } else {
            // Landscape
            dHeight = (int) (dHeight * ((float)width/(float) dWidth));
            dWidth = width;
            dTop = top + (height - dHeight) / 2;
            dBottom = bottom - top - (height - dHeight) / 2;
        }
        dLeft = left + (width - dWidth) / 2;
        dRight = dLeft + dWidth;
    }
}
