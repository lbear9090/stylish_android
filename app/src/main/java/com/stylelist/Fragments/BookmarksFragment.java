package com.stylelist.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.stylelist.Activity.MainActivity;
import com.stylelist.Adapters.PostListAdapter;
import com.stylelist.Interfaces.PostItemClickAction;
import com.stylelist.Interfaces.PostListAdapterCallBack;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;
import com.stylelist.R;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

import static com.stylelist.Utils.Constants.LIKE;
import static com.stylelist.Utils.StyleListApp.allActivityArray;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarksFragment extends Fragment implements PostListAdapterCallBack {

    private ListView bookmarkListView;
    private PostListAdapter postListAdapter;
    private ArrayList<Post> bookmarkPosts = new ArrayList<>();
    private View footer;
    private ParseUser currentUser = ParseUser.getCurrentUser();

    public BookmarksFragment() {
        // Required empty public constructor
    }

    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        bookmarkListView = view.findViewById(R.id.bookmarks_post_list);
        footer = getLayoutInflater().inflate(R.layout.post_list_footer, null);

        bookmarkPosts = UtilFunctions.getBookMarkPosts();
        postListAdapter = new PostListAdapter(getActivity(), bookmarkPosts, this);
        bookmarkListView.setAdapter(postListAdapter);
        bookmarkListView.addFooterView(footer);

        return view;
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
}
