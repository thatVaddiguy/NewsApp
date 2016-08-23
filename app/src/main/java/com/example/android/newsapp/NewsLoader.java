package com.example.android.newsapp;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by BOX on 8/21/2016.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<News> loadInBackground() {
        if (url==null){
            return null;
        }

        List<News> news = Utils.fetchData(url);
        return news;
    }
}
