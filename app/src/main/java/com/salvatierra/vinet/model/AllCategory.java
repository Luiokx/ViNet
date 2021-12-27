package com.salvatierra.vinet.model;

import java.util.List;

public class AllCategory {
    private String CategoryTitle;
    private Integer CategoryId;
    private List<CategoryItem> categoryItemList;

    public AllCategory(){
    }

    public AllCategory(Integer CategoryId,String CategoryTitle){
        this.CategoryId = CategoryId;
        this.CategoryTitle = CategoryTitle;
    }

    public AllCategory(Integer CategoryId,String CategoryTitle, List<CategoryItem> categoryItemList){
        this.CategoryId = CategoryId;
        this.CategoryTitle = CategoryTitle;
        this.categoryItemList = categoryItemList;
    }

    public void setCategoryItemList(List<CategoryItem> categoryItemList){
        this.categoryItemList = categoryItemList;
    }

    public List<CategoryItem> getCategoryItemList() {
        return this.categoryItemList;
    }

    public void setCategoryTitle(String CategoryTitle){
        this.CategoryTitle = CategoryTitle;
    }

    public void setCategoryId(Integer CategoryId){
        this.CategoryId = CategoryId;
    }

    public String getCategoryTitle(){
        return this.CategoryTitle;
    }

    public Integer getCategoryId() {
        return this.CategoryId;
    }
}
