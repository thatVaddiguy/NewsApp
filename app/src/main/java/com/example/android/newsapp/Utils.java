package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BOX on 8/22/2016.
 */
public final class Utils {

    public static final String LOG_TAG = Utils.class.getName();

    public Utils() {
    }

    private static URL creatUrl(String stringurl){

        URL url = null;
        try {
            url= new URL(stringurl);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Problem Building url");
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url==null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode()==200){
                inputStream=urlConnection.getInputStream();
                jsonResponse= readFromStream(inputStream);
            }
            else
            {Log.e(LOG_TAG,"Error Response Code"+urlConnection.getResponseCode());}

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            if (inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<News> extractFromJson(String newsJson){

        if (TextUtils.isEmpty(newsJson)){
            return null;
        }

        List<News> news = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJson);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = response.getJSONArray("results");
            for (int i=0;i<newsArray.length();i++){
                JSONObject currentNews = newsArray.getJSONObject(i);
                String author = "Author N/A";
                JSONArray authors = currentNews.getJSONArray("tags");
                if (authors.length()>0){
                    author = authors.getJSONObject(0).getString("webTitle");
                }
                String title = currentNews.getString("webTitle");
                String category = currentNews.getString("sectionName");
                String url = currentNews.getString("webUrl");
                News newsObject = new News(title, category, url,author);
                news.add(newsObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return news;
    }

    public static List<News> fetchData(String requestUrl){
        URL url = creatUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem making the HTTP request");
        }

        List<News> news = extractFromJson(jsonResponse);
        return news;
    }

}
