package com.stylelist.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.stylelist.Activity.MainActivity;
import com.stylelist.Adapters.ChatFriendListAdapter;
import com.stylelist.R;

import static com.stylelist.Utils.StyleListApp.allUsers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseFriendFragment extends Fragment implements AdapterView.OnItemClickListener {


    private ListView listViewFriends;
    private ChatFriendListAdapter chatFriendListAdapter;

    public ChooseFriendFragment() {
        // Required empty public constructor
    }

    public static ChooseFriendFragment newInstance() {
        ChooseFriendFragment fragment = new ChooseFriendFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_friend, container, false);

        listViewFriends = view.findViewById(R.id.listview_friends);

        initUI();
        return view;
    }

    private void initUI() {
        chatFriendListAdapter = new ChatFriendListAdapter(getActivity(), allUsers);
        listViewFriends.setAdapter(chatFriendListAdapter);
        listViewFriends.setOnItemClickListener(this);
    }

    public void createChatSession() {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.addFragment(ChattingFragment.newInstance(allUsers.get(position)));
    }
}
