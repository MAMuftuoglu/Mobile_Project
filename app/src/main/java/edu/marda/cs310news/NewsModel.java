package edu.marda.cs310news;

public class NewsModel {
    private int id;
    private String title;
    private String text;
    private String date;
    private String categoryName;
    private String img;

    public NewsModel(int id, String title, String text, String date, String categoryName, String img) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
        this.categoryName = categoryName;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getImg() {
        return img;
    }
}
