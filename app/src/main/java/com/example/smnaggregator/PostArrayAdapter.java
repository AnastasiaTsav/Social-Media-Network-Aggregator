package com.example.smnaggregator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class PostArrayAdapter extends ArrayAdapter<Post> {

    private static int counter = 0;

    private List<Post> postList;
    private final LayoutInflater inflater;
    private final int layoutResource;

    private final ListView postListView;


    public PostArrayAdapter(@NonNull Context context, int resource, @NonNull List<Post> objects, ListView listView) {
        super(context, resource, objects);
        postList = objects;
        layoutResource = resource;
        inflater = LayoutInflater.from(context);
        postListView = listView;
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        counter++;
        Log.d("ADAPTER", "get view in adapter just called. counter = "+counter);
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = inflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            Log.w("VIEW_HOLDER", "View Holder Created");
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Post currentPost = postList.get(position);

        viewHolder.name.setText(currentPost.getName()+"");
        viewHolder.url.setText(currentPost.getUrl()+"");


        return convertView;
    }

    private static class ViewHolder{
        final TextView name;
        final TextView url;

        ViewHolder(View view){
            name = view.findViewById(R.id.hashtagName);
            url = view.findViewById(R.id.hahshtagURL);

        }
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        postListView.setAdapter(this);
    }
}