package com.example.smnaggregator;
import android.content.Context;
import android.text.Layout;
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

public class HashtagArrayAdapter extends ArrayAdapter<Hashtag> {

    private static int counter = 0;

    private List<Hashtag> hashtagList;
    private final LayoutInflater inflater;
    private final int layoutResource;

    private ListView hashtagListView;


    public HashtagArrayAdapter(@NonNull Context context, int resource, @NonNull List<Hashtag> objects, ListView listView) {
        super(context, resource, objects);
        hashtagList = objects;
        layoutResource = resource;
        inflater = LayoutInflater.from(context);
        hashtagListView= listView;
    }


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

        Hashtag currentTweet = hashtagList.get(position);



        viewHolder.createdAt.setText(currentTweet.getCreatedAt()+"");
        viewHolder.text.setText(currentTweet.getText()+"");

        return convertView;
    }

    private class ViewHolder{
        final TextView createdAt;
        final TextView text;


        ViewHolder(View view){
            createdAt = view.findViewById(R.id.createdAt);
            text = view.findViewById(R.id.tweetText);

        }
    }


    @Override
    public int getCount() {
        return hashtagList.size();
    }

    public void setHashtagList(List<Hashtag> hashtagList) {
        this.hashtagList = hashtagList;
        hashtagListView.setAdapter(this);
    }
}