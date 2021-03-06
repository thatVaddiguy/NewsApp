package com.example.android.newsapp;

/**
 * Created by BOX on 8/21/2016.
 */
public class News {

    private String title;
    private String category;
    private String url;
    private String author;

    public News(String title,String category, String url,String author) {
        this.category = category;
        this.title = title;
        this.url = url;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
