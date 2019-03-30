package com.dsciitp.shabd.Home;

public class TopicModel {

    private String title;
    private String description;
    private String imageUrl = "-1";
    private int backgroundId;

    public TopicModel(){

    }

    public TopicModel(String title, int backgroundId){
        this.title = title;
        this.backgroundId = backgroundId;
    }

    public TopicModel(String title, String imageUrl){
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public TopicModel(String title, String imageUrl, String description) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }

    public boolean hasImage() {
        return !(imageUrl.equals("-1"));
    }
}
