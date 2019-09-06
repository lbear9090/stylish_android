package com.stylelist.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.stylelist.R;


/**
 * Created by security on 2/13/2018.
 */

public class CustomFontTextView extends AppCompatTextView {


    public CustomFontTextView(Context context) {
        super(context);
    }

    public CustomFontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomFontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FontText);
        if (ta != null) {
            String fontAsset = ta.getString(R.styleable.FontText_customFont);
            if (!TextUtils.isEmpty(fontAsset)) {
                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), fontAsset);
                if (tf != null)
                    this.setTypeface(tf);
                else
                    Log.d("FontText", String.format("Could not create a font from asset: %s", fontAsset));
            }
            ta.recycle();
        }
    }
}
