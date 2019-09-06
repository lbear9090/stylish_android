package com.stylelist.EditPhotoUtils.Draw;


import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stylelist.EditPhotoUtils.Base.EditPhotoActivity;
import com.stylelist.EditPhotoUtils.Base.Utils;
import com.stylelist.Models.ItemRect;
import com.stylelist.R;

import static com.stylelist.Utils.StyleListApp.currentItemRect;
import static com.stylelist.Utils.StyleListApp.drawAreaBitmap;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingFragment extends Fragment implements View.OnClickListener {

    private static final String INPUT_URL = "inputUrl";

    private AppCompatImageView drawingView;
    private Button ivCheck, ivCancel;
    private TextView tvTitle;
    private DrawingView drawArea;
//    private AVLoadingIndicatorView ivLoading;
    private OnDrawListener onDrawListener;

    private String inputUrl;
    private EditPhotoActivity editPhotoActivity;
    private Bitmap bitmap;

    public DrawingFragment() {
        // Required empty public constructor
    }

    public static DrawingFragment newInstance(Bitmap inputUrl, OnDrawListener onDrawListener) {
        DrawingFragment fragment = new DrawingFragment();
        fragment.bitmap = inputUrl;
        fragment.onDrawListener = onDrawListener;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drawing, container, false);

        drawingView = view.findViewById(R.id.drawing_view);
        ivCancel = view.findViewById(R.id.ivCancel);
        tvTitle = view.findViewById(R.id.tvTitle);
        ivCheck = view.findViewById(R.id.ivCheck);
        drawArea = view.findViewById(R.id.image_draw_area);
//        ivLoading = view.findViewById(R.id.ivLoading);

        ivCancel.setOnClickListener(this);
        ivCheck.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
//        if (getArguments() != null) {
//            inputUrl = getArguments().getString(INPUT_URL);
//            showImage();
//        }
        showImage();
        editPhotoActivity = (EditPhotoActivity)getActivity();
    }

    private void showImage() {
        drawArea.setImageBitmap(bitmap);
        drawingView.setImageBitmap(bitmap);
//        Observable.just(inputUrl)
//                .map(new Function<String, Bitmap>() {
//                    @Override
//                    public Bitmap apply(String url) throws Exception {
//                        return getBitmap(url);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Bitmap>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        showLoading();
//                    }
//
//                    @Override
//                    public void onNext(Bitmap bitmap) {
//                        drawingView.setImageBitmap(bitmap);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("eror", e.toString());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        hideLoading();
//                    }
//                });
    }

    private void hideLoading() {
//        if (ivLoading != null)
//            ivLoading.smoothToHide();
    }

    private void showLoading() {
//        if (ivLoading != null)
//            ivLoading.smoothToShow();
    }

    private Bitmap getBitmap(String inputUrl) {
        Bitmap bitmap = Utils.getBitmapSdcard(inputUrl);
        bitmap = Utils.scaleDown(bitmap);
        return bitmap;
    }

    private void back() {
        getActivity().onBackPressed();
    }

    private void saveImage() {
        drawAreaBitmap = saveBitmap();
        if (onDrawListener != null) {
            Bitmap rendered = renderOriginBitmap();
            onDrawListener.onDrawPhotoCompleted(rendered, drawAreaBitmap);
        }
        back();
//        Observable.just(inputUrl)
//                .map(new Function<String, String>() {
//                    @Override
//                    public String apply(String url) throws Exception {
//                        return saveBitmap(url);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        showLoading();
//                    }
//
//                    @Override
//                    public void onNext(String url) {
//                        if (onDrawListener != null)
//                            onDrawListener.onDrawPhotoCompleted(url);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        back();
//                    }
//                });
    }

    private Bitmap saveBitmap() {
        Bitmap well = drawArea.getCroppedBitmap();

        RectF rectF = drawArea.getRectForPath();
        currentItemRect = new ItemRect();
        currentItemRect.xPosition = rectF.left / drawArea.getWidth();
        currentItemRect.yPosition = rectF.top / drawArea.getHeight();
        currentItemRect.width = rectF.width() / drawArea.getWidth();
        currentItemRect.height = rectF.height() / drawArea.getHeight();

        return well;
    }

    private Bitmap renderOriginBitmap() {
        drawingView.setDrawingCacheEnabled(true);
        drawingView.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(drawingView.getDrawingCache());
        drawingView.setDrawingCacheEnabled(false);
        return bmp;
    }

    @Override
    public void onClick(View v) {
//        if (ivLoading.isShown()) return;
        if (v.getId() == R.id.ivCancel) {
            back();
        } else if (v.getId() == R.id.ivCheck) {
            if (drawArea.isDrawn) {
                editPhotoActivity.editActionAcount++;
            }
            saveImage();
        }
    }
}
