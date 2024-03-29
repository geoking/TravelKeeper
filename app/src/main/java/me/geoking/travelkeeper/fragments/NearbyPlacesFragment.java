package me.geoking.travelkeeper.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.widget.Button;

import java.util.List;

import me.geoking.travelkeeper.R;

import static android.content.Context.LOCATION_SERVICE;
import static me.geoking.travelkeeper.R.id.places_parks;
import static me.geoking.travelkeeper.R.id.places_shopping;
import static me.geoking.travelkeeper.R.id.places_to_eat;
import static me.geoking.travelkeeper.R.id.places_tourism;

public class NearbyPlacesFragment extends Fragment implements View.OnClickListener {

    MapView mMapView;
    private GoogleMap googleMap;
    LocationManager mLocationManager;
    Location myLocation;
    private Intent browserIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby_places, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        Button restuarantButton = (Button) view.findViewById(R.id.places_to_eat);
        restuarantButton.setOnClickListener(this);
        Button parksButton = (Button) view.findViewById(R.id.places_parks);
        parksButton.setOnClickListener(this);
        Button shoppingButton = (Button) view.findViewById(R.id.places_shopping);
        shoppingButton.setOnClickListener(this);
        Button tourismButton = (Button) view.findViewById(R.id.places_tourism);
        tourismButton.setOnClickListener(this);

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
                    LatLng london = new LatLng(52.486636, -1.891116);
                    googleMap.addMarker(new MarkerOptions().position(london).title("Marker Title").snippet("Marker Description"));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(london).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } else {
                    mMap.setMyLocationEnabled(true);
                    LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                    myLocation = getLastKnownLocation();
                    Location location = myLocation;
                    CameraPosition cameraPosition;
                    if (myLocation != null) {
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        LatLng locationLatLng = new LatLng(latitude, longitude);
                        cameraPosition = new CameraPosition.Builder().target(locationLatLng).zoom(15).build();
                    }
                    else {
                        LatLng london = new LatLng(52.486636, -1.891116);
                        cameraPosition = new CameraPosition.Builder().target(london).zoom(15).build();
                    }
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

                // For dropping a marker at a point on the Map

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        String title = getActivity().getResources().getString(R.string.button_nearby);
        getActivity().setTitle(title);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = null;
        if (mLocationManager != null) {
            providers = mLocationManager.getProviders(true);
        }
        Location bestLocation = null;
        if (providers != null) {
            for (String provider : providers) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return bestLocation;
                }
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
        }
        return bestLocation;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case places_to_eat:
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=restaurants"));
                startActivity(browserIntent);
                break;
            case places_parks:
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=parks"));
                startActivity(browserIntent);
                break;
            case places_shopping:
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=shops"));
                startActivity(browserIntent);
                break;
            case places_tourism:
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=tourist+attractions"));
                startActivity(browserIntent);
                break;
        }
    }
}