package me.geoking.travelkeeper.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.geoking.travelkeeper.MainActivity;
import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.adapters.ImageRecyclerViewAdapter;
import me.geoking.travelkeeper.model.AppDatabase;
import me.geoking.travelkeeper.model.Holiday;
import me.geoking.travelkeeper.model.Visit;

public class GalleryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private int mColumnCount = 3;

    private OnFragmentInteractionListener mListener;

    public GalleryFragment() {
        // Required empty public constructor
    }

    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = getActivity().getResources().getString(R.string.title_gallery);
        getActivity().setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList holidays = (ArrayList) AppDatabase.getInstance().getHolidayDao().getHolidays();
        ArrayList visits = (ArrayList) AppDatabase.getInstance().getVisitDao().getVisits();
        View view;

        ArrayList<Bitmap> images = new ArrayList<Bitmap>();
        if (holidays.size() > 0) {
            int i = 0;
            while (i < holidays.size()) {
                Holiday holiday = (Holiday) holidays.get(i);
                String location = holiday.getImageLocation();
                String uuid = holiday.getImageLocationUUID();
                if (location != null || uuid != null) {
                    Bitmap b = ((MainActivity) getActivity()).loadImageFromStorage(location, uuid);
                    images.add(b);
                }
                i++;
            }
        }

        if (visits.size() > 0) {
            int i = 0;
            while (i < visits.size()) {
                Visit visit = (Visit) visits.get(i);
                String location = visit.getImageLocation();
                String uuid = visit.getImageLocationUUID();
                if (location != null || uuid != null) {
                    Bitmap b = ((MainActivity) getActivity()).loadImageFromStorage(location, uuid);
                    images.add(b);
                }
                i++;
            }
        }

        if (!(images.isEmpty())) {
            view = inflater.inflate(R.layout.fragment_gallery, container, false);
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.image_list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new ImageRecyclerViewAdapter(images, mListener));
        }
        else {
            view = inflater.inflate(R.layout.fragment_gallery_noimages, container, false);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Bitmap bitmap);
    }
}
