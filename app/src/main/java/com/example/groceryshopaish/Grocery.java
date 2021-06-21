package com.example.groceryshopaish;

public class Grocery
{
    String gid;
    String imageUri;
    String gname,measure;
    int price,stock;

    public Grocery() {
    }

    public Grocery(String gid, String imageUri, String gname, String measure, int price, int stock) {
        this.gid = gid;
        this.imageUri = imageUri;
        this.gname = gname;
        this.measure = measure;
        this.price = price;
        this.stock = stock;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
