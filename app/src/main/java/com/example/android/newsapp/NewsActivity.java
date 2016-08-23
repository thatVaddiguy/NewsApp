package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>>  {

    private static String query;
    private static String requestUrl = "http://content.guardianapis.com/search?q=#&api-key=test";
    private static final int NEWS_LOADER_ID=1;
    private NewsAdapter adapter;
    private TextView emptyStateView;
    private ProgressBar progressBar;


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        Uri uri = Uri.parse(requestUrl);
        return new NewsLoader(this,uri.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        progressBar.setVisibility(View.GONE);
        emptyStateView.setText(R.string.No_news);

        if (news!=null&&!news.isEmpty()){
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        emptyStateView = (TextView)findViewById(R.id.empty_view);


        ListView newsListView = (ListView)findViewById(R.id.list);
        newsListView.setEmptyView(emptyStateView);

        adapter = new NewsAdapter(this,new ArrayList<News>());
        newsListView.setAdapter(adapter);

        Button button = (Button)findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText)findViewById(R.id.search_text);
                query=editText.getText().toString();
                requestUrl=requestUrl.replace("#",query);
                Log.e("NewsActivity",requestUrl);
            }
        });

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                News currentNews = adapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();
        if (networkInfo!=null&&networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID,null,this);
        }
        else {
            progressBar.setVisibility(View.GONE);
            emptyStateView.setText("No Internet Connection");
        }

    }

}
