package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by BOX on 8/22/2016.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News>news){
        super(context,0,news);
    }

    private String categoryString(String string){
        return string.substring(0,3);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item,parent,false);
        }

        News currentNews = getItem(position);
        String category = categoryString(currentNews.getCategory());
        String title = currentNews.getTitle();

        TextView categoryView = (TextView) listItemView.findViewById(R.id.category);
        categoryView.setText(category);

        TextView titleView = (TextView)listItemView.findViewById(R.id.headline);
        titleView.setText(title);

        return listItemView;
    }

}
