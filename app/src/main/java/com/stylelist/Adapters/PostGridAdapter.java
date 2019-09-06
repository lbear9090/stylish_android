package com.stylelist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stylelist.ParseModels.Post;
import com.stylelist.R;

import java.util.ArrayList;

public class PostGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Post> postArrayList;

    public PostGridAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @Override
    public int getCount() {
        return postArrayList.size();
    }

    @Override
    public Post getItem(int position) {
        return postArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = getItem(position);
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.grid_item_post, parent, false);
            viewHolder.imageView = convertView.findViewById(R.id.image_grid_item_post);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(post.getImage().getUrl()).placeholder(R.drawable.placeholder_photo).into(viewHolder.imageView);
        return convertView;
    }

    public void updateDataSet(ArrayList<Post> postArrayList) {
        this.postArrayList = postArrayList;
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView imageView;
    }
}
