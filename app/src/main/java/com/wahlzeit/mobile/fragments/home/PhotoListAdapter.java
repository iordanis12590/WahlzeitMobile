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
//    private Context mContext;
    private LayoutInflater inflater;
    private List<PhotoListItem> photoListItems;

    public PhotoListAdapter(Activity activity, List<PhotoListItem> photoListItems) {
        this.activity = activity;
        this.photoListItems = photoListItems;
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
        @InjectView(R.id.textview_photo_praise) TextView textViewPraise;
        @InjectView(R.id.textview_status) TextView textViewStatus;
        @InjectView(R.id.textview_upload_date) TextView textViewUploadDate;
        @InjectView(R.id.textview_photo_tags) TextView textViewPhotoTags;
        @InjectView(R.id.textview_photo_link) TextView textViewPhotoLink;
        @InjectView(R.id.imageview_photo_home) ImageView imageView;
        public ListItemViewHolder(View view) {ButterKnife.inject(this, view);}
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.photo_list_item, null);

        ListItemViewHolder holder = new ListItemViewHolder(convertView);
        PhotoListItem item = photoListItems.get(position);

        holder.textViewPraise.setText(item.getPraise());
        holder.textViewStatus.setText(item.getStatus());
        holder.textViewUploadDate.setText(item.getUploadDate());
        holder.textViewPhotoTags.setText(item.getPhotoTags());
        holder.textViewPhotoLink.setText(item.getPhotoLink());
        holder.imageView.setImageBitmap(item.getImage());

        return convertView;

//        if (inflater == null)
//            inflater = (LayoutInflater) activity
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if (convertView == null)
//            convertView = inflater.inflate(R.layout.photo_list_item, null);
//
//
//        return convertView;

//        if (imageLoader == null)
//            imageLoader = new ImageLoader(Volley.newRequestQueue(new MainActivity().getApplicationContext()), null);
//
//        TextView name = (TextView) convertView.findViewById(R.id.name);
//        TextView timestamp = (TextView) convertView
//                .findViewById(R.id.timestamp);
//        TextView statusMsg = (TextView) convertView
//                .findViewById(R.id.txtStatusMsg);
//        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
//        NetworkImageView profilePic = (NetworkImageView) convertView
//                .findViewById(R.id.profilePic);
//        FeedImageView feedImageView = (FeedImageView) convertView
//                .findViewById(R.id.feedImage1);
//
//        PhotoListItem item = photoListItems.get(position);
//
//        name.setText(item.getName());
//
//        // Converting timestamp into x ago format
//        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
//                Long.parseLong(item.getTimeStamp()),
//                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
//        timestamp.setText(timeAgo);
//
//        // Chcek for empty status message
//        if (!TextUtils.isEmpty(item.getStatus())) {
//            statusMsg.setText(item.getStatus());
//            statusMsg.setVisibility(View.VISIBLE);
//        } else {
//            // status is empty, remove from view
//            statusMsg.setVisibility(View.GONE);
//        }
//
//        // Checking for null feed url
//        if (item.getUrl() != null) {
//            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
//                    + item.getUrl() + "</a> "));
//
//            // Making url clickable
//            url.setMovementMethod(LinkMovementMethod.getInstance());
//            url.setVisibility(View.VISIBLE);
//        } else {
//            // url is null, remove from the view
//            url.setVisibility(View.GONE);
//        }
//
//        // user profile pic
//        profilePic.setImageUrl(item.getProfilePic(), imageLoader);
//
//        // Feed image
//        if (item.getImge() != null) {
//            feedImageView.setImageUrl(item.getImge(), imageLoader);
//            feedImageView.setVisibility(View.VISIBLE);
//            feedImageView
//                    .setResponseObserver(new FeedImageView.ResponseObserver() {
//                        @Override
//                        public void onError() {
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                        }
//                    });
//        } else {
//            feedImageView.setVisibility(View.GONE);
//        }
//
//        return convertView;
    }

}