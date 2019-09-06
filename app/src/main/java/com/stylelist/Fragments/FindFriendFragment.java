package com.stylelist.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.parse.ParseUser;
import com.stylelist.Adapters.FindFriendAdapter;
import com.stylelist.R;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

import static com.stylelist.Utils.StyleListApp.allUsers;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindFriendFragment extends Fragment implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    public ListView listViewFriends;
    private SearchView searchFriendName;
    private FindFriendAdapter findFriendAdapter;
    private ArrayList<ParseUser> queriedUsers = new ArrayList<>();

    public FindFriendFragment() {
        // Required empty public constructor
    }

    public static FindFriendFragment newInstance() {
        return new FindFriendFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_friend, container, false);
        listViewFriends = view.findViewById(R.id.list_find_friend);
        searchFriendName = view.findViewById(R.id.search_find_friend);

        queriedUsers.addAll(allUsers);
        findFriendAdapter = new FindFriendAdapter(getActivity(), queriedUsers, false);
        listViewFriends.setAdapter(findFriendAdapter);
        listViewFriends.setOnItemClickListener(this);
        searchFriendName.setOnQueryTextListener(this);
        searchFriendName.setOnCloseListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        findFriendAdapter.followClicked(UtilFunctions.isFollowing(findFriendAdapter.getItem(position)), findFriendAdapter.getItem(position));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        queriedUsers.clear();
        if (query.isEmpty()) {
            queriedUsers.addAll(allUsers);
        } else {
            for (ParseUser user : allUsers) {
                String userName = user.get("usernameFix").toString();
                if (userName.contains(query)) {
                    queriedUsers.add(user);
                }
            }
        }
        findFriendAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onClose() {
        queriedUsers.clear();
        queriedUsers.addAll(allUsers);
        findFriendAdapter.notifyDataSetChanged();
        return false;
    }
}
