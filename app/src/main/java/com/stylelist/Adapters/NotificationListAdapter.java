package com.stylelist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.Interfaces.NotificationListAdapterCallback;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;
import com.stylelist.R;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Notification> notifications;
    private NotificationListAdapterCallback callback;

    public NotificationListAdapter(Context context){
        this.context = context;
    }
    public NotificationListAdapter(Context context, ArrayList<Notification> notifications, NotificationListAdapterCallback callback) {
        this.context = context;
        this.notifications = notifications;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Notification getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Notification notification = getItem(position);
        final ParseUser fromUser = notification.getFromUser();
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.notification_list_item, parent, false);
            viewHolder.profileImageView  = convertView.findViewById(R.id.image_notification_user_photo);
            viewHolder.txtUserName = convertView.findViewById(R.id.text_notification_username);
            viewHolder.txtTimeStamp = convertView.findViewById(R.id.text_notification_timestamp);
            viewHolder.txtDescription = convertView.findViewById(R.id.text_notification_description);
            viewHolder.infoImageView = convertView.findViewById(R.id.image_notification_info);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            viewHolder.txtUserName.setText((String) fromUser.fetchIfNeeded().get("displayName"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.txtTimeStamp.setText(UtilFunctions.getTimeStamp(notification.getCreatedAt()));

        final String type = notification.getType();
        if(type.equals("follow")){
            viewHolder.txtDescription.setText("follows you now");
        }
        if(type.equals("like")){
            viewHolder.txtDescription.setText("liked your post");
        }
        if(type.equals("comment")){
            viewHolder.txtDescription.setText("commented your post");
        }

        if(!type.equals("follow")){
            Post post = notification.getPost();
            if(post != null){
                ParseFile photoFile = post.getThumbnail();
                if (photoFile != null) {
                    Picasso.with(context).load(photoFile.getUrl()).placeholder(R.drawable.placeholder_photo).into(viewHolder.infoImageView);
                }
            }
        } else {
            ParseUser toUser = notification.getToUser();
            try {
                ParseFile profileFile = toUser.fetchIfNeeded().getParseFile("profilePictureSmall");
                Picasso.with(context).load(profileFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(viewHolder.infoImageView);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        ParseFile thumbnailFile = null;
        try {
            thumbnailFile = fromUser.fetchIfNeeded().getParseFile("profilePictureSmall");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (thumbnailFile != null) {
            Picasso.with(context).load(thumbnailFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(viewHolder.profileImageView);
        }

        viewHolder.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onProfileClicked(fromUser);
            }
        });

        viewHolder.infoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!type.equals("follow")) {
                    callback.onInfoClicked(notification.getPost());
                } else {
                    callback.onProfileClicked(notification.getToUser());
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        CustomFontTextView txtUserName, txtDescription, txtTimeStamp;
        CircleImageView profileImageView, infoImageView;
    }
}
