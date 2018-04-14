package me.geoking.travelkeeper.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.geoking.travelkeeper.MainActivity;
import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.model.Visit;

public class VisitDetailsFragment extends Fragment {
    private static final String VISIT = "Visit";

    private Visit visit;

    private OnFragmentInteractionListener mListener;

    public VisitDetailsFragment() {
        // Required empty public constructor
    }

    public static VisitDetailsFragment newInstance(String param1) {
        VisitDetailsFragment fragment = new VisitDetailsFragment();
        Bundle args = new Bundle();
        args.putString(VISIT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment currentFragment;
        Bundle args = new Bundle();
        switch (item.getItemId()) {
            case R.id.visitEdit:
                currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
                visit = (Visit) currentFragment.getArguments().getSerializable("Visit");
                VisitDetailsEditFragment newFragment = new VisitDetailsEditFragment();
                args.putSerializable("Visit", visit);
                newFragment.setArguments(args);
                FragmentTransaction editTransaction =
                        getFragmentManager().beginTransaction();
                editTransaction.replace(R.id.fragment_container, newFragment);
                editTransaction.addToBackStack(null);
                editTransaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            visit = (Visit) getArguments().getSerializable(VISIT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = visit.getTitle();
        getActivity().setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visit_details, container, false);

        TextView titleField = view.findViewById(R.id.visit_view_details_title);
        TextView addressField = view.findViewById(R.id.visit_view_details_address);
        TextView tagsField = view.findViewById(R.id.visit_view_details_tags);
        TextView startButton = view.findViewById(R.id.visit_view_details_date);
        TextView notesField = view.findViewById(R.id.visit_view_details_notes);
        ImageView visitImage = view.findViewById(R.id.visit_view_details_image);
        titleField.setText(visit.getTitle());
        addressField.setText(visit.getAddress());
        tagsField.setText(visit.getTags());
        startButton.setText(visit.getVisitDate());
        notesField.setText(visit.getNotes());
        notesField.setMovementMethod(new ScrollingMovementMethod());
        if (visit.getImageLocation() != null) {
            visitImage.setImageBitmap(((MainActivity)getActivity()).loadImageFromStorage(visit.getImageLocation(), visit.getImageLocationUUID()));
        }
        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.visit_details_edit, menu);
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
        void onFragmentInteraction(Uri uri);
    }
}
