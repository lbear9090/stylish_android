package com.stylelist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by security on 2/26/2018.
 */

public class ChatFriendListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ParseUser> users;

    public ChatFriendListAdapter(Context context, ArrayList<ParseUser> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public ParseUser getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ParseUser parseUser = getItem(position);
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.chat_friend_item, parent, false);
            viewHolder.txtFullName = convertView.findViewById(R.id.text_chat_friend_full_name);
            viewHolder.profileImage = convertView.findViewById(R.id.image_chat_friend_profile);
            viewHolder.chkSelected = convertView.findViewById(R.id.check_chat_friend_selected);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtFullName.setText(parseUser.getString("displayName"));

        ParseFile thumbnailFile = parseUser.getParseFile("profilePictureSmall");
        if (thumbnailFile != null) {
            Picasso.with(context).load(thumbnailFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(viewHolder.profileImage);
        }

        return convertView;
    }

    private class ViewHolder {
        CircleImageView profileImage;
        CustomFontTextView txtFullName;
        ImageView chkSelected;
    }
}
