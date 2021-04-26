package com.example.myapplication3;

public class FeaturedHelperClass {
    int image;
    String title;
    public FeaturedHelperClass(int image,String title){
        this.image=image;
        this.title=title;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
