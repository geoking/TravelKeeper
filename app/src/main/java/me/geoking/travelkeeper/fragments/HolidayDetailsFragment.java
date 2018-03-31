package me.geoking.travelkeeper.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.model.Holiday;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HolidayDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HolidayDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HolidayDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Item";

    // TODO: Rename and change types of parameters
    private Holiday item;

    private OnFragmentInteractionListener mListener;

    public HolidayDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HolidayDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HolidayDetailsFragment newInstance(String param1, String param2) {
        HolidayDetailsFragment fragment = new HolidayDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = (Holiday) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = getActivity().getResources().getString(R.string.title_holiday_details);
        getActivity().setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_holiday_details, container, false);

        TextView titleField = view.findViewById(R.id.title_holiday_details);
        titleField.setText(item.getDescription());
        TextView notesField = view.findViewById(R.id.holiday_notes);
        notesField.setText(item.getNotes());
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
