package com.project.opticproject;

/**
 * Created by Luke on 31/08/2016.
 */
public class ArticleData {
    private String author;
    private String datePublished;

    public ArticleData(String author, String datePublished) {
        this.author = author;
        this.datePublished = datePublished;
    }

    public String getAuthor() {
        return author;
    }

    public String getDatePublished() {
        return datePublished;
    }
}
