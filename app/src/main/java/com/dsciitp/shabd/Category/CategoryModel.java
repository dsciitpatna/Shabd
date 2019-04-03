package com.dsciitp.shabd.Category;

public class CategoryModel {

    private String title;
    private String imageUrl = "-1";

    public CategoryModel() {

    }

    public CategoryModel(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}


