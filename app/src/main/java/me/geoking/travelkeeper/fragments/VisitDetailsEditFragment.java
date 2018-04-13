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
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import me.geoking.travelkeeper.MainActivity;
import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.model.Visit;

import static me.geoking.travelkeeper.R.id.visit_details_remove;
import static me.geoking.travelkeeper.R.id.visit_details_upload;

public class VisitDetailsEditFragment extends Fragment implements View.OnClickListener,  DatePickerDialog.OnDateSetListener{

    private static final String VISIT = "Visit";

    private Visit visit;

    private OnFragmentInteractionListener mListener;

    public static final int GET_FROM_GALLERY = 3;

    public Bitmap bitmap = null;


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
        if (newStartDate.equals("Start Date")) {
            dateButton.setError("This must not be empty!");
            error = true;
        }

        if (!(error)) {
            return true;
        }
        return false;
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
        final DatePickerDialog datePickerDialogStart = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                // set day of month , month and year value in the edit text
                visitDate.setText(dayOfMonth + "/"
                        + (monthOfYear + 1) + "/" + year);

            }
        }, mYear, mMonth, mDay);
        final DatePickerDialog datePickerDialogEnd = new DatePickerDialog(
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

                datePickerDialogStart.show();
            }
        });
        visitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialogEnd.show();
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



        return view;
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
