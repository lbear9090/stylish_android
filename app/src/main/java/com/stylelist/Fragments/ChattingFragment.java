package com.stylelist.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parse.ParseUser;
import com.stylelist.Adapters.ChattingListAdapter;
import com.stylelist.Models.MessageItem;
import com.stylelist.R;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChattingFragment extends Fragment implements View.OnClickListener, ValueEventListener {

    public ParseUser chattingUser;
    public String receiverId;

    private RecyclerView listViewChatting;
    private ImageButton btnSendMessage;
    private EditText edtChatContent;
    private ChattingListAdapter chattingListAdapter;
    private ArrayList<MessageItem> messageItems = new ArrayList<>();

    private ParseUser currentUser = ParseUser.getCurrentUser();

    public ChattingFragment() {
        // Required empty public constructor
    }

    public static ChattingFragment newInstance(@Nullable ParseUser chatingUser) {
        ChattingFragment fragment = new ChattingFragment();
        fragment.chattingUser = chatingUser;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chatting, container, false);

        listViewChatting = view.findViewById(R.id.listview_chatting);
        btnSendMessage = view.findViewById(R.id.btn_chat_send);
        edtChatContent = view.findViewById(R.id.edit_chat_content);

        initUI();
        return view;
    }

    private void initUI() {
        btnSendMessage.setOnClickListener(this);

        chattingListAdapter = new ChattingListAdapter(getActivity(), messageItems, currentUser, chattingUser);
        listViewChatting.setAdapter(chattingListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        listViewChatting.setLayoutManager(linearLayoutManager);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Message").child(getChatID()).addValueEventListener(this);
    }

    private void sendMessage(String message) {
        MessageItem messageItem = new MessageItem();
        messageItem.messageContent = message;
        if (chattingUser != null) {
            messageItem.receiverId = chattingUser.getObjectId();
        } else {
            messageItem.receiverId = receiverId;
        }
        messageItem.senderId = ParseUser.getCurrentUser().getObjectId();
        messageItem.timeStamp = Calendar.getInstance().getTimeInMillis();

        FirebaseDatabase.getInstance().getReference().child("Message").child(getChatID()).push().setValue(messageItem, new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.d("ChattingFragment", databaseError.getMessage());
                } else {
                    edtChatContent.setText("");
                }
            }
        });
    }

    private String getChatID() {
        String chatId;
        String senderId = ParseUser.getCurrentUser().getObjectId();
        String receiverId = chattingUser.getObjectId();

        int compare = senderId.compareTo(receiverId);
        if (compare < 0) {
            chatId = senderId + "+" + receiverId;
        } else {
            chatId = receiverId + "+" + senderId;
        }
        return chatId;
    }

    @Override
    public void onClick(View v) {
        String messageContent = edtChatContent.getText().toString();
        if (!messageContent.equals("")) {
            sendMessage(messageContent);
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        messageItems.clear();
        chattingListAdapter.notifyDataSetChanged();
        for (DataSnapshot message : dataSnapshot.getChildren()) {
            MessageItem messageItem = message.getValue(MessageItem.class);
            if (messageItem.senderId.equals(currentUser.getObjectId()) && messageItem.receiverId.equals(chattingUser.getObjectId()) || messageItem.senderId.equals(chattingUser.getObjectId()) && messageItem.receiverId.equals(currentUser.getObjectId())) {
                messageItems.add(messageItem);
            }
        }
        chattingListAdapter.notifyDataSetChanged();
        if (chattingListAdapter.getItemCount() > 0) {
            listViewChatting.smoothScrollToPosition(chattingListAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
