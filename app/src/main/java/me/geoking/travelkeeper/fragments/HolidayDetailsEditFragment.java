package me.geoking.travelkeeper.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import me.geoking.travelkeeper.MainActivity;
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
public class HolidayDetailsEditFragment extends Fragment implements View.OnClickListener,  DatePickerDialog.OnDateSetListener{

    private static final String HOLIDAY = "Holiday";

    private Holiday holiday;

    private OnFragmentInteractionListener mListener;

    public static final int GET_FROM_GALLERY = 3;

    public Bitmap bitmap = null;


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
            String tags = holiday.getTags();
            String startDate = holiday.getStartDate();
            String endDate = holiday.getEndDate();
            String notes = holiday.getNotes();
            Bitmap image = holiday.getImage();
            getActivity().setTitle(title);
            EditText holidayTitle = getActivity().findViewById(R.id.holiday_details_title);
            EditText holidayTags = getActivity().findViewById(R.id.holiday_details_tags);
            Button holidayDateStart = (Button) getActivity().findViewById(R.id.holiday_details_start);
            Button holidayDateEnd = (Button) getActivity().findViewById(R.id.holiday_details_end);
            EditText holidayNotes = getActivity().findViewById(R.id.holiday_details_notes);
            if (image != null) {
                ImageView holidayImage = getActivity().findViewById(R.id.holiday_details_image);
                holidayImage.setImageBitmap(image);
            }
            holidayTitle.setText(title);
            holidayTags.setText(tags);
            holidayDateStart.setText(startDate);
            holidayDateEnd.setText(endDate);
            holidayNotes.setText(notes);
        }
        else {
            String title = getActivity().getResources().getString(R.string.title_holiday_details_add);
            getActivity().setTitle(title);
        }

    }

    public boolean checkInputErrors() {
        EditText holidayTitle = (EditText)getActivity().findViewById(R.id.holiday_details_title);
        Button startDateButton = (Button)getActivity().findViewById(R.id.holiday_details_start);
        Button endDateButton = (Button)getActivity().findViewById(R.id.holiday_details_end);
        String newTitle = holidayTitle.getText().toString();
        String newStartDate = startDateButton.getText().toString();
        String newEndDate = endDateButton.getText().toString();
        boolean error = false;
        if (TextUtils.isEmpty(newTitle)) {
            holidayTitle.setError("This must not be empty!");
            error = true;
        }
        if (newStartDate.equals("Start Date")) {
            startDateButton.setError("This must not be empty!");
            error = true;
        }
        if (newEndDate.equals("End Date")) {
            endDateButton.setError("This must not be empty!");
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
        View view = inflater.inflate(R.layout.fragment_holiday_details_edit, container, false);
        final Button holidayDateStart = (Button) view.findViewById(R.id.holiday_details_start);
        final Button holidayDateEnd = (Button) view.findViewById(R.id.holiday_details_end);
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
                holidayDateStart.setText(dayOfMonth + "/"
                        + (monthOfYear + 1) + "/" + year);

            }
        }, mYear, mMonth, mDay);
        final DatePickerDialog datePickerDialogEnd = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                // set day of month , month and year value in the edit text
                holidayDateEnd.setText(dayOfMonth + "/"
                        + (monthOfYear + 1) + "/" + year);

            }
        }, mYear, mMonth, mDay);
        holidayDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialogStart.show();
            }
        });
        holidayDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialogEnd.show();
            }
        });

        Button uploadButton = (Button) view.findViewById(R.id.holiday_details_upload);
        uploadButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.holiday_details_upload:
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

                break;
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
                ImageView holidayImg = getActivity().findViewById(R.id.holiday_details_image);
                Bitmap test = Bitmap.createScaledBitmap(bitmap, 600, 405, false);
                holidayImg.setImageBitmap(test);
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
