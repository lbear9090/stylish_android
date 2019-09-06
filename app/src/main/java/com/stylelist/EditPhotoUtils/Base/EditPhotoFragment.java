package com.stylelist.EditPhotoUtils.Base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.stylelist.Activity.PreparePostActivity;
import com.stylelist.EditPhotoUtils.Crop.CropperFragment;
import com.stylelist.EditPhotoUtils.Crop.OnCropListener;
import com.stylelist.EditPhotoUtils.Draw.DrawingFragment;
import com.stylelist.EditPhotoUtils.Draw.OnDrawListener;
import com.stylelist.EditPhotoUtils.Filter.FilterFragment;
import com.stylelist.EditPhotoUtils.Filter.OnFilterListener;
import com.stylelist.R;

import static com.stylelist.Utils.StyleListApp.drawAreaBitmap;
import static com.stylelist.Utils.StyleListApp.editedPhoto;
import static com.stylelist.Utils.StyleListApp.isPhotoEdited;

/**
 * Created by Hoang Anh Tuan on 11/23/2017.
 */

public class EditPhotoFragment extends Fragment implements
        EditAdapter.OnItemEditPhotoClickedListener,
        OnCropListener,
        OnFilterListener,
        View.OnClickListener, OnDrawListener {

    ImageView ivPhotoView;
    ImageView drawAreaImage;
//    AVLoadingIndicatorView ivLoading;
    RecyclerView listEdit;
    Button ivCancel;
    Button ivCheck;

    private EditPhotoActivity editPhotoActivity;

    public static EditPhotoFragment newInstance() {
        EditPhotoFragment fragment = new EditPhotoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_photo, container, false);
        ivPhotoView = view.findViewById(R.id.ivPhotoView);
        drawAreaImage = view.findViewById(R.id.image_draw_area);
//        ivLoading = view.findViewById(R.id.ivLoadingEdit);
        listEdit = view.findViewById(R.id.listEdit);
        ivCancel = view.findViewById(R.id.ivCancel);
        ivCheck = view.findViewById(R.id.ivCheck);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {

        EditAdapter editAdapter = new EditAdapter(getActivity());
        editAdapter.setOnItemEditPhotoClickedListener(this);
        listEdit.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listEdit.setAdapter(editAdapter);
        new StartSnapHelper().attachToRecyclerView(listEdit);

        editAdapter.add(EditType.Draw);
        editAdapter.add(EditType.Crop);
//        editAdapter.add(EditType.Filter);
//        editAdapter.add(EditType.Rotate);
//        editAdapter.add(EditType.Brightness);
//        editAdapter.add(EditType.Contrast);

        showPhoto(editedPhoto, null);

        ivCancel.setOnClickListener(this);
        ivCheck.setOnClickListener(this);
        editPhotoActivity = (EditPhotoActivity) getActivity();
    }


    private void showPhoto(Bitmap bitmap, @Nullable Bitmap drawBitmap) {
        ivPhotoView.setImageBitmap(bitmap);
        if (drawBitmap != null) {
            drawAreaImage.setImageBitmap(drawBitmap);
        } else {
            drawAreaImage.setImageBitmap(null);
        }
        editedPhoto = bitmap;
        drawAreaBitmap = drawBitmap;

    }

    private Bitmap getBitmap(String inputUrl) {
        Bitmap bitmap = Utils.getBitmapSdcard(inputUrl);
        bitmap = Utils.scaleDown(bitmap);
        return bitmap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemEditPhotoClicked(EditType type) {
        switch (type) {
            case Draw:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(DrawingFragment.newInstance(editedPhoto, this));
                break;
            case Crop:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(CropperFragment.newInstance(editedPhoto, this));
                break;
            case Filter:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(FilterFragment.newInstance(editedPhoto, this));
                break;
//            case Rotate:
//                ((EditPhotoActivity) getActivity()).addFragmentToStack(RotateFragment.newInstance(editedPhotoPath, this));
//                break;
//            case Brightness:
//                ((EditPhotoActivity) getActivity()).addFragmentToStack(BrightnessFragment.newInstance(editedPhotoPath, this));
//                break;
//            case Contrast:
//                ((EditPhotoActivity) getActivity()).addFragmentToStack(ContrastFragment.newInstance(editedPhotoPath, this));
//                break;
        }
    }

    private void hideLoading() {
//        if (ivLoading != null)
//            ivLoading.smoothToHide();
    }

    private void showLoading() {
//        if (ivLoading != null)
//            ivLoading.smoothToShow();
    }

//    @Override
//    public void onBrightnessPhotoCompleted(String s) {
//        showPhoto(s);
//    }
//
//    @Override
//    public void onContrastPhotoCompleted(String s) {
//        showPhoto(s);
//    }

    @Override
    public void onCropPhotoCompleted(Bitmap s) {
        showPhoto(s, null);
    }

    @Override
    public void onFilterPhotoCompleted(Bitmap s) {
        showPhoto(s, null);
    }

//    @Override
//    public void onRotatePhotoCompleted(String s) {
//        showPhoto(s);
//    }

    @Override
    public void onClick(View v) {
        if (v == ivCheck) {
            if (editPhotoActivity.editActionAcount == 0) {
                // Go to publish
                isPhotoEdited = false;
                Intent intent = new Intent(editPhotoActivity, PreparePostActivity.class);
                startActivity(intent);
                editPhotoActivity.finish();
            } else {
                Intent intent = new Intent(editPhotoActivity, PreparePostActivity.class);
                isPhotoEdited = true;
                startActivity(intent);
            }
        } else if (v == ivCancel) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onDrawPhotoCompleted(Bitmap originBitmap, Bitmap drawBitmap) {
//        if (url != null) {
        showPhoto(originBitmap, drawBitmap);
        if (editPhotoActivity.editActionAcount > 0) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ((EditPhotoActivity) getActivity()).addFragmentToStack(FilterFragment.newInstance(editedPhoto, EditPhotoFragment.this));
                }
            }, 100);
        }
//        }
    }
}
