package edu.marda.cs310news;

public class CommentModel {
    private String name;
    private String text;
    private int id;
    private int news_id;

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public int getNews_id() {
        return news_id;
    }

    public CommentModel(String name, String text, int id, int news_id) {
        this.name = name;
        this.text = text;
        this.id = id;
        this.news_id = news_id;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", id=" + id +
                ", news_id=" + news_id +
                '}';
    }
}
