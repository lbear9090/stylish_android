package com.stylelist.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.CountCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.stylelist.Activity.MainActivity;
import com.stylelist.Adapters.CommentListAdapter;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.Interfaces.ParseUtilCallBack;
import com.stylelist.Models.Result;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;
import com.stylelist.R;
import com.stylelist.Utils.ParseUtil;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.stylelist.Utils.Constants.COMMENT;
import static com.stylelist.Utils.StyleListApp.allActivityArray;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailFragment extends Fragment implements View.OnClickListener, ParseUtilCallBack, SwipeRefreshLayout.OnRefreshListener {

    private Post currentPost;

    private CustomFontTextView txtLikesNumber, txtUserName, txtTimeStamp, txtFullName;
    private CircleImageView profileImageView;
    private ListView listViewComments;
    private ImageButton btnSubmitComment;
    private EditText edtComment;
    private SwipeRefreshLayout commentListRefresher;
    private RecyclerView likedUserList;

    private CommentListAdapter commentListAdapter;
    private View commentField;

    private ParseImageView postImageView;
    private ParseFile thumbnailFile;

    private ParseUtil parseUtil;

    private ArrayList<Notification> comments = new ArrayList<>();

    public PostDetailFragment() {
        // Required empty public constructor
    }

    public static PostDetailFragment newInstance(Post post) {
        PostDetailFragment fragment = new PostDetailFragment();
        fragment.currentPost = post;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        txtUserName = view.findViewById(R.id.text_post_detail_user_name);
        txtFullName = view.findViewById(R.id.text_post_detail_full_name);
        txtTimeStamp = view.findViewById(R.id.text_post_detail_timestamp);
        profileImageView = view.findViewById(R.id.image_post_detail_profile);
        postImageView = view.findViewById(R.id.image_post_detail_photo);
        txtLikesNumber = view.findViewById(R.id.text_post_detail_like_count);
        listViewComments = view.findViewById(R.id.list_post_detail_comments);
        commentListRefresher = view.findViewById(R.id.list_view_post_comments_refresher);
        likedUserList = view.findViewById(R.id.listview_post_detail_liked);

        commentField = getLayoutInflater().inflate(R.layout.comment_footer, null);
        btnSubmitComment = commentField.findViewById(R.id.new_comment_send);
        edtComment = commentField.findViewById(R.id.new_comment_input);

        initUI();
        return view;
    }

    private void initUI() {
        parseUtil = new ParseUtil(getActivity(), this);
        ParseUser photoUser = null;
        try {
            photoUser = currentPost.getUser().fetchIfNeeded();
            txtUserName.setText("@" + photoUser.get("usernameFix").toString());
            txtFullName.setText(photoUser.get("displayName").toString());

            txtTimeStamp.setText(UtilFunctions.getTimeStamp(currentPost.getCreatedAt()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        thumbnailFile = photoUser.getParseFile("profilePictureSmall");
        if (thumbnailFile != null) {
            Picasso.with(getActivity()).load(thumbnailFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(profileImageView);
        }

        postImageView.setPlaceholder(getContext().getResources().getDrawable(R.drawable.placeholder_photo));
        ParseFile photoFile = currentPost.getImage();
        postImageView.setParseFile(photoFile);
        postImageView.loadInBackground();

        postImageView.setOnClickListener(this);

        getPostLikesCount(currentPost);

        commentListAdapter = new CommentListAdapter(getActivity(), comments);
        listViewComments.setAdapter(commentListAdapter);
        listViewComments.addFooterView(commentField);

        btnSubmitComment.setOnClickListener(this);

        commentListRefresher.setOnRefreshListener(this);
        commentListRefresher.setRefreshing(true);
        parseUtil.getCommentsForPost(currentPost);
    }

    private void submitComment() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String commentText = edtComment.getText().toString().trim();

        if (commentText.length() > 0 && currentPost != null) { //post.objectForKey("user")
            final Notification comment = new Notification();
            comment.setContent(commentText);
            comment.setToUser(currentPost.getUser());
            comment.setFromUser(currentUser);
            comment.setType(COMMENT);
            comment.setPost(currentPost);
            comment.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        edtComment.setText("");
                        parseUtil.getCommentsForPost(currentPost);
                        allActivityArray.add(comment);
                    }
                }
            });
        }
    }

    public void getPostLikesCount(Post currentPost) {
        ParseQuery<Notification> photoLikes = new ParseQuery<>("Notification");
        photoLikes.whereEqualTo("post", currentPost);
        photoLikes.whereEqualTo("type", "like");
        photoLikes.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        photoLikes.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    txtLikesNumber.setText(String.valueOf(count));
                } else {
                    e.printStackTrace();
                    txtLikesNumber.setText("0");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmitComment) {
            submitComment();
        } else if (v == postImageView) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.prepareItemPosts(currentPost);
        }
    }

    @Override
    public void onGetArray(Result arrayResult) {
        commentListRefresher.setRefreshing(false);
        comments.clear();
        comments = (ArrayList<Notification>) arrayResult.getT();
        commentListAdapter.updateDataSet(comments);
    }

    @Override
    public void onRefresh() {
        parseUtil.getCommentsForPost(currentPost);
    }
}
