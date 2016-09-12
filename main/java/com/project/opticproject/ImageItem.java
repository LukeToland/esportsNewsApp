package com.project.opticproject;

/**
 * Created by Luke on 02/09/2016.
 */
public class ImageItem {
    private String url;
    private String team;
    private String article;

    public ImageItem(String url, String team, String a) {
        this.url = url;
        this.team = team;
        this.article = a;
    }

    public String getUrl() {
        return url;
    }

    public String getTeam() {
        return team;
    }

    public String getArticle() {
        return article;
    }
}
