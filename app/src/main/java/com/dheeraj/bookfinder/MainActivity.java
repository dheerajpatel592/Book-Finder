package com.dheeraj.bookfinder;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager ;
import android.app.LoaderManager.LoaderCallbacks ;
import android.content.Loader ;
import android.content.AsyncTaskLoader ;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {
    private BookAdapter adapter ;

    private TextView emptyStateTextview ;
    private View loadingIndicatorView;

    // Get a reference to the LoaderManager , in order to interact with loaders.
    LoaderManager loaderManager = getLoaderManager();

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1 ;

    private static String RequestUrl ;
    private static final String FIXED_URL = "https://www.googleapis.com/books/v1/volumes?q=" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView bookListView = findViewById(R.id.list);

        loadingIndicatorView = findViewById(R.id.loading_indicator);
        // hide loading indicator so error message will be visible
        loadingIndicatorView.setVisibility(View.GONE);

        emptyStateTextview = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyStateTextview);

        // Get a reference to the ConnectivityManager to check the state of the network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo =  connectivityManager.getActiveNetworkInfo();

        // if there is a network connection , fetch data
        if(networkInfo != null && networkInfo.isConnected()){
            emptyStateTextview.setText(R.string.start_typing);
        }
        else{
            // Otherwise display error
            // Update empty state with no connection error message
            emptyStateTextview.setText(R.string.no_internet_connection);
        }

        adapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(adapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook = (Book) adapter.getItem(position);
                Uri bookUri = Uri.parse(currentBook.getUrl());
                Intent websiteIntent = new Intent (Intent.ACTION_VIEW ,bookUri);
                startActivity(websiteIntent);
            }
        });

        SearchView searchView = findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadingIndicatorView.setVisibility(View.VISIBLE);
                RequestUrl = FIXED_URL + query;
                loaderManager.restartLoader(BOOK_LOADER_ID , null , MainActivity.this);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                emptyStateTextview.setVisibility(View.GONE);
                loadingIndicatorView.setVisibility(View.VISIBLE);

                RequestUrl = FIXED_URL + newText ;
                if(loaderManager.getLoader(BOOK_LOADER_ID) == null){
                    // Initialize the loader .Pass in the int ID constant defined above and pass in null for
                    // the bundle . Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    loaderManager.initLoader(BOOK_LOADER_ID , null , MainActivity.this);
                }
                else
                    loaderManager.restartLoader(BOOK_LOADER_ID , null , MainActivity.this);
                return false;
            }

        });

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i , Bundle bundle){
        //Create a new loader for the given URL
        return new BookLoader(this, RequestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader , List<Book> books) {
        View loadingIndicatorView = findViewById(R.id.loading_indicator);
        loadingIndicatorView.setVisibility(View.GONE);
        emptyStateTextview.setVisibility(View.VISIBLE);

        if(RequestUrl.equals(FIXED_URL))
            emptyStateTextview.setText(R.string.start_typing);
        else {
            // Set empty state text to display "No books found"
            emptyStateTextview.setText(R.string.no_books_found);
        }
        // Clear the adapter of previous earthquake data
        adapter.clear();

        // if there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update
        if(books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader){
        // Loader reset , so we can clear out our existing data
        adapter.clear();
    }

}