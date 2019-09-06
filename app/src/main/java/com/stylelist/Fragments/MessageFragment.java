package com.stylelist.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parse.ParseUser;
import com.stylelist.Activity.MainActivity;
import com.stylelist.Adapters.MessageListAdapter;
import com.stylelist.Models.MessageItem;
import com.stylelist.Models.MessageSession;
import com.stylelist.R;
import com.stylelist.Utils.UtilFunctions;

import java.util.ArrayList;

import static com.stylelist.Utils.StyleListApp.allUsers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements ValueEventListener, AdapterView.OnItemClickListener {


    private SwipeRefreshLayout messageRefresher;
    private ListView listViewMessage;

    private ArrayList<DataSnapshot> messageSessions = new ArrayList<>();

    private ParseUser currentUser = ParseUser.getCurrentUser();

    private MessageListAdapter messageListAdapter;

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        messageRefresher = view.findViewById(R.id.message_list_refresher);
        listViewMessage = view.findViewById(R.id.message_listview);

        initUI();
        return view;
    }

    private void initUI() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Message").addValueEventListener(this);

        messageListAdapter = new MessageListAdapter(getActivity(), messageSessions, allUsers);
        listViewMessage.setAdapter(messageListAdapter);
        listViewMessage.setOnItemClickListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        int count = 0;
        messageSessions.clear();
        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

            MessageSession messageSession = new MessageSession();
            for (DataSnapshot dataSnapshot1 : noteDataSnapshot.getChildren()) {
                MessageItem messageItem = dataSnapshot1.getValue(MessageItem.class);
                messageSession.messageItems.add(messageItem);
            }
            String id = noteDataSnapshot.getKey();
            if (id.contains(currentUser.getObjectId()) && messageSession.messageItems.size() > 0) {
                count++;
                messageSessions.add(noteDataSnapshot);
            }
        }
        Log.d("MessageFragment", "I have " + String.valueOf(count) + " message sessions.");
        messageListAdapter.updateDataSet(messageSessions, allUsers);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity mainActivity = (MainActivity) getActivity();
        DataSnapshot messageSessionData = messageListAdapter.getItem(position);
        String receiverId = messageSessionData.getKey().replace(currentUser.getObjectId(), "");
        receiverId = receiverId.replace("+", "");
        mainActivity.addFragment(ChattingFragment.newInstance(UtilFunctions.findUserWithId(receiverId)));
    }
}
