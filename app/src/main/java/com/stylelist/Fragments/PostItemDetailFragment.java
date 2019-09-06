package com.stylelist.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stylelist.ParseModels.Post;
import com.stylelist.R;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostItemDetailFragment extends Fragment {

    private TextView txtFullName, txtUserName, txtTimeStamp, txtItemName, txtItemCategory, txtItemSize, txtItemDescription, txtItemCondition, txtMeetPerson, txtShipping, txtAddress, txtPrice, txtShippingFee, txtTotalPrice;
    private ImageView imgProfile;
    private ViewPager imagePager;
    private CircleIndicator pagerIndicator;
    private Button btnBuy;

    private Post post;

    public PostItemDetailFragment() {
        // Required empty public constructor
    }

    public static PostItemDetailFragment newInstance(Post post) {
        PostItemDetailFragment fragment = new PostItemDetailFragment();
        fragment.post = post;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_item_detail, container, false);
        return view;
    }

}
