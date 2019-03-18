package com.dsciitp.shabd.home;

public class GetTitle {
    private String topic_title;
    private String topic_desc;

    public GetTitle(){

    }

    public GetTitle(String topic_title, String topic_desc) {
        this.topic_title = topic_title;
        this.topic_desc = topic_desc;
    }

    public String getTitle(){
        return topic_title;
    }

    public String getDescription(){
        return topic_desc;
    }
}
