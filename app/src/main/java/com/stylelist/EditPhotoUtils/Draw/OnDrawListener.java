package com.stylelist.EditPhotoUtils.Draw;

import android.graphics.Bitmap;

/**
 * Created by security on 3/2/2018.
 */

public interface OnDrawListener {

    void onDrawPhotoCompleted(Bitmap originBitmap, Bitmap drawBitmap);
}
