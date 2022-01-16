package com.dheeraj.bookfinder;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;
public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private static  final String LOG_TAG = BookLoader.class.getName();

    // Query url
    private String url ;

    public BookLoader (Context context , String url ){
        super(context);
        this.url = url ;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if(url == null )
            return null ;
        List<Book> result = QueryUtils.fetchBookData(url);
        return result;
    }

}
