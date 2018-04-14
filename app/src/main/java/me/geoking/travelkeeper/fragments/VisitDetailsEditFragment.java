package me.geoking.travelkeeper.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import me.geoking.travelkeeper.MainActivity;
import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.adapter.PlaceArrayAdapter;
import me.geoking.travelkeeper.model.AppDatabase;
import me.geoking.travelkeeper.model.Visit;

import static me.geoking.travelkeeper.R.id.visit_details_remove;
import static me.geoking.travelkeeper.R.id.visit_details_upload;

public class VisitDetailsEditFragment extends Fragment implements View.OnClickListener,  DatePickerDialog.OnDateSetListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String VISIT = "Visit";

    private Visit visit;
    private String placeid = null;

    private OnFragmentInteractionListener mListener;

    public static final int GET_FROM_GALLERY = 3;

    public Bitmap bitmap = null;

    private static final String TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private static final LatLngBounds BOUNDS_ASTON_UNI = new LatLngBounds(
            new LatLng(52.485403, -1.891407), new LatLng(52.487153, -1.887759));


    private GeoDataClient mGeoDataClient;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;


    public VisitDetailsEditFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static VisitDetailsEditFragment newInstance() {
        VisitDetailsEditFragment fragment = new VisitDetailsEditFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            visit = (Visit) getArguments().getSerializable(VISIT);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.visit_details_confirm, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {

            String title = visit.getTitle();
            String tags = visit.getTags();
            String date = visit.getVisitDate();
            String notes = visit.getNotes();
            String imageLocation = visit.getImageLocation();
            ArrayList holidays = (ArrayList) AppDatabase.getInstance().getHolidayDao().getHolidays();
            getActivity().setTitle(title);
            EditText visitTitle = getActivity().findViewById(R.id.visit_details_title);
            EditText visitTags = getActivity().findViewById(R.id.visit_details_tags);
            Button visitDate = (Button) getActivity().findViewById(R.id.visit_details_date);
            EditText visitNotes = getActivity().findViewById(R.id.visit_details_notes);
            if (imageLocation != null) {
                ImageView visitImage = getActivity().findViewById(R.id.visit_details_image);
                visitImage.setImageBitmap(((MainActivity)getActivity()).loadImageFromStorage(visit.getImageLocation(), visit.getImageLocationUUID()));
            }
            visitTitle.setText(title);
            visitTags.setText(tags);
            visitDate.setText(date);
            visitNotes.setText(notes);
        }
        else {
            String title = getActivity().getResources().getString(R.string.title_visit_details_add);
            getActivity().setTitle(title);
        }

    }

    public boolean checkInputErrors() {
        EditText visitTitle = (EditText)getActivity().findViewById(R.id.visit_details_title);
        Button dateButton = (Button)getActivity().findViewById(R.id.visit_details_date);
        String newTitle = visitTitle.getText().toString();
        String newStartDate = dateButton.getText().toString();
        boolean error = false;
        if (TextUtils.isEmpty(newTitle)) {
            visitTitle.setError("This must not be empty!");
            error = true;
        }
        if (newStartDate.equals("Date Of Visit")) {
            dateButton.setError("This must not be empty!");
            error = true;
        }

        if (!(error)) {
            return true;
        }
        return false;
    }

    public String getPlaceid() {
        return placeid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visit_details_edit, container, false);
        final Button visitDate = (Button) view.findViewById(R.id.visit_details_date);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        final DatePickerDialog datePickerDialogDate = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                // set day of month , month and year value in the edit text
                visitDate.setText(dayOfMonth + "/"
                        + (monthOfYear + 1) + "/" + year);

            }
        }, mYear, mMonth, mDay);
        visitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialogDate.show();
            }
        });
        Button uploadButton = (Button) view.findViewById(visit_details_upload);
        Button removeButton = (Button) view.findViewById(visit_details_remove);
        if (visit != null) {
            if (visit.getImageLocation() == null) {
                removeButton.setVisibility(View.GONE);
            }
        }
        else {
            removeButton.setVisibility(View.GONE);

        }
        uploadButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);

        mAutocompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.visit_details_title);
        mAutocompleteTextView.setThreshold(3);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1,
                BOUNDS_ASTON_UNI, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        return view;
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            placeid = String.valueOf(item.placeId);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case visit_details_upload:
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                break;
            case visit_details_remove:
                buildAlertDialog("Remove Image", "Are you sure you want to remove this image?\n\nThis process CANNOT be undone!");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (!(bitmap == null)) {
                ImageView visitImg = getActivity().findViewById(R.id.visit_details_image);
                Bitmap test = Bitmap.createScaledBitmap(bitmap, 600, 405, false);
                visitImg.setImageBitmap(test);
                Button uploadButton = (Button) getView().findViewById(visit_details_upload);
                uploadButton.setVisibility(View.GONE);
                Button removeButton = (Button) getView().findViewById(visit_details_remove);
                removeButton.setVisibility(View.VISIBLE);
            }
        }
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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void buildAlertDialog(String title, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (visit != null) {
                                if (visit.getImageLocation() != null) {
                                    File fdelete = new File(visit.getImageLocation());
                                    fdelete.delete();
                                }
                                visit.setImageLocation(null);
                                visit.setImageLocationUUID(null);
                            }
                            ImageView visitImg = getActivity().findViewById(R.id.visit_details_image);
                            visitImg.setImageDrawable(null);
                            Button removeButton = (Button) getView().findViewById(visit_details_remove);
                            removeButton.setVisibility(View.GONE);
                            Button uploadButton = (Button) getView().findViewById(visit_details_upload);
                            uploadButton.setVisibility(View.VISIBLE);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .show();
        }
}
