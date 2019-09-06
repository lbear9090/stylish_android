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

import java.util.ArrayList;

import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.ParseModels.Notification;
import com.stylelist.R;
import com.stylelist.Utils.UtilFunctions;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by security on 2/21/2018.
 */

public class CommentListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Notification> comments;

    public CommentListAdapter(Context context, ArrayList<Notification> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Notification getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notification comment = getItem(position);
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.comment_list_items, parent, false);
            viewHolder.txtComment = convertView.findViewById(R.id.text_comment_item_comment);
            viewHolder.txtFullName = convertView.findViewById(R.id.text_comment_item_user_name);
            viewHolder.txttimeStamp = convertView.findViewById(R.id.text_comment_item_timstamp);
            viewHolder.profileImage = convertView.findViewById(R.id.image_comment_item_user_profile);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtComment.setText(comment.getContent());
        viewHolder.txttimeStamp.setText(UtilFunctions.getTimeStamp(comment.getCreatedAt()));
        ParseUser parseUser = comment.getUser();
        try {
            viewHolder.txtFullName.setText(parseUser.fetchIfNeeded().getString("displayName"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseFile thumbnailFile = comment.getFromUser().getParseFile("profilePictureSmall");
        if (thumbnailFile != null) {
            Picasso.with(context).load(thumbnailFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(viewHolder.profileImage);
        }

        return convertView;
    }


    private static class ViewHolder {
        CircleImageView profileImage;
        CustomFontTextView txtFullName, txtComment, txttimeStamp;
    }

    public void updateDataSet(ArrayList<Notification> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
        this.notifyDataSetChanged();
    }
}
