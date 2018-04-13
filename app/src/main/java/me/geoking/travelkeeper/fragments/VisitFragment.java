package me.geoking.travelkeeper.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.model.AppDatabase;
import me.geoking.travelkeeper.model.Visit;

public class VisitFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    MapView mMapView;
    private GoogleMap googleMap;
    private int mColumnCount = 1;

    public VisitFragment() {
        // Required empty public constructor
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.visitAdd:
                VisitDetailsEditFragment addFragment = new VisitDetailsEditFragment();
                FragmentTransaction addNewTransaction =
                        getFragmentManager().beginTransaction();
                addNewTransaction.replace(R.id.fragment_container, addFragment);
                addNewTransaction.addToBackStack(null);
                addNewTransaction.commit();
                return true;
            default:
                return onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.visited_details_add, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = getActivity().getResources().getString(R.string.title_places_visited);
        getActivity().setTitle(title);
    }

    public static VisitFragment newInstance(String param1, String param2) {
        VisitFragment fragment = new VisitFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (AppDatabase.getInstance().getVisitDao().getVisits().size() == 0) {
            View view = inflater.inflate(R.layout.fragment_visit_novisit, container, false);
            setHasOptionsMenu(true);
            return view;
        }
        else {
            setHasOptionsMenu(true);
            View view = inflater.inflate(R.layout.fragment_visited, container, false);
            ArrayList visits = (ArrayList) AppDatabase.getInstance().getVisitDao().getVisits();

            mMapView = (MapView) view.findViewById(R.id.visited_map);
            mMapView.onCreate(savedInstanceState);

            mMapView.onResume(); // needed to get the map to display immediately

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;


                    if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        LatLng london = new LatLng(51.5006715, -0.1247405);
                        googleMap.addMarker(new MarkerOptions().position(london).title("Marker Title").snippet("Marker Description"));

                        // For zooming automatically to the location of the marker
                        //CameraPosition cameraPosition = new CameraPosition.Builder().target(london).zoom(15).build();
                        //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    } else {
                        mMap.setMyLocationEnabled(true);
                        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        LatLng locationLatLng = new LatLng(latitude, longitude);
                        // For zooming automatically to the location of the marker
                        //CameraPosition cameraPosition = new CameraPosition.Builder().target(locationLatLng).zoom(15).build();
                        //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }

                    // For dropping a marker at a point on the Map

                }
            });

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.holidays_list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyVisitRecyclerViewAdapter(visits, mListener));

            return view;
        }
    }

    public void onButtonPressed() {

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
        void onListFragmentInteraction(Visit visit);
    }
}
