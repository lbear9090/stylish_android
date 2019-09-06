package com.stylelist.EditPhotoUtils.Crop;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stylelist.EditPhotoUtils.Base.EditPhotoActivity;
import com.stylelist.EditPhotoUtils.Crop.Cropper.CropImageView;
import com.stylelist.R;

/**
 * Created by Hoang Anh Tuan on 11/15/2017.
 */

public class CropperFragment extends Fragment implements
        View.OnClickListener {

    CropImageView ivCrop;
    Button ivCancel;
    TextView tvTitle;
    Button ivCheck;
    LinearLayout controller;
    RelativeLayout rootCrop;

    private EditPhotoActivity editPhotoActivity;
    private Bitmap bitmap;

    public static CropperFragment newInstance(Bitmap inputUrl, OnCropListener onCropListener) {
        CropperFragment fragment = new CropperFragment();
        fragment.setOnCropListener(onCropListener);
        fragment.bitmap = inputUrl;
        return fragment;
    }

    private OnCropListener onCropListener;

    public void setOnCropListener(OnCropListener onCropListener) {
        this.onCropListener = onCropListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop, container, false);
        mappingView(view);
        return view;
    }

    private void mappingView(View view) {
        ivCrop = view.findViewById(R.id.ivCrop);
        ivCancel = view.findViewById(R.id.ivCancel);
        tvTitle = view.findViewById(R.id.tvTitle);
        ivCheck = view.findViewById(R.id.ivCheck);
        controller = view.findViewById(R.id.controller);
        rootCrop = view.findViewById(R.id.rootCrop);

        ivCancel.setOnClickListener(this);
        ivCheck.setOnClickListener(this);
        rootCrop.setOnClickListener(this);

        editPhotoActivity = (EditPhotoActivity)getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        ivCrop.setImageBitmap(bitmap);
    }

    private void saveImage() {
        if (onCropListener != null)
            onCropListener.onCropPhotoCompleted(ivCrop.getCroppedImage());
        back();
    }

    private void back() {
        hideLoading();
        getActivity().onBackPressed();
    }

    private void hideLoading() {
//        if (ivLoading != null)
//            ivLoading.smoothToHide();
    }

    private void showLoading() {
//        if (ivLoading != null)
//            ivLoading.smoothToShow();
    }

    @Override
    public void onClick(View view) {
//        if (ivLoading.isShown()) return;
        if (view.getId() == R.id.ivCancel) {
            back();
        } else if (view.getId() == R.id.ivCheck) {
            saveImage();
            editPhotoActivity.editActionAcount++;
        }
    }
}
