package com.stylelist.EditPhotoUtils.Filter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.stylelist.EditPhotoUtils.Base.EditPhotoActivity;
import com.stylelist.EditPhotoUtils.Filter.Library.filter.FilterManager;
import com.stylelist.R;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.stylelist.Utils.StyleListApp.drawAreaBitmap;
import static com.stylelist.Utils.StyleListApp.editedPhoto;

/**
 * Created by Hoang Anh Tuan on 11/23/2017.
 */

public class FilterFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

//    private static final String INPUT_URL = "inputUrl";

    FilterView filterView;
    AppCompatImageView drawArea;
//    AVLoadingIndicatorView ivLoading;
//    RecyclerView list;
    Button ivCancel;
    TextView tvTitle;
    Button ivCheck;
    LinearLayout controller;
    RelativeLayout rootFilter;
    Spinner filterSelector;

    private FilterManager.FilterType currentFilter;

    private EditPhotoActivity editPhotoActivity;
    private Bitmap bitmap;
    private boolean edited = false;

    public static FilterFragment newInstance(Bitmap inputUrl, OnFilterListener onFilterListener) {
        FilterFragment fragment = new FilterFragment();
        fragment.setOnFilterListener(onFilterListener);
        fragment.bitmap = inputUrl;
        return fragment;
    }

//    private String inputUrl;

    private OnFilterListener onFilterListener;

    public void setOnFilterListener(OnFilterListener onFilterListener) {
        this.onFilterListener = onFilterListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        mappingView(view);
        return view;
    }

    private void mappingView(View view) {
        filterView = view.findViewById(R.id.filterView);
        drawArea = view.findViewById(R.id.image_draw_area);
//        ivLoading = view.findViewById(R.id.ivLoading);
//        list = view.findViewById(R.id.list);
        ivCancel = view.findViewById(R.id.ivCancel);
        tvTitle = view.findViewById(R.id.tvTitle);
        ivCheck = view.findViewById(R.id.ivCheck);
        controller = view.findViewById(R.id.controller);
        rootFilter = view.findViewById(R.id.rootFilter);
        filterSelector = view.findViewById(R.id.image_filter_selector);

        ivCancel.setOnClickListener(this);
        ivCheck.setOnClickListener(this);
        rootFilter.setOnClickListener(this);
        filterSelector.setOnItemSelectedListener(this);

        editPhotoActivity = (EditPhotoActivity)getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
//        FilterAdapter filterAdapter = new FilterAdapter(getActivity());
//        filterAdapter.setOnItemFilterClickedListener(this);
//        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        list.setAdapter(filterAdapter);
//        new StartSnapHelper().attachToRecyclerView(list);

//        filterAdapter.add(new FilterModel(FilterManager.FilterType.Filter1, R.drawable.filter1));
//        filterAdapter.add(new FilterModel(FilterManager.FilterType.Filter2, R.drawable.filter2));
//        filterAdapter.add(new FilterModel(FilterManager.FilterType.Filter3, R.drawable.filter3));
//        filterAdapter.add(new FilterModel(FilterManager.FilterType.Filter4, R.drawable.filter4));
//        filterAdapter.add(new FilterModel(FilterManager.FilterType.Filter5, R.drawable.filter5));
//        filterAdapter.add(new FilterModel(FilterManager.FilterType.Filter6, R.drawable.filter6));
//        filterAdapter.add(new FilterModel(FilterManager.FilterType.Filter7, R.drawable.filter7));
//        filterAdapter.add(new FilterModel(FilterManager.FilterType.Filter8, R.drawable.filter8));
//        filterAdapter.add(new FilterModel(FilterManager.FilterType.Filter9, R.drawable.filter9));
//        filterAdapter.add(new FilterModel(FilterManager.FilterType.Filter10, R.drawable.filter10));
//        filterAdapter.add(new FilterModel(FilterManager.FilterType.Filter11, R.drawable.filter11));

//        if (getArguments() != null) {
//            inputUrl = getArguments().getString(INPUT_URL);
//            changeFiler(FilterManager.FilterType.Normal);
//        }
        filterView.setImageBitmap(editedPhoto);
        drawArea.setImageBitmap(drawAreaBitmap);
        edited = false;
        changeFilter(FilterManager.FilterType.Normal);
        currentFilter = FilterManager.FilterType.Normal;
    }

    private void changeFilter(final FilterManager.FilterType filter) {
//        if (filter == FilterManager.FilterType.Glow) {
//            filterView.setType(drawAreaBitmap, filter, new Observer<String>() {
//                @Override
//                public void onSubscribe(Disposable d) {
//                    showLoading();
//                }
//
//                @Override
//                public void onNext(String value) {
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                }
//
//                @Override
//                public void onComplete() {
//                    hideLoading();
//                }
//            });
//        } else {
            filterView.setType(bitmap, filter, new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {
                    showLoading();
                }

                @Override
                public void onNext(String value) {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                    hideLoading();
                }
            });
//        }
    }


    private void hideLoading() {
//        if (ivLoading != null)
//            ivLoading.smoothToHide();
    }

    private void showLoading() {
//        if (ivLoading != null)
//            ivLoading.smoothToShow();
    }

    private Bitmap renderOriginBitmap() {
        drawArea.setDrawingCacheEnabled(true);
        drawArea.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(drawArea.getDrawingCache());
        drawArea.setDrawingCacheEnabled(false);
        return bmp;
    }

    private Bitmap mergeBitmaps(Bitmap filtedBitmap) {
        if (drawAreaBitmap != null) {
            Bitmap temp = Bitmap.createBitmap(filtedBitmap.getWidth(), filtedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            drawAreaBitmap = renderOriginBitmap();
            Canvas canvas = new Canvas(temp);
            canvas.drawBitmap(filtedBitmap, 0f, 0f, null);
            canvas.drawBitmap(drawAreaBitmap, 0f, 0f, null);
            return temp;
        } else {
            return filtedBitmap;
        }
    }

    private void saveImage() {
        if (onFilterListener != null) {
            Bitmap result = mergeBitmaps(saveBitmap(bitmap));
            onFilterListener.onFilterPhotoCompleted(result);
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
//                        if (onFilterListener != null)
//                            onFilterListener.onFilterPhotoCompleted(url);
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

    private Bitmap saveBitmap(Bitmap bitmap) {

//        if (currentFilter == FilterManager.FilterType.Blur) {
//            ImageEglSurface imageEglSurface = new ImageEglSurface(bitmap.getWidth(), bitmap.getHeight());
//            imageEglSurface.setRenderer(filterView.getImageRenderer());
//            filterView.getImageRenderer().changeFilter(filterView.getType());
//            filterView.getImageRenderer().setImageBitmap(bitmap);
//            imageEglSurface.drawFrame();
//            bitmap = imageEglSurface.getBitmap();
//            imageEglSurface.release();
//            filterView.getImageRenderer().destroy();
//            return bitmap;
//        } else {
            filterView.setDrawingCacheEnabled(true);
            filterView.buildDrawingCache();
            Bitmap bmp = Bitmap.createBitmap(filterView.getDrawingCache());
            filterView.setDrawingCacheEnabled(false);
            return bmp;
//        }
    }

    private void back() {
        getActivity().onBackPressed();
    }

//    @Override
//    public void onItemFilterClicked(FilterModel filterModel) {
//        changeFiler(filterModel.getType());
//    }

    @Override
    public void onClick(View view) {
//        if (ivLoading.isShown()) return;
        if (view.getId() == R.id.ivCancel) {
            back();
        } else if (view.getId() == R.id.ivCheck) {
            if (edited) {
                editPhotoActivity.editActionAcount++;
            }
            saveImage();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        switch (position) {
            case 0:
                changeFilter(FilterManager.FilterType.Normal);
                edited = false;
                currentFilter = FilterManager.FilterType.Normal;
                break;
            case 1:
                changeFilter(FilterManager.FilterType.Grayscale);
                edited = true;
                currentFilter = FilterManager.FilterType.Grayscale;
                break;
            case 2:
                changeFilter(FilterManager.FilterType.Glow);
                edited = true;
                currentFilter = FilterManager.FilterType.Glow;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
    }
}
