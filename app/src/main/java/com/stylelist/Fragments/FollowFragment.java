package com.stylelist.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseUser;
import com.stylelist.Adapters.FindFriendAdapter;
import com.stylelist.R;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowFragment extends Fragment {

    public boolean following = false;
    private ParseUser selectedUser;
    private FindFriendAdapter findFriendAdapter;
    private ListView friendListView;
    private ArrayList<ParseUser> matchedUsers = new ArrayList<>();

    public FollowFragment() {
        // Required empty public constructor
    }

    public static FollowFragment newInstance(boolean following, ParseUser selectedUser) {
        FollowFragment fragment = new FollowFragment();
        fragment.following = following;
        fragment.selectedUser = selectedUser;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        friendListView = view.findViewById(R.id.follow_list_view);
        getMatchedFriends();
        return view;
    }

    private void getMatchedFriends() {
        matchedUsers.clear();
        if (following) {
            matchedUsers.addAll(UtilFunctions.getFollowingUsers(selectedUser));
        } else {
            matchedUsers.addAll(UtilFunctions.getFollowerUsers(selectedUser));
        }
        findFriendAdapter = new FindFriendAdapter(getActivity(), matchedUsers, true);
        friendListView.setAdapter(findFriendAdapter);
    }
}
