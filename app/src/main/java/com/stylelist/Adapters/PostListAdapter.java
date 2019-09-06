package com.stylelist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.Interfaces.PostItemClickAction;
import com.stylelist.Interfaces.PostListAdapterCallBack;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;
import com.stylelist.R;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.stylelist.Utils.StyleListApp.allActivityArray;

/**
 * Created by security on 2/22/2018.
 */

public class PostListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Post> postArrayList;
    private PostListAdapterCallBack postListAdapterCallBack;

    public PostListAdapter(Context context, ArrayList<Post> postArrayList, PostListAdapterCallBack postListAdapterCallBack) {
        this.context = context;
        this.postArrayList = postArrayList;
        this.postListAdapterCallBack = postListAdapterCallBack;
    }

    @Override
    public int getCount() {
        return postArrayList.size();
    }

    @Override
    public Post getItem(int position) {
        return postArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder = new ViewHolder(); // view lookup cache stored in tag
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.home_post_item, parent, false);
            viewHolder.txtFullName = convertView.findViewById(R.id.text_post_item_full_name);
            viewHolder.txtUserName = convertView.findViewById(R.id.text_post_item_username);
            viewHolder.txtTimeStamp = convertView.findViewById(R.id.text_post_item_time_ago);
            viewHolder.txtLikeCount = convertView.findViewById(R.id.text_post_item_likes);
            viewHolder.txtCommentCount = convertView.findViewById(R.id.text_post_item_comments);
            viewHolder.profileImageView = convertView.findViewById(R.id.image_post_item_profile);
            viewHolder.btnComment = convertView.findViewById(R.id.btn_post_item_comment);
            viewHolder.btnLike = convertView.findViewById(R.id.btn_post_item_like);
            viewHolder.btnShare = convertView.findViewById(R.id.btn_post_item_share);
            viewHolder.postImageView = convertView.findViewById(R.id.image_post_item_post);
            viewHolder.txtStyleTag = convertView.findViewById(R.id.text_post_item_style_tags);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Post post = getItem(position);
        if(post == null)
            return convertView;
        ParseUser postUser = post.getUser();

        try {
            viewHolder.txtUserName.setText("@" + postUser.fetchIfNeeded().get("usernameFix").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.txtFullName.setText(postUser.get("displayName").toString());
        viewHolder.txtTimeStamp.setText(UtilFunctions.getTimeStamp(post.getCreatedAt()));
        viewHolder.txtLikeCount.setText(String.valueOf(likesCount(position)) + " likes");
        viewHolder.txtCommentCount.setText(String.valueOf(commentsCount(position)) + " comments");

        ArrayList<String> postStyles = null;
        try {
            postStyles = (ArrayList<String>) post.fetchIfNeeded().get("Styles");
            String styleTag = "";
            if (postStyles != null && postStyles.size() > 0) {
                for (String style : postStyles) {
                    styleTag = styleTag + " #" + style;
                }
                viewHolder.txtStyleTag.setVisibility(View.VISIBLE);
                viewHolder.txtStyleTag.setText(styleTag);
            } else {
                viewHolder.txtStyleTag.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            viewHolder.txtStyleTag.setVisibility(View.GONE);
        }

        viewHolder.btnLike.setSelected(checkIfLiked(position));
        if (checkIfLiked(position)) {
            viewHolder.btnLike.setEnabled(false);
        } else {
            viewHolder.btnLike.setEnabled(true);
        }

        viewHolder.postImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postListAdapterCallBack.onClickAction(PostItemClickAction.ITEM_POST_DETAIL, getItem(position));
            }
        });

        viewHolder.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postListAdapterCallBack.onClickAction(PostItemClickAction.POST_PROFILE, getItem(position));
            }
        });

        viewHolder.txtFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postListAdapterCallBack.onClickAction(PostItemClickAction.POST_PROFILE, getItem(position));
            }
        });

        viewHolder.txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postListAdapterCallBack.onClickAction(PostItemClickAction.POST_PROFILE, getItem(position));
            }
        });

        viewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfLiked(position))
                    postListAdapterCallBack.onClickAction(PostItemClickAction.POST_LIKE, getItem(position));
            }
        });

        viewHolder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postListAdapterCallBack.onClickAction(PostItemClickAction.POST_COMMENT, getItem(position));
            }
        });

        viewHolder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postListAdapterCallBack.onClickAction(PostItemClickAction.POST_SHARE, getItem(position));
            }
        });

        ParseFile thumbnailFile = postUser.getParseFile("profilePictureSmall");
        if (thumbnailFile != null) {
            Picasso.with(context).load(thumbnailFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(viewHolder.profileImageView);
        }

        ParseFile photoFile = post.getImage();
        if (photoFile != null) {
            Picasso.with(context).load(photoFile.getUrl()).placeholder(R.drawable.placeholder_photo).into(viewHolder.postImageView);
        }

        return convertView;
    }

    public void updateDataSet(ArrayList<Post> postArrayList) {
        this.postArrayList = postArrayList;
        this.notifyDataSetChanged();
    }

    private int likesCount(int position) {
        int count = 0;
        Post post = getItem(position);
        for (int i = 0; i < allActivityArray.size(); i++) {
            String postId = allActivityArray.get(i).getPostId();
            String postId2 = post.getObjectId();
            if (allActivityArray.get(i).getType().equals("like") && postId.equals(postId2)) {
                count++;
            }
        }
        return count;
    }

    private int commentsCount(int position) {
        int count = 0;
        Post post = getItem(position);
        for (int i = 0; i < allActivityArray.size(); i++) {
            if (allActivityArray.get(i).getType().equals("comment") && allActivityArray.get(i).getPostId().equals(post.getObjectId())) {
                count++;
            }
        }
        return count;
    }

    private boolean checkIfLiked(int position) {
        boolean isLike = false;
        Post post = getItem(position);
        for (int i = 0; i < allActivityArray.size(); i++) {
            if (allActivityArray.get(i).getType().equals("like") &&
                    allActivityArray.get(i).getPostId().equals(post.getObjectId()) &&
                    allActivityArray.get(i).getFromUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                isLike = true;
            }
        }
        return isLike;
    }

    private class ViewHolder{
        CircleImageView profileImageView;
        CustomFontTextView txtFullName, txtUserName, txtTimeStamp, txtLikeCount, txtCommentCount, txtStyleTag;
        ImageView postImageView, btnLike, btnComment, btnShare;
    }
}
