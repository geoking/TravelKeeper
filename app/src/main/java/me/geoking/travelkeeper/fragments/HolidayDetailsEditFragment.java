package me.geoking.travelkeeper.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.model.Holiday;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HolidayDetailsEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HolidayDetailsEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HolidayDetailsEditFragment extends Fragment {

    private static final String HOLIDAY = "Holiday";

    private Holiday holiday;

    private OnFragmentInteractionListener mListener;

    public HolidayDetailsEditFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HolidayDetailsEditFragment newInstance() {
        HolidayDetailsEditFragment fragment = new HolidayDetailsEditFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            holiday = (Holiday) getArguments().getSerializable(HOLIDAY);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.holiday_details_confirm, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {

            String title = holiday.getTitle();
            getActivity().setTitle(title);
            EditText edit = getActivity().findViewById(R.id.holiday_details_title);
            edit.setText(title);
        }
        else {
            String title = getActivity().getResources().getString(R.string.title_holiday_details_add);
            getActivity().setTitle(title);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_holiday_details_edit, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
