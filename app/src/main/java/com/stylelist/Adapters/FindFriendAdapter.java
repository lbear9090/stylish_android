package com.stylelist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.ParseModels.Notification;
import com.stylelist.R;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.stylelist.Utils.StyleListApp.allActivityArray;

public class FindFriendAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ParseUser> parseUsers;
    private boolean isShowFollowing = false;

    public FindFriendAdapter(Context context, ArrayList<ParseUser> parseUsers, boolean isShowFollowing) {
        this.context = context;
        this.parseUsers = parseUsers;
        this.isShowFollowing = isShowFollowing;
    }

    @Override
    public int getCount() {
        return parseUsers.size();
    }

    @Override
    public ParseUser getItem(int position) {
        return parseUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ParseUser parseUser = getItem(position);
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.find_friends_list, parent, false);
            viewHolder.txtUserName = convertView.findViewById(R.id.text_username_friend_list);
            viewHolder.profileImageView = convertView.findViewById(R.id.image_profile_friend_list);
            viewHolder.txtPostCount = convertView.findViewById(R.id.text_photo_number_friend_list);
            viewHolder.btnFollow = convertView.findViewById(R.id.btn_follow_friend_list);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            viewHolder.txtUserName.setText(parseUser.fetchIfNeeded().getString("displayName"));
        } catch (ParseException e) {
            e.printStackTrace();
            viewHolder.txtUserName.setText("");
        }
        ParseFile thumbnailFile = parseUser.getParseFile("profilePictureSmall");
        if (thumbnailFile != null) {
            Picasso.with(context).load(thumbnailFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(viewHolder.profileImageView);
        } else {
            viewHolder.profileImageView.setImageResource(R.drawable.avatarplaceholderprofile);
        }
        viewHolder.txtPostCount.setText(UtilFunctions.getPostCount(parseUser) + " posts");
        if (isShowFollowing) {
            viewHolder.btnFollow.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.btnFollow.setVisibility(View.VISIBLE);
        }
        if (UtilFunctions.isFollowing(parseUser)) {
            viewHolder.btnFollow.setSelected(true);
            viewHolder.btnFollow.setText("Following");
        } else {
            viewHolder.btnFollow.setSelected(false);
            viewHolder.btnFollow.setText("Follow");
        }
        viewHolder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followClicked(UtilFunctions.isFollowing(parseUser), parseUser);
            }
        });

        return convertView;
    }

    public void followClicked(boolean isFollowing, ParseUser parseUser) {
        if (!isShowFollowing) {
            if (isFollowing)
                deleteFollow(parseUser);
            else
                submitFollow(parseUser);
        }
    }

    private void deleteFollow(final ParseUser parseUser) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<Notification> followQueryDelete = new ParseQuery<>("Notification");
        followQueryDelete.whereMatches("type", "follow");
        followQueryDelete.whereEqualTo("fromUser", currentUser);
        followQueryDelete.whereEqualTo("toUser", parseUser);
        followQueryDelete.getFirstInBackground(new GetCallback<Notification>() {
            public void done(final Notification object, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    object.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                UtilFunctions.removeFollow(parseUser.getObjectId());
                            }
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    private void submitFollow(ParseUser parseUser) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        final Notification followActivity = new Notification();
        followActivity.setFromUser(currentUser);
        followActivity.setToUser(parseUser);
        followActivity.setType("follow");
        followActivity.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    allActivityArray.add(followActivity);
                }
                notifyDataSetChanged();
            }
        });
    }

    private class ViewHolder {
        CircleImageView profileImageView;
        CustomFontTextView txtUserName, txtPostCount;
        CustomFontButton btnFollow;
    }
}
