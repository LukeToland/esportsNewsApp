package com.project.opticproject;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by Luke on 29/08/2016.
 */
public class NewsItem {
    private String newsHeading;
    private String link;
    private String description;
    private String img;
    private String author;
    private String datePublished;

    public NewsItem(String link, String newsHeading, String image) {
        this.link = link;
        this.newsHeading = newsHeading;
        this.img = image;
    }

//    public NewsItem(String link, String newsHeading, String description) {
//        this.link = link;
//        this.newsHeading = newsHeading;
//        this.description = description;
//    }


    public NewsItem(String newsHeading, String datePublished, String author, String link, String img) {
        this.newsHeading = newsHeading;
        this.datePublished = datePublished;
        this.author = author;
        this.link = link;
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public String getAuthor() {
        return author;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public String getNewsHeading() {
        return newsHeading;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return img;
    }

    public void setNewsHeading(String newsHeading) {
        this.newsHeading = newsHeading;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return  newsHeading;
    }


}