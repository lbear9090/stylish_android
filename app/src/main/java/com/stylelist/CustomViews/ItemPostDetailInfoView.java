package com.stylelist.CustomViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stylelist.Interfaces.ItemPostClickCallback;
import com.stylelist.ParseModels.ItemPost;
import com.stylelist.R;
import com.stylelist.Utils.Constants;
import com.stylelist.Utils.UtilFunctions;

/**
 * Created by Security on 3/22/2018.
 */

public class ItemPostDetailInfoView extends RelativeLayout {

    private TextView txtItemName, txtOwnCount, txtItemPrice, txtOwnCountText;
    private ItemPost currentItemPost;
    private ItemPostClickCallback itemPostClickCallback;

    public ItemPostDetailInfoView(Context context, ItemPost itemPost, ItemPostClickCallback itemPostClickCallback) {
        super(context);
        this.currentItemPost = itemPost;
        this.itemPostClickCallback = itemPostClickCallback;
        initView(context);
    }

    public ItemPostDetailInfoView(Context context) {
        super(context);
        initView(context);
    }

    public ItemPostDetailInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ItemPostDetailInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_info_container, this);
        txtItemName = findViewById(R.id.text_post_item_item_tag_name);
        txtItemPrice = findViewById(R.id.text_post_item_price);
        txtOwnCount = findViewById(R.id.text_post_item_own_number);
        txtOwnCountText = findViewById(R.id.text_item_post_own_count_text);

        txtItemName.setText(currentItemPost.getHashTag());
        if (currentItemPost.getSaleType().equals(Constants.FOR_SALE)) {
            txtItemPrice.setText("BUY $" + currentItemPost.getItemPrice());
        } else if (currentItemPost.getSaleType().equals(Constants.FOR_HIRE)) {
            txtItemPrice.setText("HIRE $" + currentItemPost.getItemPrice());
        } else {
            txtItemPrice.setText("");
        }

        txtItemName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPostClickCallback.onItemClick(currentItemPost);
            }
        });
        int ownCount = UtilFunctions.getOwnCount(currentItemPost.getObjectId());
        txtOwnCount.setText(String.valueOf(ownCount));
        if (ownCount == 0) {
            txtOwnCount.setVisibility(GONE);
            txtOwnCountText.setVisibility(GONE);
        } else if (ownCount == 1) {
            txtOwnCountText.setText("Person owns this.");
        } else {
            txtOwnCountText.setText("People own this.");
        }

    }
}
