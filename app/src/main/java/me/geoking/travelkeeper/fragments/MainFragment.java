package me.geoking.travelkeeper.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.geoking.travelkeeper.MainActivity;
import me.geoking.travelkeeper.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = getActivity().getResources().getString(R.string.app_name);
        getActivity().setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button holidayButton = (Button) view.findViewById(R.id.button_holidays);
        Button visitedButton = (Button) view.findViewById(R.id.button_visited);
        Button galleryButton = (Button) view.findViewById(R.id.button_gallery);
        Button nearbyButton = (Button) view.findViewById(R.id.button_nearby);
        holidayButton.setOnClickListener(this);
        visitedButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
        nearbyButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        Menu menuNav = navigationView.getMenu();
        MenuItem item;
        switch (view.getId()) {
            case R.id.button_holidays:
                item = menuNav.findItem(R.id.nav_holidays);
                ((MainActivity)getActivity()).onNavigationItemSelected(item);
                break;
            case R.id.button_visited:
                item = menuNav.findItem(R.id.nav_visited);
                ((MainActivity)getActivity()).onNavigationItemSelected(item);
                break;
            case R.id.button_gallery:
                item = menuNav.findItem(R.id.nav_gallery);
                ((MainActivity)getActivity()).onNavigationItemSelected(item);
                break;
            case R.id.button_nearby:
                item = menuNav.findItem(R.id.nav_nearby);
                ((MainActivity)getActivity()).onNavigationItemSelected(item);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
