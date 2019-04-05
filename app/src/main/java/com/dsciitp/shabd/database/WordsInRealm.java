package com.dsciitp.shabd.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WordsInRealm extends RealmObject {

    @PrimaryKey
    private int id;
    private String title;
    private String description;
    private String hindiTitle;
    private String imageResource;
    private String parentClass;
    private int isItTopic;

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

    public int getIsItTopic() {
        return isItTopic;
    }

    public void setIsItTopic(int isItTopic) {
        this.isItTopic = isItTopic;
    }
}


