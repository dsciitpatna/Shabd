package com.dsciitp.shabd.Learn;

public class LearnStoryModel {

    private String title;
    private String description;
    private String hindiTitle;
    private String imageResource;
    private String intent;

    public LearnStoryModel() {
    }

    public LearnStoryModel(String title, String description, String hindiTitle, String imageResource, String intent) {
        this.title = title;
        this.description = description;
        this.hindiTitle = hindiTitle;
        this.imageResource = imageResource;
        this.intent = intent;
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

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }
}
