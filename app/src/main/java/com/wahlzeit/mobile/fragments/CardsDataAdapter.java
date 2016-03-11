package com.wahlzeit.mobile.fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahlzeit.mobile.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by iordanis on 10/03/16.
 */
public class CardsDataAdapter extends ArrayAdapter<CardModel> {
    public CardsDataAdapter(Context context) { super(context, R.layout.card_content);}

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){
        ViewHolder holder = new ViewHolder(contentView);
        holder.titleText.setText(getItem(position).getmTitle());
        holder.imageView.setImageBitmap(getItem(position).getmPhotoImage());
        return contentView;
    }

    // Class that holds the views of each card_layout
    static class ViewHolder {
        @InjectView(R.id.textView_title) TextView titleText;
        @InjectView(R.id.imageView_photo) ImageView imageView;
        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
