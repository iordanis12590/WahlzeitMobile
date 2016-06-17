package com.wahlzeit.mobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.model.PhotoCaseListItem;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/** An adapter class that holds all photo cases for the moderate fragment
 * Created by iordanis on 16/04/16.
 */
public class PhotoCaseListAdapter extends BaseAdapter {

    private Activity mContext;
    private LayoutInflater inflater;
    private List<PhotoCaseListItem> photoCaseItemsList;

    public PhotoCaseListAdapter(Activity mContext, List<PhotoCaseListItem> photoCaseItemsList) {
        this.mContext = mContext;
        this.photoCaseItemsList = photoCaseItemsList;
    }

    @Override
    public int getCount() {
        return photoCaseItemsList.size();
    }

    @Override
    public PhotoCaseListItem getItem(int position) {
        return photoCaseItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class PhotoCaseListItemViewHolder {
        @InjectView(R.id.textview_photo_case_description) TextView textViewDescription;
        @InjectView(R.id.textview_photo_case_tags) TextView textViewTags;
        @InjectView(R.id.textview_photo_case_flagger) TextView textViewFlagger;
        @InjectView(R.id.textview_photo_case_reason) TextView textViewReason;
        @InjectView(R.id.textview_photo_case_explanation) TextView textViewExplanation;
        @InjectView(R.id.imageview_photo_case_moderate) ImageView imageView;
        public PhotoCaseListItemViewHolder(View view) {ButterKnife.inject(this,view);}
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) convertView = inflater.inflate(R.layout.fragment_moderate_photo_case_list_item, null);

        PhotoCaseListItemViewHolder holder = new PhotoCaseListItemViewHolder(convertView);
        PhotoCaseListItem item = photoCaseItemsList.get(position);

        holder.imageView.setImageBitmap(item.getImage());
        holder.textViewDescription.setText(item.getDescription());
        holder.textViewTags.setText(item.getTags());
        holder.textViewFlagger.setText(item.getFlagger());
        holder.textViewReason.setText(item.getReason());
        holder.textViewExplanation.setText(item.getExaplanation());
        return convertView;
    }
}
