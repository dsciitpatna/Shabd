package com.dsciitp.shabd.Home;

public class GetTitle {

    private String title;
    private String description;

    public GetTitle(){

    }

    public GetTitle(String title, String description) {
        this.title = title;
        this.description = description;
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
}
