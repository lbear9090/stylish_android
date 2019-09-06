package com.stylelist.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.stylelist.Activity.MainActivity;
import com.stylelist.Adapters.PostGridAdapter;
import com.stylelist.Adapters.PostListAdapter;
import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.Interfaces.ParseUtilCallBack;
import com.stylelist.Interfaces.PostItemClickAction;
import com.stylelist.Interfaces.PostListAdapterCallBack;
import com.stylelist.Models.Result;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;
import com.stylelist.R;
import com.stylelist.Utils.ParseUtil;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

import static com.stylelist.Utils.Constants.LIKE;
import static com.stylelist.Utils.StyleListApp.allActivityArray;
import static com.stylelist.Utils.StyleListApp.allPostArray;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, PostListAdapterCallBack, SwipeRefreshLayout.OnRefreshListener, ParseUtilCallBack {

    private CustomFontButton btnTabRecent, btntabTrending, btnTabFeatured, btnCurrentTab;
    private ListView listViewPosts;
    private GridView gridViewPosts;
    private SwipeRefreshLayout postListRefresher;
    private View footer;
//    private ImageView btnList, btnGrid, btnCurrentType;

    private PostListAdapter postListAdapter;
    private PostGridAdapter postGridAdapter;
    private ArrayList<Post> postArrayList = new ArrayList<>();

    private ParseUser currentUser = ParseUser.getCurrentUser();
    private ArrayList<String> myStyles = new ArrayList<>();
    private ArrayList<String> filterKeyArray = new ArrayList<>();
    private ArrayList<String> conditionKeyArray = new ArrayList<>();
    private ArrayList<String> locationKeyArray = new ArrayList<>();
    private ArrayList<String> saleTypeKeyArray = new ArrayList<>();

    private ParseUtil parseUtil;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnTabRecent = view.findViewById(R.id.btn_tab_recent);
        btntabTrending = view.findViewById(R.id.btn_tab_trending);
        btnTabFeatured = view.findViewById(R.id.btn_tab_featured);
        postListRefresher = view.findViewById(R.id.post_list_refresher);
        listViewPosts = view.findViewById(R.id.listview_home_posts);
        gridViewPosts = view.findViewById(R.id.grid_view_home_posts);
        footer = getLayoutInflater().inflate(R.layout.post_list_footer, null);
//        btnList = view.findViewById(R.id.btn_show_list);
//        btnGrid = view.findViewById(R.id.btn_show_grid);

        initUI();

        return view;
    }

    private void initUI() {

        getMyStyle();

        postListAdapter = new PostListAdapter(getActivity(), postArrayList, this);
        postGridAdapter = new PostGridAdapter(getActivity(), postArrayList);
        listViewPosts.setAdapter(postListAdapter);
        gridViewPosts.setAdapter(postGridAdapter);
        listViewPosts.addFooterView(footer);

        listViewPosts.setOnItemClickListener(this);
        gridViewPosts.setOnItemClickListener(this);

        btnTabRecent.setOnClickListener(this);
        btntabTrending.setOnClickListener(this);
        btnTabFeatured.setOnClickListener(this);
//        btnList.setOnClickListener(this);
//        btnGrid.setOnClickListener(this);
        postListRefresher.setOnRefreshListener(this);

        btnCurrentTab = btnTabRecent;
        btnCurrentTab.setSelected(true);
//        btnCurrentType = btnList;
        updatePostListStyle(ListingStyle.LIST);
//        btnCurrentType.setSelected(true);
        if (allPostArray.size() > 0) {
            updatePostList();
        } else {
            postListRefresher.setRefreshing(true);
        }

        parseUtil = new ParseUtil(getActivity(), this);
    }

    public void refreshListView() {
        updatePostList();
        postListRefresher.setRefreshing(false);
    }

    private void updatePostList() {
        if (btnCurrentTab == btnTabRecent) {
            showRecent();
        } else if (btnCurrentTab == btntabTrending) {
            showTrending();
        } else {
            showFeatured();
        }
    }

    public void updatePostListWithFilter(ArrayList<String> filterKeyArray, ArrayList<String> conditionKeyArray, ArrayList<String> locationKeyArray, ArrayList<String> saleTypeKeyArray) {
        ArrayList<Post> filterPosts = new ArrayList<>();
        for (Post post : postArrayList) {
            ArrayList<String> postHashTags = post.getHashTags();
            ArrayList<String> postLocations = post.getLocations();
            ArrayList<String> postConditions = post.getConditions();
            ArrayList<String> postSaleTypes = post.getSaleTypes();
            if (filterKeyArray.size() > 0) {
                if (postHashTags.size() > 0) {
                    for (String key : filterKeyArray) {
                        if (postHashTags.contains(key) && !filterPosts.contains(post)) {
                            filterPosts.add(post);
                        }
                    }
                }
            }
            if (conditionKeyArray.size() > 0) {
                if (postConditions.size() > 0) {
                    for (String key : conditionKeyArray) {
                        if (postConditions.contains(key) && !filterPosts.contains(post)) {
                            filterPosts.add(post);
                        }
                    }
                }
            }
            if (locationKeyArray.size() > 0) {
                if (postLocations.size() > 0) {
                    for (String key : locationKeyArray) {
                        if (postLocations.contains(key) && !filterPosts.contains(post)) {
                            filterPosts.add(post);
                        }
                    }
                }
            }
            if (saleTypeKeyArray.size() > 0) {
                if (postSaleTypes.size() > 0) {
                    for (String key : saleTypeKeyArray) {
                        if (postSaleTypes.contains(key) && !filterPosts.contains(post)) {
                            filterPosts.add(post);
                        }
                    }
                }
            }
            if (filterKeyArray.size() == 0 && conditionKeyArray.size() == 0 && locationKeyArray.size() == 0 && saleTypeKeyArray.size() == 0) {
                filterPosts.add(post);
            }
        }
        postListAdapter.updateDataSet(filterPosts);
        postGridAdapter.updateDataSet(filterPosts);
        this.filterKeyArray = filterKeyArray;
        this.conditionKeyArray = conditionKeyArray;
        this.locationKeyArray = locationKeyArray;
        this.saleTypeKeyArray = saleTypeKeyArray;
    }

    public void updatePostListStyle(ListingStyle style) {
        switch (style) {
            case LIST:
                listViewPosts.setVisibility(View.VISIBLE);
                gridViewPosts.setVisibility(View.GONE);
                break;
            case GRID:
                listViewPosts.setVisibility(View.GONE);
                gridViewPosts.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showRecent() {
        postArrayList.clear();
        if (myStyles.size() == 0) {
            postArrayList.addAll(allPostArray);
//            postListAdapter.updateDataSet(allPostArray, allActivityArray);
//            postGridAdapter.updateDataSet(allPostArray);
        } else {
            for (int i = 0; i < allPostArray.size(); i++) {
                Post post = allPostArray.get(i);
                ArrayList<String> postStyles = null;
                try {
                    postStyles = (ArrayList<String>) post.fetchIfNeeded().get("Styles");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                boolean isStyleMatch = false;
                if (postStyles != null && postStyles.size() > 0) {
                    for (String style : postStyles) {
                        if (myStyles.contains(style))
                            isStyleMatch = true;
                    }
                }
                if (isStyleMatch)
                    postArrayList.add(post);
            }
//            postListAdapter.updateDataSet(postArrayList, allActivityArray);
//            postGridAdapter.updateDataSet(postArrayList);
        }
        updateBySearchKeys();
    }

    private void showTrending() {
        postArrayList.clear();
        postListRefresher.setRefreshing(true);
        parseUtil.getTrendingPosts();
        postListAdapter.updateDataSet(postArrayList);
        postGridAdapter.updateDataSet(postArrayList);
    }

    private void showFeatured() {
        postArrayList.clear();
        ArrayList<ParseUser> followingUsers = UtilFunctions.getFollowingUsers(currentUser);

        for (ParseUser user : followingUsers) {
            Log.d("User Id", user.getObjectId());
            for (Post post : allPostArray) {
                ParseUser tempUser = post.getUser();
                String id1 = tempUser.getObjectId();
                String id2 = user.getObjectId();
                if (id1.equals(id2)) {
                    postArrayList.add(post);
                }
            }
        }
        updateBySearchKeys();
//        postListAdapter.updateDataSet(postArrayList, allActivityArray);
//        postGridAdapter.updateDataSet(postArrayList);
    }

    private void updateBySearchKeys() {
        ArrayList<Post> filterPosts = new ArrayList<>();
        for (Post post : postArrayList) {
            ArrayList<String> postHashTags = post.getHashTags();
            ArrayList<String> postLocations = post.getLocations();
            ArrayList<String> postConditions = post.getConditions();
            ArrayList<String> postSaleTypes = post.getSaleTypes();
            if (filterKeyArray.size() > 0) {
                if (postHashTags.size() > 0) {
                    for (String key : filterKeyArray) {
                        if (postHashTags.contains(key) && !filterPosts.contains(post)) {
                            filterPosts.add(post);
                        }
                    }
                }
            }
            if (conditionKeyArray.size() > 0) {
                if (postConditions.size() > 0) {
                    for (String key : conditionKeyArray) {
                        if (postConditions.contains(key) && !filterPosts.contains(post)) {
                            filterPosts.add(post);
                        }
                    }
                }
            }
            if (locationKeyArray.size() > 0) {
                if (postLocations.size() > 0) {
                    for (String key : locationKeyArray) {
                        if (postLocations.contains(key) && !filterPosts.contains(post)) {
                            filterPosts.add(post);
                        }
                    }
                }
            }
            if (saleTypeKeyArray.size() > 0) {
                if (postSaleTypes.size() > 0) {
                    for (String key : saleTypeKeyArray) {
                        if (postSaleTypes.contains(key) && !filterPosts.contains(post)) {
                            filterPosts.add(post);
                        }
                    }
                }
            }
            if (filterKeyArray.size() == 0 && conditionKeyArray.size() == 0 && locationKeyArray.size() == 0 && saleTypeKeyArray.size() == 0) {
                filterPosts.add(post);
            }
        }
        postListAdapter.updateDataSet(filterPosts);
        postGridAdapter.updateDataSet(filterPosts);
        postListRefresher.setRefreshing(false);
    }

    private void getMyStyle() {
        ArrayList<String> styles = null;
        try {
            styles = (ArrayList<String>) currentUser.fetchIfNeeded().get("Style");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (styles != null) {
            myStyles.addAll(styles);
        } else {
            myStyles.clear();
        }
        Log.d("HomeFragment", "I have " + String.valueOf(myStyles.size()) + " Styles");
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.addFragment(PostDetailFragment.newInstance(postArrayList.get(position)));
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
        } else if (v.getClass().toString().equals(ImageView.class.toString())) {
//            if (btnCurrentType != v) {
//                btnCurrentType.setSelected(false);
//                btnCurrentType = (ImageView) v;
//                btnCurrentType.setSelected(true);
//                updatePostListStyle();
//            }
        }
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

    @Override
    public void onRefresh() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.updateAllPostAndActivity();
    }

    @Override
    public void onGetArray(Result arrayResult) {
        ArrayList<Object> objects = (ArrayList<Object>) arrayResult.getT();
        if (objects.size() > 0) {
            if (objects.get(0).getClass() == Post.class) {
                postArrayList.clear();
                postArrayList = (ArrayList<Post>) arrayResult.getT();
                updateBySearchKeys();
            }
        }
    }

    public enum ListingStyle {
        LIST, GRID
    }
}
