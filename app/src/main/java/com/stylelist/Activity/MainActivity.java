package com.stylelist.Activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.almeros.android.multitouch.MoveGestureDetector;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.navdrawer.SimpleSideDrawer;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.stylelist.Adapters.MenuListAdapter;
import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.CustomViews.ItemPostDetailInfoView;
import com.stylelist.EditPhotoUtils.Base.EditPhotoActivity;
import com.stylelist.Fragments.BookmarksFragment;
import com.stylelist.Fragments.ChattingFragment;
import com.stylelist.Fragments.ChooseFriendFragment;
import com.stylelist.Fragments.FindFriendFragment;
import com.stylelist.Fragments.FollowFragment;
import com.stylelist.Fragments.HomeFragment;
import com.stylelist.Fragments.MessageFragment;
import com.stylelist.Fragments.NotificationFragment;
import com.stylelist.Fragments.PostDetailFragment;
import com.stylelist.Fragments.ProfileFragment;
import com.stylelist.Interfaces.ItemPostClickCallback;
import com.stylelist.Interfaces.ParseUtilCallBack;
import com.stylelist.Models.MenuItem;
import com.stylelist.Models.Result;
import com.stylelist.ParseModels.ItemPost;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;
import com.stylelist.R;
import com.stylelist.Utils.Constants;
import com.stylelist.Utils.ParseUtil;
import com.stylelist.Utils.UtilFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import me.grantland.widget.AutofitTextView;

import static com.stylelist.Utils.StyleListApp.allActivityArray;
import static com.stylelist.Utils.StyleListApp.allPostArray;
import static com.stylelist.Utils.StyleListApp.allUsers;
import static com.stylelist.Utils.StyleListApp.currentItemPost;
import static com.stylelist.Utils.StyleListApp.editedPhoto;
import static com.stylelist.Utils.StyleListApp.mainActivity;
import static com.stylelist.Utils.StyleListApp.originalPhoto;

public class MainActivity extends FragmentActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, ParseUtilCallBack, ItemPostClickCallback,
        View.OnTouchListener {

    private ImageButton btnMenuDrawer, btnActionComposeMessage, btnTabCamera, btnTabHome, btnTabProfile, btnTabChat, btnTabActivity, btnCurrentTab;
    private CustomFontTextView txtPageTitle;
    private AutofitTextView searchView;
    private CustomFontButton btnBack, btnActionDone;
//    private Button btnActionFilter;
    private RelativeLayout itemPostContainer, itemPostInfoContainer, searchKitContainer;
    private LinearLayout listStyleContainer;
    private ImageView itemPostImage, btnBinocular, btnGrid;
    private ListView menuListView;

    private SimpleSideDrawer simpleSideDrawer;

    private Post currentPost;

    private FragmentManager fragmentManager;

    public static Stack<String> mFragmentStack;

    private static int REQUEST_CAMERA = 0;
    private static int REQUEST_GALLERY = 1;

    private static int REQUEST_ID_MULTIPLE_PERMISSIONS = 3;

    private ParseUtil parseUtil;
    private static final int REQUEST_GET_POSTS = 1;
    private static final int REQUEST_GET_NOTIFICATIONS = 2;
    private static final int REQUEST_GOT_ALL_DATA = 3;
    private static final int REQUEST_SEARCH_FILTER = 1005;
    private int currentFetchState = 0;

    private Bitmap bitmap;
    private ProgressDialog mProgressDialog;

    private ArrayList<ItemPost> itemPosts = new ArrayList<>();
    private ArrayList<ItemPostDetailInfoView> itemPostDetailInfoViews = new ArrayList<>();
    private ArrayList<ParseFile> itemPostImages = new ArrayList<>();
    private boolean isShowingItems = false;

    private ArrayList<String> filterKeyArray = new ArrayList<>();
    private ArrayList<String> conditionKeyArray = new ArrayList<>();
    private ArrayList<String> locationKeyArray = new ArrayList<>();
    private ArrayList<String> saleTypeKeyArray = new ArrayList<>();

    int width, height;
    private int mTouchSlop;
    private int mFlingSlop;
    private final Rect boundsRect = new Rect();
    private Rect containerOriginRect = new Rect();

//    MoveGestureDetector moveGestureDetector;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        checkAndRequestPermissions();
        mainActivity = this;
    }

    private void initUI() {

        fragmentManager = getSupportFragmentManager();
        mFragmentStack = new Stack<>();

        btnMenuDrawer = findViewById(R.id.btn_menu_drawer);
        txtPageTitle = findViewById(R.id.text_page_title);
        btnTabHome = findViewById(R.id.btn_tab_home);
        btnTabProfile = findViewById(R.id.btn_tab_profile);
        btnTabCamera = findViewById(R.id.btn_tab_camera);
        btnTabChat = findViewById(R.id.btn_tab_message);
        btnTabActivity = findViewById(R.id.btn_tab_activity);
        btnActionComposeMessage = findViewById(R.id.btn_action_compose_message);
//        btnActionFilter = findViewById(R.id.btn_action_filter);
        btnActionDone = findViewById(R.id.btn_action_done);
        btnBack = findViewById(R.id.btn_back_menu);
//        btnCloseSearch = findViewById(R.id.btn_close_search);
//        btnSearch = findViewById(R.id.btn_search);
        searchView = findViewById(R.id.edit_search_key);
        itemPostContainer = findViewById(R.id.item_image_detail_container);
        itemPostInfoContainer = findViewById(R.id.item_info_container_container);
        itemPostImage = findViewById(R.id.image_item_post_image);
        btnBinocular = findViewById(R.id.btn_binocular);
//        btnAccessories = findViewById(R.id.btn_filter_accessory);
//        btnBag = findViewById(R.id.btn_filter_bag);
//        btnSuit = findViewById(R.id.btn_filter_suit);
//        btnSkirt = findViewById(R.id.btn_filter_skirt);
//        btnShoes = findViewById(R.id.btn_filter_shoes);
//        btnBlazer = findViewById(R.id.btn_filter_blazer);
//        btnDress = findViewById(R.id.btn_filter_dress);
//        btnPants = findViewById(R.id.btn_filter_pants);
//        btnShirt = findViewById(R.id.btn_filter_shirt);
//        btnJacket = findViewById(R.id.btn_filter_jacket);
//        searchKeyContainer = findViewById(R.id.filter_button_container);
        searchKitContainer = findViewById(R.id.search_kit_container);
//        btnList = findViewById(R.id.btn_show_list);
        btnGrid = findViewById(R.id.btn_show_grid);
        listStyleContainer = findViewById(R.id.list_style_container);

        simpleSideDrawer = new SimpleSideDrawer(this);
        simpleSideDrawer.setLeftBehindContentView(R.layout.nav_drawer_menu);

        menuListView = simpleSideDrawer.findViewById(R.id.nav_menu_listview);
        menuListView.setAdapter(new MenuListAdapter(this, ParseUser.getCurrentUser().get("displayName").toString(), ParseUser.getCurrentUser().get("usernameFix").toString(), ParseUser.getCurrentUser().getParseFile("profilePictureSmall")));

        menuListView.setOnItemClickListener(this);
        btnMenuDrawer.setOnClickListener(this);
        btnTabActivity.setOnClickListener(this);
        btnTabChat.setOnClickListener(this);
        btnTabCamera.setOnClickListener(this);
        btnTabProfile.setOnClickListener(this);
        btnTabHome.setOnClickListener(this);
        btnBack.setOnClickListener(this);
//        btnJacket.setOnClickListener(this);
//        btnDress.setOnClickListener(this);
//        btnAccessories.setOnClickListener(this);
//        btnSuit.setOnClickListener(this);
//        btnBlazer.setOnClickListener(this);
//        btnShoes.setOnClickListener(this);
//        btnSkirt.setOnClickListener(this);
//        btnShirt.setOnClickListener(this);
//        btnPants.setOnClickListener(this);
//        btnBag.setOnClickListener(this);
        btnActionComposeMessage.setOnClickListener(this);
        btnActionDone.setOnClickListener(this);
        btnBinocular.setOnClickListener(this);
//        btnSearch.setOnClickListener(this);
//        btnCloseSearch.setOnClickListener(this);
//        btnActionFilter.setOnClickListener(this);
//        btnList.setOnClickListener(this);
        btnGrid.setOnClickListener(this);
        searchKitContainer.setOnClickListener(this);

        btnCurrentTab = btnTabHome;
        btnCurrentTab.setSelected(true);
        addFragment(HomeFragment.newInstance());
//        btnActionFilter.setVisibility(View.VISIBLE);
        listStyleContainer.setVisibility(View.VISIBLE);
//        btnCurrentType = btnList;
//        btnCurrentType.setSelected(true);
        btnGrid.setSelected(true);

        btnActionComposeMessage.setVisibility(View.INVISIBLE);
        btnActionDone.setVisibility(View.INVISIBLE);
        btnBack.setVisibility(View.INVISIBLE);

        parseUtil = new ParseUtil(this, this);

        updateAllPostAndActivity();
        checkFireBase();

        itemPostImage.setOnTouchListener(this);
        itemPostContainer.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, new GestureListener());
        ViewConfiguration viewConfiguration = ViewConfiguration.get(this);
        mFlingSlop = viewConfiguration.getScaledMinimumFlingVelocity();
        mTouchSlop = viewConfiguration.getScaledTouchSlop();

        containerOriginRect.set(0, 0, itemPostContainer.getWidth(), itemPostContainer.getHeight());
    }

    private void checkFireBase() {
        Task<AuthResult> task = FirebaseAuth.getInstance().signInWithEmailAndPassword("chat@stylelist.com", "chat123456");
        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d("MainActivity", "FireBase auth success");
            }
        });
    }

    public void updateAllPostAndActivity() {
        currentFetchState = 0;
        parseUtil.getAllPosts();
        parseUtil.getAllActivities();
        parseUtil.getAllUsers();
    }

    private void updateUI(Fragment currentFragment) {
        updateBackVisibility();
        updateActionBar(currentFragment);
    }

    private void updateTab(ImageButton btnCurrentTab) {
        Fragment newFragment = null;
        if (btnCurrentTab == btnTabHome) {
            newFragment = HomeFragment.newInstance();
        } else if (btnCurrentTab == btnTabProfile) {
            newFragment = ProfileFragment.newInstance(ParseUser.getCurrentUser());
        } else if (btnCurrentTab == btnTabChat) {
            newFragment = MessageFragment.newInstance();
        } else if (btnCurrentTab == btnTabActivity) {
            newFragment = NotificationFragment.newInstance();
        }
        switchTabFragment(newFragment);
        updateUI(newFragment);
    }

    public void addFragment(Fragment fragment) {
        String tag = fragment.toString();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mFragmentStack.size() != 0) {
            transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        }
        transaction.add(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(tag);
        mFragmentStack.add(tag);
        transaction.commit();
        updateUI(fragment);
    }

    private void switchTabFragment(Fragment fragment) {
        String tag = fragment.toString();
        mFragmentStack.clear();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(tag);
        mFragmentStack.add(tag);
        transaction.commit();
        updateUI(fragment);
    }

    public void switchFragment(Fragment fragment) {

        String tag = fragment.toString();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        Fragment currentFragment = fragmentManager.findFragmentByTag(mFragmentStack.peek());
        transaction.hide(currentFragment);
        transaction.add(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(tag);
        mFragmentStack.add(tag);
        transaction.commit();
        updateUI(fragment);
    }

    private void removeFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.findFragmentByTag(mFragmentStack.pop());
//        if (currentFragment.getClass().toString().equals(ChattingFragment.class.toString())) {
            transaction.remove(currentFragment);
//            currentFragment = fragmentManager.findFragmentByTag(mFragmentStack.pop());
//        }
        Fragment fragment = fragmentManager.findFragmentByTag(mFragmentStack.peek());
        if (fragment.getClass().toString().equals(ChooseFriendFragment.class.toString())) {
            transaction.remove(fragment);
            currentFragment = fragmentManager.findFragmentByTag(mFragmentStack.pop());
            fragment = fragmentManager.findFragmentByTag(mFragmentStack.peek());
        }
        transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        transaction.remove(currentFragment);
        transaction.show(fragment);
        transaction.commit();
        updateUI(fragment);
    }

    private void updateActionBar(Fragment fragment) {
        String currentFragmentClass = fragment.getClass().toString();
        String fragmentTitle = "";

        if (mFragmentStack.size() > 1) {
//            btnActionFilter.setVisibility(View.INVISIBLE);
            listStyleContainer.setVisibility(View.INVISIBLE);
            btnActionComposeMessage.setVisibility(View.INVISIBLE);
            btnActionDone.setVisibility(View.INVISIBLE);
        }
        hideSearch();

        if (currentFragmentClass.equals(HomeFragment.class.toString())) {
            fragmentTitle = this.getString(R.string.tab_home);
//            btnActionFilter.setVisibility(View.VISIBLE);
            listStyleContainer.setVisibility(View.VISIBLE);
            btnActionComposeMessage.setVisibility(View.INVISIBLE);
            btnActionDone.setVisibility(View.INVISIBLE);
            showSearch();
        } else if (currentFragmentClass.equals(ProfileFragment.class.toString())) {
            fragmentTitle = this.getString(R.string.tab_profile);
//            btnActionFilter.setVisibility(View.INVISIBLE);
            listStyleContainer.setVisibility(View.INVISIBLE);
            btnActionComposeMessage.setVisibility(View.INVISIBLE);
            btnActionDone.setVisibility(View.INVISIBLE);
        } else if (currentFragmentClass.equals(MessageFragment.class.toString())) {
            fragmentTitle = this.getString(R.string.tab_chat);
//            btnActionFilter.setVisibility(View.INVISIBLE);
            listStyleContainer.setVisibility(View.INVISIBLE);
            btnActionComposeMessage.setVisibility(View.VISIBLE);
            btnActionDone.setVisibility(View.INVISIBLE);
        } else if (currentFragmentClass.equals(NotificationFragment.class.toString())) {
            fragmentTitle = this.getString(R.string.tab_activity);
//            btnActionFilter.setVisibility(View.INVISIBLE);
            listStyleContainer.setVisibility(View.INVISIBLE);
            btnActionComposeMessage.setVisibility(View.INVISIBLE);
            btnActionDone.setVisibility(View.INVISIBLE);
        } else if (currentFragmentClass.equals(PostDetailFragment.class.toString())) {
            fragmentTitle = this.getString(R.string.post_detail_title);
        } else if (currentFragmentClass.equals(FindFriendFragment.class.toString())) {
            fragmentTitle = this.getString(R.string.find_friend_title);
        } else if (currentFragmentClass.equals(ChooseFriendFragment.class.toString())) {
            fragmentTitle = this.getString(R.string.choose_friend_title);
            btnActionDone.setVisibility(View.VISIBLE);
        } else if (currentFragmentClass.equals(ChattingFragment.class.toString())) {
            ChattingFragment fragment1 = (ChattingFragment) fragment;
            fragmentTitle = fragment1.chattingUser.getString("displayName");
        } else if (currentFragmentClass.equals(FollowFragment.class.toString())) {
            FollowFragment followFragment = (FollowFragment) fragment;
            if (followFragment.following) {
                fragmentTitle = "Followings";
            } else {
                fragmentTitle = "Followers";
            }
        } else if (currentFragmentClass.equals(BookmarksFragment.class.toString())) {
//            listStyleContainer.setVisibility(View.VISIBLE);
            fragmentTitle = "Bookmarks";
        }

        txtPageTitle.setText(fragmentTitle);
    }

    private void updateBackVisibility() {
        if (mFragmentStack.size() > 1) {
            showBack(true);
        } else {
            showBack(false);
        }
    }

    private void showBack(boolean canShow) {
        if (canShow) {
            btnBack.setVisibility(View.VISIBLE);
            btnMenuDrawer.setVisibility(View.INVISIBLE);
        } else {
            btnBack.setVisibility(View.INVISIBLE);
            btnMenuDrawer.setVisibility(View.VISIBLE);
        }
    }

    private void logOut() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Logout",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        ParseUser.logOut();
                        goToIntroActivity();
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void goToIntroActivity() {
        Intent Intent = new Intent(this, IntroActivity.class);
        startActivity(Intent);
        finish();
    }

    private void showCameraDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_camera_gallery);
        RelativeLayout btnCamera = dialog.findViewById(R.id.btn_dialog_take_photo);
        RelativeLayout btnGallery = dialog.findViewById(R.id.btn_dialog_choose_photo);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPhoto(true);
                dialog.dismiss();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPhoto(false);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void requestPhoto(boolean isCamera) {
        if (isCamera) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == REQUEST_SEARCH_FILTER)
                updatePostList(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bitmap = bm;
        promptToPosting();
    }

    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        String photoPath = UtilFunctions.pathFromUri(this, UtilFunctions.getImageUri(this, bitmap));
        ExifInterface ei;
        try {
            ei = new ExifInterface(photoPath);
            String orientation = ei.getAttribute(ExifInterface.TAG_ORIENTATION);
            if (orientation.equals(ExifInterface.ORIENTATION_NORMAL)) {
            } else if (orientation.equals(ExifInterface.ORIENTATION_ROTATE_90+"")) {
                bitmap = UtilFunctions.rotateImage(bitmap, 90);
            } else if (orientation.equals(ExifInterface.ORIENTATION_ROTATE_180+"")) {
                bitmap = UtilFunctions.rotateImage(bitmap, 180);
            } else if (orientation.equals(ExifInterface.ORIENTATION_ROTATE_270+"")) {
                bitmap = UtilFunctions.rotateImage(bitmap, 270);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        promptToPosting();
    }

    public void promptToEdit() {
        if (null != bitmap) {
            originalPhoto = bitmap;
            editedPhoto = bitmap;
            EditPhotoActivity.start(this);
        }
    }

    private void promptToPosting() {
        Intent intent = new Intent(this, MyStyleActivity.class);
        intent.putExtra(Constants.PARENT_ACTIVITY, Constants.PARENT_IS_PHOTO);
        startActivity(intent);
    }

    public void prepareItemPosts(Post post) {
//        searchKeyContainer.setVisibility(View.INVISIBLE);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Getting items...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        currentPost = post;
        parseUtil.getItemPostsForPost(post.getObjectId());
    }

    private void generateItemPostDetails() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        itemPostContainer.setX(containerOriginRect.left);
        itemPostContainer.setY(containerOriginRect.top);
        itemPostContainer.setRotation(0);
        itemPostContainer.setAlpha(1.0f);
        itemPostContainer.setVisibility(View.VISIBLE);
        ParseFile photoFile = currentPost.getImage();
        if (photoFile != null) {
            Picasso.with(this).load(photoFile.getUrl()).placeholder(R.drawable.placeholder_photo).into(itemPostImage);
        }
    }

    private void showItemPostViews() {
        if (!isShowingItems) {
            isShowingItems = true;
            if (itemPostDetailInfoViews.size() > 0) {
                for (int i = 0; i < itemPostDetailInfoViews.size(); i++) {
                    itemPostDetailInfoViews.get(i).setVisibility(View.VISIBLE);
                }
            } else {
                for (int i = 0; i < itemPosts.size(); i++) {
                    ItemPostDetailInfoView tempInfoView = new ItemPostDetailInfoView(this, itemPosts.get(i), this);
                    itemPostImages.add(itemPosts.get(i).getImage());
                    itemPostDetailInfoViews.add(tempInfoView);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    itemPostInfoContainer.invalidate();
                    params.setMargins((int) ((itemPostInfoContainer.getWidth() * itemPosts.get(i).getViewXPosition())),
                            (int) (itemPostInfoContainer.getHeight() * itemPosts.get(i).getViewYPosition()),
                            params.rightMargin, params.bottomMargin);
                    tempInfoView.setLayoutParams(params);
                    itemPostInfoContainer.addView(tempInfoView);
                }
            }
        } else {
            isShowingItems = false;
            for (int i = 0; i < itemPostDetailInfoViews.size(); i++) {
                itemPostDetailInfoViews.get(i).setVisibility(View.INVISIBLE);
            }
            Picasso.with(this).load(currentPost.getImage().getUrl()).placeholder(R.drawable.placeholder_photo).into(itemPostImage);

        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnMenuDrawer) {
            simpleSideDrawer.toggleLeftDrawer();
        } else if (v == btnBack) {
            onBackPressed();
        } else if (v == btnTabCamera) {
//            showCameraDialog();
            requestPhoto(false);
        } else if (v == btnActionComposeMessage) {
            addFragment(ChooseFriendFragment.newInstance());
        } else if (v == btnActionDone) {
            ChooseFriendFragment fragment = (ChooseFriendFragment) fragmentManager.findFragmentByTag(mFragmentStack.peek());
            fragment.createChatSession();
        } else if (v.getClass().toString().equals(ImageButton.class.toString())){
            if (btnCurrentTab != v) {
                btnCurrentTab.setSelected(false);
                btnCurrentTab = (ImageButton) v;
                btnCurrentTab.setSelected(true);
                updateTab(btnCurrentTab);
            }
        } else if (v == btnBinocular) {
            showItemPostViews();
//        } else if (v.getClass().toString().equals(CustomFontTextView.class.toString())) {
//            updateFilterKeys((CustomFontTextView) v);
//        } else if (v == btnSearch) {
//            if (searchKeyContainer.getVisibility() == View.INVISIBLE) {
//                searchKeyContainer.setVisibility(View.VISIBLE);
//            } else {
//                searchKeyContainer.setVisibility(View.INVISIBLE);
//            }
//        } else if (v == btnCloseSearch) {
//            filterKeyArray.clear();
//            searchView.setText("");
//            CustomFontTextView[] btnFilters = {btnAccessories, btnBag, btnShirt, btnPants, btnSkirt, btnShoes, btnBlazer, btnJacket, btnDress, btnSuit};
//            for (CustomFontTextView button : btnFilters) {
//                button.setSelected(false);
//            }
//            btnCloseSearch.setVisibility(View.INVISIBLE);
        } else if (v == btnGrid) {
            btnGrid.setSelected(!btnGrid.isSelected());
            Fragment currentFragment = fragmentManager.findFragmentByTag(mFragmentStack.peek());
            if (currentFragment.getClass().toString().equals(HomeFragment.class.toString())) {
                HomeFragment homeFragment = (HomeFragment) currentFragment;
                if (btnGrid.isSelected()) {
                    homeFragment.updatePostListStyle(HomeFragment.ListingStyle.LIST);
                } else {
                    homeFragment.updatePostListStyle(HomeFragment.ListingStyle.GRID);
                }
            }
        } else if (v == searchKitContainer) {
            Intent intent = new Intent(this, FilterActivity.class);
            intent.putStringArrayListExtra("filter_keys", filterKeyArray);
            intent.putStringArrayListExtra("condition_keys", conditionKeyArray);
            intent.putStringArrayListExtra("location_keys", locationKeyArray);
            intent.putStringArrayListExtra("sale_type_keys", saleTypeKeyArray);
            startActivityForResult(intent, REQUEST_SEARCH_FILTER);
        }
//        else if (v == btnActionFilter) {
//            searchKeyContainer.setVisibility(View.INVISIBLE);
//            updatePostList();
//        }
    }

//    private void updateFilterKeys(CustomFontTextView keyButton) {
//        keyButton.setSelected(!keyButton.isSelected());
//        if (keyButton.isSelected()) {
//            filterKeyArray.add(keyButton.getText().toString());
//        } else {
//            if (filterKeyArray.contains(keyButton.getText().toString()))
//                filterKeyArray.remove(keyButton.getText().toString());
//        }
//        if (filterKeyArray.size() > 0) {
////            btnCloseSearch.setVisibility(View.VISIBLE);
//        } else {
////            btnCloseSearch.setVisibility(View.INVISIBLE);
//        }
//        String keyText = "";
//        for (String key : filterKeyArray) {
//            keyText = keyText + " #" + key;
//        }
//        searchView.setText(keyText);
//    }

    private void updatePostList(Intent resultData) {
        filterKeyArray = resultData.getStringArrayListExtra("filter_keys");
        conditionKeyArray = resultData.getStringArrayListExtra("condition_keys");
        locationKeyArray = resultData.getStringArrayListExtra("location_keys");
        saleTypeKeyArray = resultData.getStringArrayListExtra("sale_type_keys");
        Log.d("Filter Key Count", String.valueOf(filterKeyArray.size()));
        Fragment currentFragment = fragmentManager.findFragmentByTag(mFragmentStack.peek());
        if (currentFragment.getClass().toString().equals(HomeFragment.class.toString())) {
            HomeFragment homeFragment = (HomeFragment) currentFragment;
            homeFragment.updatePostListWithFilter(filterKeyArray, conditionKeyArray, locationKeyArray, saleTypeKeyArray);
            String keyText = "";
            for (String key : filterKeyArray) {
                keyText = keyText + " #" + key;
            }
            for (String key : conditionKeyArray) {
                keyText = keyText + " #" + key;
            }
            for (String key : locationKeyArray) {
                keyText = keyText + " #" + key;
            }
            for (String key : saleTypeKeyArray) {
                keyText = keyText + " #" + key;
            }
            if (keyText.isEmpty()) {
                searchView.setText("Search All");
            } else {
                searchView.setText(keyText);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragmentStack.size() > 1 ){
            // Remove the fragment
            removeFragment();
        }
        if (itemPostContainer.getVisibility() == View.VISIBLE) {
            isShowingItems = false;
            itemPostInfoContainer.removeAllViews();
            itemPostContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == menuListView) {
            if (position == 0) {
                btnCurrentTab.setSelected(false);
                btnCurrentTab = btnTabProfile;
                btnCurrentTab.setSelected(true);
                updateTab(btnTabProfile);
            } else {
                MenuItem menuItem = (MenuItem) parent.getAdapter().getItem(position - 1);
                if (!menuItem.isHeader) {
                    switch (menuItem.getTitleResId()) {
                        case R.string.menu_find_friends:
                            addFragment(FindFriendFragment.newInstance());
                            break;
                        case R.string.menu_settings:
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                            break;
                        case R.string.menu_log_out:
                            logOut();
                            break;
                        case R.string.menu_bookmarks:
                            addFragment(BookmarksFragment.newInstance());
                            break;
                        case R.string.menu_terms:
                            startActivity(new Intent(MainActivity.this, WebViewActivity.class));
                            break;
                        case R.string.menu_contact:
                            startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                            break;
                        case R.string.menu_help:
                            startActivity(new Intent(MainActivity.this, HelpActivity.class));
                            break;
                        default:
                            break;
                    }
                }
            }
            simpleSideDrawer.toggleLeftDrawer();
        }
    }

    private  boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onGetArray(Result arrayResult) {
        if (arrayResult.getT().getClass() == ArrayList.class) {
            ArrayList<Object> objects = (ArrayList<Object>) arrayResult.getT();
            if (objects.size() > 0) {
                if (objects.get(0).getClass() == Post.class) {
                    allPostArray.clear();
                    allPostArray = (ArrayList<Post>) arrayResult.getT();
                    currentFetchState += REQUEST_GET_POSTS;
                } else if (objects.get(0).getClass() == ParseUser.class) {
                    allUsers.clear();
                    ArrayList<ParseUser> tempUsers = (ArrayList<ParseUser>) arrayResult.getT();
                    for (int i = 0; i < tempUsers.size(); i++) {
                        if (!tempUsers.get(i).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                            allUsers.add(tempUsers.get(i));
                        }
                    }
                } else if (objects.get(0).getClass() == ItemPost.class) {
                    itemPostDetailInfoViews.clear();
                    itemPostImages.clear();
                    itemPosts.clear();
                    itemPosts.addAll((ArrayList<ItemPost>) arrayResult.getT());
                    generateItemPostDetails();
                } else {
                    allActivityArray.clear();
                    allActivityArray = (ArrayList<Notification>) arrayResult.getT();
                    currentFetchState += REQUEST_GET_NOTIFICATIONS;
                }
            } else {
                if (fragmentManager.findFragmentByTag(mFragmentStack.peek()).getClass() == HomeFragment.class) {
                    HomeFragment fragment = (HomeFragment) fragmentManager.findFragmentByTag(mFragmentStack.peek());
                    fragment.refreshListView();
                }
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    Toast.makeText(this, "No items for this Post.", Toast.LENGTH_SHORT).show();
                }
            }
            if (currentFetchState == REQUEST_GOT_ALL_DATA) {
                if (fragmentManager.findFragmentByTag(mFragmentStack.peek()).getClass() == HomeFragment.class) {
                    HomeFragment fragment = (HomeFragment) fragmentManager.findFragmentByTag(mFragmentStack.peek());
                    fragment.refreshListView();
                }
            }
        }
    }

    @Override
    public void onItemClick(ItemPost itemPost) {
        currentItemPost = itemPost;
        startActivity(new Intent(this, ItemPostDetailActivity.class));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
//        if (v == itemPostImage) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    determineItemPoint(event.getX(), event.getY());
                    break;
                default:
                    return false;
            }
            return true;
//        } else {
//
//        }
//        return true;
    }

    private void determineItemPoint(float x, float y) {
        int itemIndex = -1;
        for (int i = 0; i < itemPosts.size(); i++) {
            float top, bottom, left, right;
            top = itemPosts.get(i).getYPosition() * height;
            bottom = itemPosts.get(i).getRectHeight() * height + top;
            left = itemPosts.get(i).getXPosition() * width;
            right = left + itemPosts.get(i).getRectWidth() * width;
            if (x > left && x < right && y > top && y < bottom) {
                Log.d("Touched Item Index", String.valueOf(i));
                itemIndex = i;
            }
            if (itemPostDetailInfoViews.size() > 0) {
                itemPostDetailInfoViews.get(i).setVisibility(View.INVISIBLE);
                Picasso.with(this).load(currentPost.getImage().getUrl()).placeholder(itemPostImage.getDrawable()).into(itemPostImage);
            }
        }
        if (itemIndex != -1 && itemPostDetailInfoViews.size() > 0) {
            itemPostDetailInfoViews.get(itemIndex).setVisibility(View.VISIBLE);
            Picasso.with(this).load(itemPostImages.get(itemIndex).getUrl()).placeholder(itemPostImage.getDrawable()).into(itemPostImage);
        }
    }

    private void getRectOfImageView() {
        width = itemPostImage.getMeasuredWidth();
        height = itemPostImage.getMeasuredHeight();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getRectOfImageView();
    }

    private void hideSearch() {
//        searchKeyContainer.setVisibility(View.INVISIBLE);
        searchKitContainer.setVisibility(View.INVISIBLE);
    }

    private void showSearch() {
//        searchKeyContainer.setVisibility(View.VISIBLE);
        searchKitContainer.setVisibility(View.VISIBLE);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("Fling", "Fling with " + velocityX + ", " + velocityY);

            float dx = e2.getX() - e1.getX();
            if (Math.abs(dx) > mTouchSlop &&
                    Math.abs(velocityX) > Math.abs(velocityY) &&
                    Math.abs(velocityX) > mFlingSlop * 3) {

                leave(velocityX, velocityY);

                return true;
            } else
                return false;
        }
    }

    public void leave(float velocityX, float velocityY) {
//        if (!locked) {
//            locked = true; // Lock swipe until current card is dismissed

            final View topCard = itemPostContainer;

            float targetX = topCard.getX();
            float targetY = topCard.getY();
            long duration = 0;

            boundsRect.set(0 - topCard.getWidth() - 100, 0 - topCard.getHeight() - 100, topCard.getWidth() + 100, topCard.getHeight() + 100);

            while (boundsRect.contains((int) targetX, (int) targetY)) {
                targetX += velocityX / 10;
                targetY += velocityY / 10;
                duration += 100;
            }

            duration = Math.min(500, duration);

            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    locked = false; // Unlock swipe
                }
            }, duration + 200);

            topCard.animate()
                    .setDuration(duration)
                    .alpha(.75f)
                    .setInterpolator(new LinearInterpolator())
                    .x(targetX)
                    .y(targetY)
                    .rotation(Math.copySign(45, velocityX))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            itemPostContainer.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            onAnimationEnd(animation);
                        }
                    });
//        }
    }
}
