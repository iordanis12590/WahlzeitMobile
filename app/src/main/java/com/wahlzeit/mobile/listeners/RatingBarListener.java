package com.wahlzeit.mobile.listeners;

import android.content.Context;
import android.util.Log;
import android.widget.RatingBar;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.model.ModelManager;
import com.wahlzeit.mobile.network.asyncTasks.RatePhotoTask;
import com.wenchao.cardstack.CardStack;

/**
 * Created by iordanis on 07/04/16.
 */
public class RatingBarListener implements RatingBar.OnRatingBarChangeListener {

    String ratedPhotoId;
    CardStack cardStack;
    Context myContext;

    public RatingBarListener(Context context, String ratedPhotoId, CardStack cardStack) {
        this.ratedPhotoId = ratedPhotoId;
        this.cardStack = cardStack;
        this.myContext = context;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        Log.d("Rating", Float.toString(rating));
        Log.d("Rated photo id", ratedPhotoId);

        Photo photoToRate = ModelManager.manager.getPhotoFromId(ratedPhotoId);
        photoToRate.setPraisingClientId(ModelManager.manager.getCurrentClient().getId());
        ModelManager.manager.setPraisedPhoto(ratedPhotoId);
        photoToRate.setRating((int) rating);
        new RatePhotoTask(myContext).execute(photoToRate);

        cardStack.discardTop(2);
    }
}