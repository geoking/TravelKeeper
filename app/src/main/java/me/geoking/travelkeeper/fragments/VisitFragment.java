package me.geoking.travelkeeper.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;

import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.model.AppDatabase;
import me.geoking.travelkeeper.model.Visit;

import static android.content.ContentValues.TAG;

public class VisitFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private OnFragmentInteractionListener mListener;
    MapView mMapView;
    private GoogleMap googleMap;
    private int mColumnCount = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;

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
            final ArrayList visits = (ArrayList) AppDatabase.getInstance().getVisitDao().getVisits();
            Collections.reverse(visits);

            mMapView = (MapView) view.findViewById(R.id.visited_map);
            mMapView.onCreate(savedInstanceState);

            mMapView.onResume(); // needed to get the map to display immediately

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                    .addConnectionCallbacks(this)
                    .build();

            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;

                    buildMapMarkers(visits);

                    LatLng london = new LatLng(51.5006715, -0.1247405);

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(london).zoom(0).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    // For dropping a marker at a point on the Map

                }
            });

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.visited_list);
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

    public void buildMapMarkers(ArrayList visits) {
        int i = 0;
        while (i < visits.size()) {
            Visit visit = (Visit)visits.get(i);
            String id = visit.getPlaceid();
            if (id != null) {
                Places.GeoDataApi.getPlaceById(mGoogleApiClient, id).setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        if(places.getStatus().isSuccess() && places.getCount()>0){
                            final Place myPlace = places.get(0);
                            googleMap.addMarker(new MarkerOptions().position(myPlace.getLatLng()).title(myPlace.getName().toString()).snippet(myPlace.getAddress().toString()));
                        }
                        places.release();
                    }
                });
            }
            i++;

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Visit visit);
    }
}
