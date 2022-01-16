package com.dheeraj.bookfinder;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BookAdapter extends ArrayAdapter {
    private static final String LOCATION_SEPARATOR = " of ";
    DecimalFormat formatter = new DecimalFormat("0.0");

    public BookAdapter(Context context , List<Book> books){
        super(context , 0 , books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView ;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item , parent , false);

        Book currentBook = (Book) getItem(position);

        TextView bookNameView = listItemView.findViewById(R.id.book_name);
        String bookName = currentBook.getBookName();
        bookNameView.setText(bookName);

        TextView authorNameView = listItemView.findViewById(R.id.author_name);
        String authorName = currentBook.getAuthorName();
        authorNameView.setText(authorName);

        ImageView imageView = listItemView.findViewById(R.id.image);
        String imageUrl = currentBook.getImageUrl();
        // In androidmanifest , type in (usescleartexttraffic = true ) else images will not be displayed
        Picasso.get().load(imageUrl).into(imageView);


        return listItemView ;
    }

}

