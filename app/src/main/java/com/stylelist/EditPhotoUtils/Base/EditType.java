package com.stylelist.EditPhotoUtils.Base;


import com.stylelist.R;

/**
 * Created by Hoang Anh Tuan on 11/29/2017.
 */

public enum EditType {
    Crop(R.drawable.ic_crop),
    Filter(R.drawable.ic_filter),
//    Rotate(R.drawable.ic_rotate),
//    Brightness(R.drawable.ic_brightness),
//    Contrast(R.drawable.ic_contrast),
    Draw(R.drawable.ic_draw);

    public int VALUE;

    EditType(int VALUE) {
        this.VALUE = VALUE;
    }
}
