package com.dsciitp.shabd.home;

public class TopicModel {

    private String title;
    private String description;
    private String imageUrl = "-1";
    private int backgroundId;
    private String returnText;

    public TopicModel(){

    }

    public TopicModel(String title, int backgroundId, String returnText){
        this.title = title;
        this.backgroundId = backgroundId;
        this.returnText = returnText;
    }

    public TopicModel(String title, String imageUrl, String returnText){
        this.title = title;
        this.imageUrl = imageUrl;
        this.returnText = returnText;
    }

    public TopicModel(String title, String imageUrl, String description, String returnText) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.returnText = returnText;
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

    public String getReturnText() {
        return returnText;
    }

    public void setReturnText(String returnText) {
        this.returnText = returnText;
    }
}
