package com.stylelist.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseUser;
import com.stylelist.Activity.MainActivity;
import com.stylelist.Adapters.NotificationListAdapter;
import com.stylelist.Interfaces.NotificationListAdapterCallback;
import com.stylelist.ParseModels.Notification;
import com.stylelist.ParseModels.Post;
import com.stylelist.R;

import java.util.ArrayList;

import static com.stylelist.Utils.StyleListApp.allActivityArray;
import static com.stylelist.Utils.StyleListApp.mainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements NotificationListAdapterCallback {

    private ListView listViewNotifications;
    private NotificationListAdapter notificationListAdapter;
    private ArrayList<Notification> notifications = new ArrayList<>();

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        listViewNotifications = view.findViewById(R.id.listview_notifications);
        initUI();

        return view;
    }

    private void initUI() {
        notifications.clear();
        for (Notification notification: allActivityArray) {
            if (notification.getToUser() != null && notification.getToUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId()) && notification.getFromUser() != null && !notification.getFromUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                notifications.add(notification);
            }
        }
        notificationListAdapter = new NotificationListAdapter(getActivity(), notifications, this);
        listViewNotifications.setAdapter(notificationListAdapter);
    }

    private void updateHomeList() {
        listViewNotifications.smoothScrollToPosition(0);
        listViewNotifications.setAdapter(notificationListAdapter);
    }

    @Override
    public void onProfileClicked(ParseUser user) {
        if (user != null) {
            mainActivity.addFragment(ProfileFragment.newInstance(user));
        }
    }

    @Override
    public void onInfoClicked(Post post) {
        if (post != null) {
            mainActivity.addFragment(PostDetailFragment.newInstance(post));
        }
    }
}
