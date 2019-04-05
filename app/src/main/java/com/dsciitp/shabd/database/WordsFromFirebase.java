package com.dsciitp.shabd.database;

public class WordsFromFirebase {

    private int id;
    private String title;
    private String description;
    private String hindiTitle;
    private String imageResource;
    private String parentClass;
    private int isItTopic;

    public WordsFromFirebase(){}

    public WordsFromFirebase(String title, String description, String hindiTitle, String imageResource, String parentClass, int isItTopic){
        this.title = title;
        this.description = description;
        this.hindiTitle = hindiTitle;
        this.imageResource = imageResource;
        this.parentClass = parentClass;
        this.isItTopic = isItTopic;
    }

    public String getDescription() {
        return description;
    }

    public String getHindiTitle() {
        return hindiTitle;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHindiTitle(String hindiTitle) {
        this.hindiTitle = hindiTitle;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsItTopic(int isItTopic) {
        this.isItTopic = isItTopic;
    }

    public int getIsItTopic() {
        return isItTopic;
    }
}


