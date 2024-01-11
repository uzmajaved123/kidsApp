package com.mts.kidsapp.model;


public class CategoryModel {

    private String poem_id;
    private String poem_title;


    public CategoryModel(String poem_id, String poem_title) {
        this.poem_id = poem_id;
        this.poem_title = poem_title;
    }


    public String getpoem_title() {
        return poem_title;
    }

    public String getPoem_id() {
        return poem_id;
    }

    public void setpoem_title(String poem_title) {
        this.poem_title = poem_title;
    }

    public void setPoem_id(String id) {
        this.poem_id = id;
    }
}

