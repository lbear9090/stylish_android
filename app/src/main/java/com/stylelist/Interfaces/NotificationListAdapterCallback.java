package com.stylelist.Interfaces;

import com.parse.ParseUser;
import com.stylelist.ParseModels.Post;

public interface NotificationListAdapterCallback {
    void onProfileClicked(ParseUser user);
    void onInfoClicked(Post post);
}
