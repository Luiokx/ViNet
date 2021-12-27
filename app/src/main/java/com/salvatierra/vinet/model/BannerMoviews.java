package com.salvatierra.vinet.model;

public class BannerMoviews {
    private Integer id;
    private String movieName, imageUrl, fileUrl;

    public BannerMoviews(){
    }

    public BannerMoviews(Integer id, String movieName, String imageUrl, String fileUrl){
        this.id = id;
        this.movieName = movieName;
        this.imageUrl = imageUrl;
        this.fileUrl = fileUrl;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public void setMovieName(String movieName){
        this.movieName = movieName;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void setFileUrl(String fileUrl){
        this.imageUrl = imageUrl;
    }

    public Integer getId(){
        return this.id;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public String getMovieName(){
        return this.movieName;
    }

    public String getFileUrl(){
        return this.fileUrl;
    }

}
