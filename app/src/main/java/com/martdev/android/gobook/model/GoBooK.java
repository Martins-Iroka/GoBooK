package com.martdev.android.gobook.model;

public class GoBooK {
    private String mBookTitle;
    private String mBookAuthor;
    private String mDate;
    private String mUrl;
    private String mImageUri;

    public GoBooK(String bookTitle, String bookAuthor, String date, String url, String imageUri) {
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
        mDate = date;
        mUrl = url;
        mImageUri = imageUri;
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public String getBookAuthor() {
        return mBookAuthor;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getImageUri() {
        return mImageUri;
    }
}
