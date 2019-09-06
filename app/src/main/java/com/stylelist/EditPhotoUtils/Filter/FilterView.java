package com.stylelist.EditPhotoUtils.Filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.stylelist.EditPhotoUtils.Filter.Library.filter.FilterManager;
import com.stylelist.EditPhotoUtils.Filter.Library.image.ImageEglSurface;
import com.stylelist.EditPhotoUtils.Filter.Library.image.ImageRenderer;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hoang Anh Tuan on 12/7/2017.
 */

class FilterView extends AppCompatImageView {

    private FilterManager.FilterType type;
    private ImageRenderer imageRenderer;

    public FilterView(Context context) {
        super(context);
        initView(context);
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        imageRenderer = new ImageRenderer(context, FilterManager.FilterType.Normal);
    }

    public ImageRenderer getImageRenderer() {
        return imageRenderer;
    }

    public FilterManager.FilterType getType() {
        return type;
    }

    public void setType(Bitmap url, final FilterManager.FilterType type, final Observer<String> observer) {
        if (this.type == type) return;
        this.type = type;

        Observable.just(url)
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(Bitmap url) throws Exception {
                        if (type == FilterManager.FilterType.Grayscale) {
                            return applyGrayScaleFilterBitMap(url);
                        } else if (type == FilterManager.FilterType.Glow) {
                            return applyGlowFilterBitMap(url);
                        } else {
                            return filterBitmap(url);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        observer.onSubscribe(d);
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    private Bitmap filterBitmap(Bitmap bitmap) {
        bitmap = Utils.scaleDown(bitmap);

        ImageEglSurface imageEglSurface = new ImageEglSurface(bitmap.getWidth(), bitmap.getHeight());
        imageEglSurface.setRenderer(imageRenderer);
        imageRenderer.changeFilter(type);
        imageRenderer.setImageBitmap(bitmap);
        imageEglSurface.drawFrame();
        bitmap = imageEglSurface.getBitmap();
        imageEglSurface.release();
        imageRenderer.destroy();

        return bitmap;
    }

    private Bitmap applyGrayScaleFilterBitMap(Bitmap bitmap) {
        bitmap = Utils.scaleDown(bitmap);

        Bitmap bmpGrayscale = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);
        return bmpGrayscale;
    }

    private Bitmap applyGlowFilterBitMap(Bitmap bitmap) {
        bitmap = Utils.scaleDown(bitmap);
        Bitmap alpha = bitmap.extractAlpha();
        BlurMaskFilter blurMaskFilter = new BlurMaskFilter(5, BlurMaskFilter.Blur.INNER);

        Paint paint = new Paint();
        paint.setMaskFilter(blurMaskFilter);
        paint.setColor(Color.GRAY);
        paint.setAlpha(128);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(alpha, 0, 0, paint);

        return bitmap;
    }
}
