package com.stylelist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.Models.MenuItem;
import com.stylelist.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by security on 2/13/2018.
 */

public class MenuListAdapter extends BaseAdapter {

    private String userName, photoUrl, fullName;
    private ArrayList<MenuItem> items;
    private LayoutInflater inflater;
    Context context;


    public MenuListAdapter(Context context, String fullName, String userName, ParseFile photoFile) {
        items = new ArrayList<>();
        items.add(new MenuItem(-1, R.string.menu_activity, true));
        items.add(new MenuItem(R.drawable.ic_transaction, R.string.menu_transactions, false));
        items.add(new MenuItem(R.drawable.ic_bookmark, R.string.menu_bookmarks, false));
        items.add(new MenuItem(-1, R.string.menu_support, true));
        items.add(new MenuItem(R.drawable.ic_help, R.string.menu_help, false));
        items.add(new MenuItem(R.drawable.ic_terms, R.string.menu_terms, false));
        items.add(new MenuItem(R.drawable.ic_contact_us, R.string.menu_contact, false));
        items.add(new MenuItem(-1, R.string.menu_settings, true));
        items.add(new MenuItem(R.drawable.ic_find_friends, R.string.menu_find_friends, false));
        items.add(new MenuItem(R.drawable.ic_settings, R.string.menu_settings, false));
        items.add(new MenuItem(R.drawable.ic_log_out, R.string.menu_log_out, false));
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.userName = userName;
        this.fullName = fullName;
        if (photoFile != null) {
            this.photoUrl = photoFile.getUrl();
        }
    }

    @Override
    public int getCount() {
        return items.size() + 1;
    }

    @Override
    public MenuItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        //If the cell is a section header we inflate the header layout
        if (position == 0) {
            v = inflater.inflate(R.layout.menu_profile_item, null);
            CustomFontTextView userName = v.findViewById(R.id.text_menu_profile_user_name);
            CustomFontTextView fullName = v.findViewById(R.id.text_menu_profile_full_name);
            CircleImageView profileImageView = v.findViewById(R.id.image_menu_profile);
            userName.setText("@" + this.userName);
            fullName.setText(this.fullName);
            Picasso.with(context).load(photoUrl).placeholder(R.drawable.avatarplaceholderprofile).into(profileImageView);
        } else {
            MenuItem item = getItem(position - 1);
            if (item.isHeader()) {
                v = inflater.inflate(R.layout.menu_header_item, null);
                v.setClickable(false);
                CustomFontTextView header = v.findViewById(R.id.text_menu_title);
                header.setText(context.getString(item.getTitleResId()));
            } else {
                v = inflater.inflate(R.layout.menu_row_item, null);
                CustomFontTextView title = v.findViewById(R.id.text_menu_title);
                ImageView iconImage = v.findViewById(R.id.image_menu_icon);
                title.setText(context.getString(item.getTitleResId()));
                iconImage.setImageResource(item.getIconResId());
            }
        }
        return v;
    }
}
