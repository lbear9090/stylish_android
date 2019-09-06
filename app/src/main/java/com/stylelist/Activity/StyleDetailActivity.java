package com.stylelist.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stylelist.Models.Style;
import com.stylelist.R;
import com.stylelist.Utils.Constants;

import me.relex.circleindicator.CircleIndicator;

import static com.stylelist.Utils.StyleListApp.manStyles;
import static com.stylelist.Utils.StyleListApp.womanStyles;

public class StyleDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_detail);

        Bundle bundle = getIntent().getExtras();
        boolean isMan = false;
        if (bundle != null) {
            isMan = bundle.getBoolean("is_man", true);
        }
        int styleIndex = 0;
        if (bundle != null) {
            styleIndex = bundle.getInt("style_index");
        }

        Button btnDone = findViewById(R.id.btn_my_style_detail_done);
        ViewPager viewPager = findViewById(R.id.style_example_pager);
        CircleIndicator pagerIndicator = findViewById(R.id.style_detail_pager_indicator);
        TextView txtDescription = findViewById(R.id.text_style_detail_description);

        final int[] exampleImages;
        Style style;
        if (isMan) {
            style = manStyles.get(styleIndex);
            exampleImages = Constants.MAN_STYLE_IMAGE[styleIndex];
        } else {
            style = womanStyles.get(styleIndex);
            exampleImages = Constants.WOMAN_STYLE_IMAGE[styleIndex];
        }

        txtDescription.setText(style.styleDescription);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (exampleImages.length > 0) {
            //this is placeholder code.
            PagerAdapter pagerAdapter = new PagerAdapter() {
                @Override
                public int getCount() {
                    return exampleImages.length;
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    LayoutInflater mLayoutInflater = (LayoutInflater) StyleDetailActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if (mLayoutInflater != null) {
                        View itemView = mLayoutInflater.inflate(R.layout.style_detail_example_item, container, false);
                        ImageView imageView = itemView.findViewById(R.id.image_style_detail_example);
                        imageView.setImageResource(exampleImages[position]);
                        container.addView(itemView);
                        return itemView;
                    } else {
                        return null;
                    }
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }
            };
            viewPager.setAdapter(pagerAdapter);
            pagerIndicator.setViewPager(viewPager);
        }
    }
}
