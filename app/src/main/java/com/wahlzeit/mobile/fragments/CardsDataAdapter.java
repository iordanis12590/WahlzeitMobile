package com.wahlzeit.mobile.fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahlzeit.mobile.R;

/**
 * Created by iordanis on 10/03/16.
 */
public class CardsDataAdapter extends ArrayAdapter<CardModel> {
    public CardsDataAdapter(Context context) { super(context, R.layout.card_content);}

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){
        TextView titleText = (TextView)(contentView.findViewById(R.id.textView_title));
        titleText.setText(getItem(position).getmTitle());
        ImageView imageView = (ImageView) contentView.findViewById(R.id.imageView_photo);
        imageView.setImageBitmap(getItem(position).getmPhotoImage());
        return contentView;
    }
}
