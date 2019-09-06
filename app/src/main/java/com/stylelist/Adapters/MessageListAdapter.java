package com.stylelist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.database.DataSnapshot;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.Models.MessageItem;
import com.stylelist.Models.MessageSession;
import com.stylelist.R;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by security on 2/26/2018.
 */

public class MessageListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DataSnapshot> messageSessions;
    private ArrayList<ParseUser> users;

    public MessageListAdapter(Context context, ArrayList<DataSnapshot> messageSessions, ArrayList<ParseUser> users) {
        this.context = context;
        this.messageSessions = messageSessions;
        this.users = users;
    }

    @Override
    public int getCount() {
        return messageSessions.size();
    }

    @Override
    public DataSnapshot getItem(int position) {
        return messageSessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        ParseUser currentUser = ParseUser.getCurrentUser();
        DataSnapshot messageSessionData = getItem(position);
        MessageSession messageSession = new MessageSession();
        for (DataSnapshot dataSnapshot1 : messageSessionData.getChildren()) {
            MessageItem messageItem = dataSnapshot1.getValue(MessageItem.class);
            messageSession.messageItems.add(messageItem);
        }
        int messageCount = messageSession.messageItems.size();
        MessageItem messageItem = messageSession.messageItems.get(messageCount - 1);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.message_list_item, parent, false);
            viewHolder.txtFullName = convertView.findViewById(R.id.text_message_item_full_name);
            viewHolder.profileImage = convertView.findViewById(R.id.image_message_item_profile);
            viewHolder.txtLastMessage = convertView.findViewById(R.id.text_message_item_last_message);
            viewHolder.txtTimeStamp = convertView.findViewById(R.id.text_message_item_timestamp);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String receiverId = messageSessionData.getKey().replace(currentUser.getObjectId(), "");
        receiverId = receiverId.replace("+", "");

        ParseUser opponentUser = null;
        for (int i = 0; i < users.size(); i++) {
            if (receiverId.equals(users.get(i).getObjectId())) {
                opponentUser = users.get(i);
            }
        }

        if (opponentUser != null) {
            viewHolder.txtFullName.setText(opponentUser.getString("displayName"));
            viewHolder.txtLastMessage.setText(messageItem.messageContent);
            viewHolder.txtTimeStamp.setText(UtilFunctions.getTimeStampFromMilliSeconds(messageItem.timeStamp));
            ParseFile thumbnailFile = opponentUser.getParseFile("profilePictureSmall");
            if (thumbnailFile != null) {
                Picasso.with(context).load(thumbnailFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(viewHolder.profileImage);
            }

        }
        return convertView;
    }

    private class ViewHolder {
        CircleImageView profileImage;
        CustomFontTextView txtFullName;
        CustomFontTextView txtLastMessage;
        CustomFontTextView txtTimeStamp;
    }

    public void updateDataSet(ArrayList<DataSnapshot> messageSessions, ArrayList<ParseUser> users) {
        this.messageSessions = messageSessions;
        this.users = users;
        this.notifyDataSetChanged();
    }
}
