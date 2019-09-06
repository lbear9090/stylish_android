//package codelight.social_network.Adapters;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.parse.ParseFile;
//import com.parse.ParseQuery;
//import com.parse.ParseQueryAdapter;
//import com.parse.ParseUser;
//import com.squareup.picasso.Picasso;
//
//import java.util.Date;
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import codelight.social_network.Activity.CommentActivity;
//import codelight.social_network.CustomViews.CustomFontTextView;
//import codelight.social_network.ParseModels.Activity;
//import lux.socialnetwork.R;
//import codelight.social_network.Utils.UtilFunctions;
//
///**
// * Created by Yannick Erpelding on 30.07.15.
// */
//
//
//public class OldCommentListAdapter extends ParseQueryAdapter<Activity> {
//
//    private List<String> itemList;
//    private Context context;
//    private ParseFile thumbnailFile;
//    private CircleImageView profileImage;
//    private CustomFontTextView txtUserName, txtComment, txtTimeStamp;
//    private ParseUser commentOwner;
//
//
//
//    public OldCommentListAdapter(Context context) {
//        super(context, new ParseQueryAdapter.QueryFactory<Activity>() {
//            public ParseQuery create() {
//
//                ParseQuery<Activity> CommentQuery = new ParseQuery<Activity>("Activity");
//                CommentQuery.whereEqualTo("post", CommentActivity.getPhoto());
//                CommentQuery.whereEqualTo("type", "comment");
//                CommentQuery.orderByAscending("created_at");
//                return CommentQuery;
//
//            }
//        });
//        this.context = context;
//    }
//
//    //@Override
//    public View getItemView(final Activity comment, View v, ViewGroup parent) {
//        if (v == null) {
//            v = View.inflate(getContext(), R.layout.comment_list_items, null);
//        }
//        super.getItemView(comment, v, parent);
//        commentOwner = comment.getUser();
//
//        profileImage  = v.findViewById(R.id.image_comment_item_user_profile);
//        txtUserName = v.findViewById(R.id.text_comment_item_user_name);
//        txtComment = v.findViewById(R.id.text_comment_item_comment);
//        txtTimeStamp = v.findViewById(R.id.text_comment_item_timstamp);
//
//        txtComment.setText(comment.get("content").toString());
//        txtUserName.setText(commentOwner.get("displayName").toString());
//
//        thumbnailFile = commentOwner.getParseFile("profilePictureSmall");
//        if (thumbnailFile != null) {
//            Picasso.with(context).load(thumbnailFile.getUrl()).placeholder(R.drawable.avatarplaceholderprofile).into(profileImage);
//        }
//
//        //Set Timestamp
//        Date then = comment.getCreatedAt();
//        Date now = new Date();
//        now.getTime();
//        txtTimeStamp.setText(UtilFunctions.getTimeStamp(comment));
//
////        ProfilePicView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                ParseUser fromUser = CommentUser;
////                goToProfile(v, fromUser);
////            }
////        });
////        UsernameView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                ParseUser fromUser = CommentUser;
////                goToProfile(v,fromUser);
////            }
////        });
//        return v;
//    }
//
//    @Override
//    public int getCount() {
//        return super.getCount();
//    }
//
//    //    public void goToProfile(View v, ParseUser fromUser){
////        Intent myIntent = new Intent(v.getContext(), ProfileActivity.class);
////        //myIntent.putExtra("PhotoID", pPost.getObjectId());
////        myIntent.putExtra("UserID", fromUser.getObjectId());
////        v.getContext().startActivity(myIntent);
////        Log.e("Log", "Go to Profile");
////        Log.e("Notif", "Notif - UserID: " + fromUser.getObjectId());
////    }
//
//}
//
//
