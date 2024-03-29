package me.geoking.travelkeeper.fragments;

import android.content.Context;
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

public class HolidayDetailsFragment extends Fragment {
    private static final String HOLIDAY = "Holiday";

    private Holiday holiday;

    private OnFragmentInteractionListener mListener;

    public HolidayDetailsFragment() {
        // Required empty public constructor
    }

    public static HolidayDetailsFragment newInstance(String param1) {
        HolidayDetailsFragment fragment = new HolidayDetailsFragment();
        Bundle args = new Bundle();
        args.putString(HOLIDAY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            holiday = (Holiday) getArguments().getSerializable(HOLIDAY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = holiday.getTitle();
        getActivity().setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_holiday_details, container, false);

        TextView titleField = view.findViewById(R.id.holiday_view_details_title);
        TextView tagsField = view.findViewById(R.id.holiday_view_details_tags);
        TextView startButton = view.findViewById(R.id.holiday_view_details_start);
        TextView endButton = view.findViewById(R.id.holiday_view_details_end);
        TextView notesField = view.findViewById(R.id.holiday_view_details_notes);
        ImageView holidayImage = view.findViewById(R.id.holiday_view_details_image);
        titleField.setText(holiday.getTitle());
        tagsField.setText(holiday.getTags());
        startButton.setText(holiday.getStartDate());
        endButton.setText(holiday.getEndDate());
        notesField.setText(holiday.getNotes());
        notesField.setMovementMethod(new ScrollingMovementMethod());
        if (holiday.getImageLocation() != null) {
            holidayImage.setImageBitmap(((MainActivity)getActivity()).loadImageFromStorage(holiday.getImageLocation(), holiday.getImageLocationUUID()));
        }
        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.holiday_details_edit, menu);
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
