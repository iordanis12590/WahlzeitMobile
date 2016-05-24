package com.wahlzeit.mobile.fragments.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahlzeit.mobile.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by iordanis on 14/03/16.
 */
public class PhotoListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;

    private List<PhotoListItem> photoListItems;

    public PhotoListAdapter(Activity activity, List<PhotoListItem> photoListItems) {
        this.activity = activity;
        this.photoListItems = photoListItems;
    }

    public void addPhotoListItem(PhotoListItem photoListItem) {
        this.getPhotoListItems().add(photoListItem);
    }

    public boolean containsPhotoListItem(String id) {
        for(PhotoListItem item: photoListItems) {
            if(item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public List<PhotoListItem> getPhotoListItems() {
        return photoListItems;
    }

    @Override
    public int getCount() {
        return photoListItems.size();
    }

    @Override
    public PhotoListItem getItem(int location) {
        return photoListItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ListItemViewHolder {
        // values
        @InjectView(R.id.textview_photo_praise) TextView textViewPraise;
        @InjectView(R.id.textview_status) TextView textViewStatus;
        @InjectView(R.id.textview_upload_date) TextView textViewUploadDate;
        @InjectView(R.id.textview_photo_tags) TextView textViewPhotoTags;
        @InjectView(R.id.imageview_photo_home) ImageView imageView;
        public ListItemViewHolder(View view) {ButterKnife.inject(this, view);}
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.fragment_home_photo_list_item, null);

        ListItemViewHolder holder = new ListItemViewHolder(convertView);
        PhotoListItem item = photoListItems.get(position);

        holder.textViewPraise.setText(item.getPraise());
        holder.textViewStatus.setText(item.getStatus());
        holder.textViewUploadDate.setText(item.getUploadDate());
        holder.textViewPhotoTags.setText(item.getPhotoTags());
        holder.imageView.setImageBitmap(item.getImage());

        return convertView;
    }
}