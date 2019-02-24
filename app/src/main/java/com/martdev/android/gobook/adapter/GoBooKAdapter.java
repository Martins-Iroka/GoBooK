package com.martdev.android.gobook.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.martdev.android.gobook.R;
import com.martdev.android.gobook.model.GoBooK;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GoBooKAdapter extends ArrayAdapter<GoBooK> {

    public GoBooKAdapter(Context context, List<GoBooK> goBooKS) {
        super(context, 0, goBooKS);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_item_list,
                    parent, false);
        }

        GoBooK goBooK = getItem(position);

        CardView cardView = listItemView.findViewById(R.id.cardView);
        cardView.setCardElevation(10f);
        cardView.setRadius(4f);
        cardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));

        ImageView bookImage = listItemView.findViewById(R.id.bookImage);
        Uri uri = Uri.parse(goBooK.getImageUri());
        Picasso.with(getContext()).load(uri).into(bookImage);

        TextView bookTitle = listItemView.findViewById(R.id.book_name);
        bookTitle.setText(goBooK.getBookTitle());
        bookTitle.setTextColor(getContext().getResources().getColor(android.R.color.white));

        TextView bookAuthors = listItemView.findViewById(R.id.book_authors);
        bookAuthors.setText(goBooK.getBookAuthor());
        bookAuthors.setTextColor(getContext().getResources().getColor(android.R.color.white));

        TextView publishedDate = listItemView.findViewById(R.id.published_date);
        publishedDate.setText(goBooK.getDate());

        return listItemView;
    }
}
