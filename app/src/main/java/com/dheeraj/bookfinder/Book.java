package com.dheeraj.bookfinder;

public class Book {
    private String authorName ;
    private String bookName ;
    private String imageUrl ;
    private String url ;

    public Book(String bookName, String authorName, String imageUrl, String url) {
        this.authorName = authorName;
        this.bookName = bookName;
        this.imageUrl = imageUrl;
        this.url = url;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getBookName() {
        return bookName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUrl() {
        return url;
    }
}
