package com.wahlzeit.mobile.fragments.show;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wahlzeit.mobile.R;
import com.wenchao.cardstack.CardStack;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by iordanis on 10/03/16.
 */
public class CardsDataAdapter extends ArrayAdapter<CardModel> {
    CardStack myCardStack;

    public CardsDataAdapter(Context context, CardStack cardStack) {
        super(context, R.layout.card_content);
        this.myCardStack = cardStack;
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){
        CardViewHolder holder = new CardViewHolder(contentView);
        holder.titleText.setText(getItem(position).getmTitle());
        holder.imageView.setImageBitmap(getItem(position).getmPhotoImage());
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBarListener(this.getContext(), getItem(position).getPhotoId(), myCardStack));
        return contentView;
    }

    // Class that holds the views of each card_layout
    static class CardViewHolder {
        @InjectView(R.id.textview_title_card) TextView titleText;
        @InjectView(R.id.imageview_photo) ImageView imageView;
        @InjectView(R.id.ratingbar) RatingBar ratingBar;

        public CardViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
