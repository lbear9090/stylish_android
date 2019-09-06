package com.stylelist.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.stylelist.Activity.EditProfileActivity;
import com.stylelist.Activity.MainActivity;
import com.stylelist.Activity.MyStyleActivity;
import com.stylelist.Adapters.PostGridAdapter;
import com.stylelist.Adapters.PostListAdapter;
import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.Interfaces.ParseUtilCallBack;
import com.stylelist.Interfaces.PostItemClickAction;
import com.stylelist.Interfaces.PostListAdapterCallBack;
import com.stylelist.Models.Result;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;
import com.stylelist.R;
import com.stylelist.Utils.Constants;
import com.stylelist.Utils.ParseUtil;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.stylelist.Utils.Constants.LIKE;
import static com.stylelist.Utils.StyleListApp.allActivityArray;
import static com.stylelist.Utils.StyleListApp.allPostArray;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, PostListAdapterCallBack, ParseUtilCallBack, SwipeRefreshLayout.OnRefreshListener {

    private ListView listViewProfile;
    private ImageView imageProfileBackground, btnShowGrid;
    private GridView postGridView;
    private CircleImageView imageProfilePhoto;
    private CustomFontTextView txtPostNumber, txtFollowerNumber, txtFollowingNumber, txtFullName, txtUsername, txtUserBio;
    private CustomFontButton btnTabAll, btnTabForSale, btnTabForHire, btnCurrentTab;
    private Button btnEdit, btnMyStyle, btnFollow;
    private ImageButton btnChat;
    private SwipeRefreshLayout postListViewRefresher;
    private View footer;

    private ArrayList<Post> postArrayList = new ArrayList<>();
    private PostListAdapter postListAdapter;

    private ParseUser selectedUser;
    private ParseUser currentUser;
    private ParseFile profileBackgroundImage, profilePhotoImage;

    private ParseUtil parseUtil;
    private PostGridAdapter postGridAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(ParseUser selectedUser) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.selectedUser = selectedUser;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        listViewProfile = view.findViewById(R.id.listview_profile_page);
        imageProfileBackground = view.findViewById(R.id.image_profile_back);
        imageProfilePhoto = view.findViewById(R.id.image_profile_profile_photo);
        txtPostNumber = view.findViewById(R.id.text_profile_post_number);
        txtFollowerNumber = view.findViewById(R.id.text_profile_follower_number);
        txtFollowingNumber = view.findViewById(R.id.text_profile_following_number);
        txtFullName = view.findViewById(R.id.text_profile_fullname);
        txtUsername = view.findViewById(R.id.text_profile_username);
        txtUserBio = view.findViewById(R.id.text_profile_bio);
        btnTabAll = view.findViewById(R.id.btn_tab_all);
        btnTabForSale = view.findViewById(R.id.btn_tab_for_sale);
        btnTabForHire = view.findViewById(R.id.btn_tab_for_hire);
        btnEdit = view.findViewById(R.id.btn_profile_edit);
        btnMyStyle = view.findViewById(R.id.btn_profile_style);
        btnChat = view.findViewById(R.id.btn_profile_chat);
        postListViewRefresher = view.findViewById(R.id.listview_profile_post_refresher);
        btnFollow = view.findViewById(R.id.btn_profile_follow);
        btnShowGrid = view.findViewById(R.id.btn_profile_show_grid);
//        btnShowList = view.findViewById(R.id.btn_profile_show_list);
        postGridView = view.findViewById(R.id.grid_view_profile_posts);
        footer = getLayoutInflater().inflate(R.layout.post_list_footer, null);

        initUI();

        return view;
    }

    private void initUI() {
        parseUtil = new ParseUtil(getActivity(), this);
        currentUser = ParseUser.getCurrentUser();

        if (currentUser.getObjectId().equals(selectedUser.getObjectId())) {
            btnEdit.setVisibility(View.VISIBLE);
            btnFollow.setVisibility(View.INVISIBLE);
            btnChat.setVisibility(View.INVISIBLE);
            btnMyStyle.setVisibility(View.VISIBLE);
        } else {
            btnEdit.setVisibility(View.INVISIBLE);
            btnFollow.setVisibility(View.VISIBLE);
            btnChat.setVisibility(View.VISIBLE);
            btnMyStyle.setVisibility(View.INVISIBLE);
        }

        if (UtilFunctions.isFollowing(selectedUser)) {
            btnFollow.setSelected(true);
            btnFollow.setText("Following");
        } else {
            btnFollow.setSelected(false);
            btnFollow.setText("Follow");
        }

        txtFullName.setText(selectedUser.get("displayName").toString());
        txtUsername.setText("@" + selectedUser.get("usernameFix").toString());
        if (selectedUser.get("UserInfo") != null) {
            txtUserBio.setText(selectedUser.get("UserInfo").toString());
        }

        profileBackgroundImage = selectedUser.getParseFile("headerPictureMedium");
        if (profileBackgroundImage != null) {
            Picasso.with(getActivity()).load(profileBackgroundImage.getUrl()).placeholder(R.drawable.placeholder_photo).into(imageProfileBackground);
        }

        profilePhotoImage = selectedUser.getParseFile("profilePictureSmall");
        if (profilePhotoImage != null) {
            Picasso.with(getActivity()).load(profilePhotoImage.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(imageProfilePhoto);
        }

        txtPostNumber.setText(UtilFunctions.getPostCount(selectedUser) + " posts");
        txtFollowerNumber.setText(UtilFunctions.getFollowerCount(selectedUser) + " followers");
        txtFollowingNumber.setText(UtilFunctions.getFollowingCount(selectedUser) + " followings");

        postListAdapter = new PostListAdapter(getActivity(), postArrayList, this);
        postGridAdapter = new PostGridAdapter(getActivity(), postArrayList);
        listViewProfile.setAdapter(postListAdapter);
        listViewProfile.addFooterView(footer);
        postGridView.setAdapter(postGridAdapter);
        listViewProfile.setOnItemClickListener(this);
        postGridView.setOnItemClickListener(this);

        postListViewRefresher.setOnRefreshListener(this);

        btnTabAll.setOnClickListener(this);
        btnTabForSale.setOnClickListener(this);
        btnTabForHire.setOnClickListener(this);

        btnEdit.setOnClickListener(this);
        btnMyStyle.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        btnChat.setOnClickListener(this);
//        btnShowList.setOnClickListener(this);
        btnShowGrid.setOnClickListener(this);
        txtFollowerNumber.setOnClickListener(this);
        txtFollowingNumber.setOnClickListener(this);

        btnCurrentTab = btnTabAll;
        btnCurrentTab.setSelected(true);
//        btnCurrentType = btnShowList;
        btnShowGrid.setSelected(true);
        updatePostListStyle();
//        btnCurrentType.setSelected(true);

        updatePostList();
    }

    private void updatePostListStyle() {
        if (btnShowGrid.isSelected()) {
            listViewProfile.setVisibility(View.VISIBLE);
            postGridView.setVisibility(View.GONE);
        } else {
            listViewProfile.setVisibility(View.GONE);
            postGridView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getClass().toString().equals(CustomFontButton.class.toString())){
            if (btnCurrentTab != v) {
                btnCurrentTab.setSelected(false);
                btnCurrentTab = (CustomFontButton) v;
                btnCurrentTab.setSelected(true);
                updatePostList();
            }
        }
        if (v == btnMyStyle) {
            Intent intent = new Intent(getActivity(), MyStyleActivity.class);
            if (selectedUser.getObjectId().equals(currentUser.getObjectId())) {
                intent.putExtra(Constants.PARENT_ACTIVITY, Constants.PARENT_IS_PROFILE);
            } else {
                intent.putExtra(Constants.PARENT_ACTIVITY, Constants.PARENT_IS_OTHER_PROFILE);
            }
            startActivity(intent);
        } else if (v == btnChat) {
            attemptChat();
        } else if (v == btnFollow) {
            actionFollow();
        } else if (v == btnEdit) {
            attemptEditProfile();
        } else if (v == btnShowGrid) {
            btnShowGrid.setSelected(!btnShowGrid.isSelected());
            updatePostListStyle();
        } else if (v == txtFollowerNumber) {
            attemptToFollow(false);
        } else if (v == txtFollowingNumber) {
            attemptToFollow(true);
        }
    }

    private void actionFollow() {
        if (UtilFunctions.isFollowing(selectedUser)) {
            deleteFollow(selectedUser);
        } else {
            submitFollow(selectedUser);
        }
    }

    private void attemptToFollow(boolean following) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.addFragment(FollowFragment.newInstance(following, selectedUser));
    }

    private void attemptEditProfile() {
        startActivity(new Intent(getActivity(), EditProfileActivity.class));
    }

    private void attemptChat() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.addFragment(ChattingFragment.newInstance(selectedUser));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.addFragment(PostDetailFragment.newInstance(postArrayList.get(position)));
    }

    @Override
    public void onClickAction(PostItemClickAction action, Post post) {
        MainActivity mainActivity = (MainActivity) getActivity();
        switch (action) {
            case POST_LIKE:
                likePost(post);
                break;
            case POST_SHARE:
                sharePost(post);
                break;
            case POST_DETAIL:
                mainActivity.addFragment(PostDetailFragment.newInstance(post));
                break;
            case POST_COMMENT:
                mainActivity.addFragment(PostDetailFragment.newInstance(post));
                break;
            case POST_PROFILE:
                ParseUser user = post.getUser();
                if (user != null) {
                    mainActivity.addFragment(ProfileFragment.newInstance(user));
                }
                break;
            case POST_BOOKMARK:
                break;
            case ITEM_POST_DETAIL:
                mainActivity.prepareItemPosts(post);
                break;
//            case ITEM_POST_ITEM_DETAIL:
//                break;
        }
    }

    private void likePost(Post post) {
        final Notification like = new Notification();
        like.setToUser(post.getUser());
        like.setFromUser(currentUser);
        like.setType(LIKE);
        like.setType("like");
        like.setPost(post);
        like.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    allActivityArray.add(like);
                    postListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void sharePost(Post post) {
        String url = post.getImage().getUrl();
        Uri uri = UtilFunctions.getUriFromUrl(url);
        if (uri != null) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_TEXT, "Yay! An awesome post from Social Network.");
            intent.putExtra(Intent.EXTRA_TITLE, "Share this image");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            Intent openInChooser = new Intent(intent);
            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, uri);
            startActivity(openInChooser);
        }
    }

    @Override
    public void onGetArray(Result arrayResult) {

    }

    @Override
    public void onRefresh() {
        updatePostList();
        postListViewRefresher.setRefreshing(false);
    }

    private void updatePostList() {
        postArrayList.clear();
        if (btnCurrentTab == btnTabAll) {
            for (Post post : allPostArray) {
                if (post.getUser().getObjectId().equals(selectedUser.getObjectId())) {
                    ArrayList<String> saleTypes = post.getSaleTypes();
                    if (saleTypes.contains(Constants.FOR_INSPIRATION)) {
                        postArrayList.add(post);
                    }
                }
            }
        } else if (btnCurrentTab == btnTabForSale) {
            for (Post post : allPostArray) {
                if (post.getUser().getObjectId().equals(selectedUser.getObjectId())) {
                    ArrayList<String> saleTypes = post.getSaleTypes();
                    if (saleTypes.contains(Constants.FOR_SALE)) {
                        postArrayList.add(post);
                    }
                }
            }
        } else if (btnCurrentTab == btnTabForHire) {
            for (Post post : allPostArray) {
                if (post.getUser().getObjectId().equals(selectedUser.getObjectId())) {
                    ArrayList<String> saleTypes = post.getSaleTypes();
                    if (saleTypes.contains(Constants.FOR_HIRE)) {
                        postArrayList.add(post);
                    }
                }
            }
        }
        postListAdapter.updateDataSet(postArrayList);
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
                                btnFollow.setSelected(false);
                                btnFollow.setText("Follow");
                            }
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
                    btnFollow.setSelected(true);
                    btnFollow.setText("Following");
                }
            }
        });
    }
}
