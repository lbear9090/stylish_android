package com.stylelist.Interfaces;

import com.stylelist.ParseModels.Post;

/**
 * Created by security on 2/20/2018.
 */

public interface PostListAdapterCallBack {
    void onClickAction(PostItemClickAction action, Post post);
}
