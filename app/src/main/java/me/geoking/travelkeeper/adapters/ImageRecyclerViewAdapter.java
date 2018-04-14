package me.geoking.travelkeeper.adapters;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.fragments.GalleryFragment;
import me.geoking.travelkeeper.fragments.GalleryFragment.OnFragmentInteractionListener;
import me.geoking.travelkeeper.model.Holiday;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder> {

    private final List<Bitmap> mImages;
    private final OnFragmentInteractionListener mListener;

    public ImageRecyclerViewAdapter(List<Bitmap> images, GalleryFragment.OnFragmentInteractionListener listener) {
        mImages = images;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mImage = mImages.get(position);
        holder.mImageView.setImageBitmap(mImages.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an holiday has been selected.
                    Bitmap image = holder.mImage;
                    //mListener.onFragmentInteraction(image);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Bitmap mImage;
        public ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.gallery_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mImageView.toString() + "'";
        }
    }
}
