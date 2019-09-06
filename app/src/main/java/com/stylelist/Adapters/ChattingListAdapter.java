package com.stylelist.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.Models.MessageItem;
import com.stylelist.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by security on 2/26/2018.
 */

public class ChattingListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context context;
    private ArrayList<MessageItem> messageItems;
    private ParseUser receiverUser;
    private ParseUser senderUser;

    public ChattingListAdapter(Context context, ArrayList<MessageItem> messageItems, ParseUser senderUser, ParseUser receiverUser) {
        this.context = context;
        this.messageItems = messageItems;
        this.receiverUser = receiverUser;
        this.senderUser = senderUser;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_send_list_item, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_receive_list_item, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageItem message = messageItems.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message, senderUser);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message, receiverUser);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageItem message = messageItems.get(position);

        if (message.senderId.equals(senderUser.getObjectId())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        CustomFontTextView messageText;
        CircleImageView profileImage;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_chat_send_content);
            profileImage = itemView.findViewById(R.id.image_chat_send_profile);
        }

        void bind(MessageItem message, ParseUser user) {
            messageText.setText(message.messageContent);
            ParseFile thumbnailFile = user.getParseFile("profilePictureSmall");
            if (thumbnailFile != null) {
                Picasso.with(context).load(thumbnailFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(profileImage);
            }
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        CustomFontTextView messageText;
        CircleImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_chat_receive_content);
            profileImage = itemView.findViewById(R.id.image_chat_receive_profile);
        }

        void bind(MessageItem message, ParseUser user) {
            messageText.setText(message.messageContent);
            ParseFile thumbnailFile = user.getParseFile("profilePictureSmall");
            if (thumbnailFile != null) {
                Picasso.with(context).load(thumbnailFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(profileImage);
            }
        }
    }
}
