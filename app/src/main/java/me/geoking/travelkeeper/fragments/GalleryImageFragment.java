package me.geoking.travelkeeper.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.geoking.travelkeeper.MainActivity;
import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.model.Holiday;

public class GalleryImageFragment extends Fragment {
    private static final String HOLIDAY = "Holiday";

    private Bitmap bitmap;

    private OnFragmentInteractionListener mListener;

    public GalleryImageFragment() {
        // Required empty public constructor
    }

    public static GalleryImageFragment newInstance(String param1) {
        GalleryImageFragment fragment = new GalleryImageFragment();
        Bundle args = new Bundle();
        args.putString(HOLIDAY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            byte[] byteArray = getArguments().getByteArray("Image");
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = "Photo";
        getActivity().setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_image, container, false);

        ImageView holidayImage = view.findViewById(R.id.gallery_image_zoom);
        if (bitmap != null) {
            holidayImage.setImageBitmap(bitmap);
        }
        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share, menu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
