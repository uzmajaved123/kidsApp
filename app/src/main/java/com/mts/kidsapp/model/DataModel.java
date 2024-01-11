package com.mts.kidsapp.model;

public class DataModel {

    private String poem_id;
    private String poem_title;
    private String poem_video;
    private String poem_icon;
    private String poem_category;

    public DataModel(String poem_id, String poem_title,
                     String poem_video, String poem_icon,
                     String poem_category) {
        this.poem_id = poem_id;
        this.poem_title = poem_title;
        this.poem_video = poem_video;
        this.poem_icon = poem_icon;
        this.poem_category = poem_category;
    }

    public String getPoem_category() {
        return poem_category;
    }

    public void setPoem_category(String poem_category) {
        this.poem_category = poem_category;
    }

    public String getPoem_icon() {
        return poem_icon;
    }

    public void setPoem_icon(String poem_icon) {
        this.poem_icon = poem_icon;
    }

    public String getpoem_title() {
        return poem_title;
    }

    public String getpoem_video() {
        return poem_video;
    }

    public String getPoem_id() {
        return poem_id;
    }

    public void setpoem_title(String poem_title) {
        this.poem_title = poem_title;
    }

    public void setpoem_video(String poem_video) {
        this.poem_video = poem_video;
    }

    public void setPoem_id(String id) {
        this.poem_id = id;
    }
}
