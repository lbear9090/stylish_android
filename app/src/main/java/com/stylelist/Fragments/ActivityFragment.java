package com.stylelist.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.stylelist.Adapters.NotificationListAdapter;
import com.stylelist.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {

    private ListView listViewNotifications;
    private NotificationListAdapter notificationListAdapter;

    public ActivityFragment() {
        // Required empty public constructor
    }

    public static ActivityFragment newInstance() {
        ActivityFragment fragment = new ActivityFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        listViewNotifications = view.findViewById(R.id.listview_notifications);
        initUI();

        return view;
    }

    private void initUI() {
//        swipyRefreshListContainer.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh(SwipyRefreshLayoutDirection direction) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Hide the refresh after 2sec
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                swipyRefreshListContainer.setRefreshing(false);
//                                updateHomeList();
//                            }
//                        });
//                    }
//                }, 2000);
//            }
//        });

//        ParseObject.registerSubclass(Activity.class);

        // Initialize ListView and set initial view to Adapter
        notificationListAdapter = new NotificationListAdapter(getActivity());
        listViewNotifications.setAdapter(notificationListAdapter);
        //notificationListAdapter.loadObjects();
    }

    private void updateHomeList() {
        listViewNotifications.smoothScrollToPosition(0);
        listViewNotifications.setAdapter(notificationListAdapter);
        //notificationListAdapter.loadObjects();
    }
}
