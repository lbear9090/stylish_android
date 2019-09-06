package com.stylelist.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.Interfaces.StyleAdapterActionCallBack;
import com.stylelist.Models.Style;
import com.stylelist.R;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

/**
 * Created by security on 2/21/2018.
 */

public class StyleGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Style> styleArrayList;
    private StyleAdapterActionCallBack callBack;
//    public Style selectedStyle = new Style();
    public ArrayList<String> selectedStyleNames = new ArrayList<>();

    private boolean isPostingAction;

    public StyleGridAdapter(Context context, ArrayList<Style> styleArrayList, StyleAdapterActionCallBack callBack, boolean isPostingAction) {
        this.context = context;
        this.callBack = callBack;
        this.styleArrayList = styleArrayList;
        this.isPostingAction = isPostingAction;
    }

    @Override
    public int getCount() {
        return styleArrayList.size();
    }

    @Override
    public Style getItem(int position) {
        return styleArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Style style = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder = new ViewHolder(); // view lookup cache stored in tag
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.style_grid_item, parent, false);
            viewHolder.txtStyleName = convertView.findViewById(R.id.text_style_item_style_name);
            viewHolder.imageStyle = convertView.findViewById(R.id.image_style_item_style);
            viewHolder.imageChecked = convertView.findViewById(R.id.btn_style_item_checked);
            viewHolder.btnDetail = convertView.findViewById(R.id.btn_style_item_detail);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtStyleName.setText(style.styleName);
        viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onDetail(position);
            }
        });
//        if (!isPostingAction) {
            if (style.isSelected) {
                viewHolder.imageChecked.setVisibility(View.VISIBLE);
                viewHolder.txtStyleName.setTextColor(Color.WHITE);
            } else {
                viewHolder.imageChecked.setVisibility(View.INVISIBLE);
            }
//        } else {
//            if (selectedStyleNames.size() > 2) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setMessage("You can only define a maximum of two styles to your post. Selected styles can be pressed again to unselect them.")
//                        .setPositiveButton(android.R.string.ok, null);
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            } else {
//                if (style.isSelected) {
//                    selectedStyleNames.add(style.styleName);
//                } else {
//                    if (selectedStyleNames.contains(style.styleName))
//                        selectedStyleNames.remove(style.styleName);
//                }
//                if (selectedStyleNames.contains(style.styleName)) {
//                    viewHolder.imageChecked.setVisibility(View.VISIBLE);
//                    viewHolder.txtStyleName.setTextColor(Color.WHITE);
//                } else {
//                    viewHolder.imageChecked.setVisibility(View.INVISIBLE);
//                }
//            }
//            if (selectedStyle.styleName.equals(style.styleName)) {
//                viewHolder.imageChecked.setVisibility(View.VISIBLE);
//                viewHolder.txtStyleName.setTextColor(Color.WHITE);
//            } else {
//                viewHolder.imageChecked.setVisibility(View.INVISIBLE);
//            }
//        }
        viewHolder.imageStyle.setImageResource(style.styleImage);
        return convertView;
    }

    private class ViewHolder {
        CustomFontTextView txtStyleName;
        ImageView imageStyle, imageChecked, btnDetail;
    }
}
