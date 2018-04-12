package me.geoking.travelkeeper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import me.geoking.travelkeeper.MainActivity;
import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.model.Holiday;
import me.geoking.travelkeeper.model.HolidayDatabase;

public class HolidayFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    public HolidayFragment() {
    }

    public static HolidayFragment newInstance(int columnCount) {
        HolidayFragment fragment = new HolidayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.holiday_details_add, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = getActivity().getResources().getString(R.string.title_holidays);
        getActivity().setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       /* if (HolidayDatabase.getInstance(this.getContext()).getHolidayDao().getHolidays().size() == 0) {
            Holiday holiday = new Holiday();
            HolidayDatabase.getInstance(this.getContext()).getHolidayDao().insertHoliday(holiday);
        }*/

        if (HolidayDatabase.getInstance().getHolidayDao().getHolidays().size() == 0) {
            View view = inflater.inflate(R.layout.fragment_holiday_noholiday, container, false);
            setHasOptionsMenu(true);
            return view;
        }
        else {
            View view = inflater.inflate(R.layout.fragment_holiday_list, container, false);
            setHasOptionsMenu(true);
            ImageView holidayImage = view.findViewById(R.id.holidays_image);
            TextView holidayTitle = view.findViewById(R.id.holidays_title);
            TextView holidayDate = view.findViewById(R.id.holidays_dates);
            ArrayList holidays = (ArrayList) HolidayDatabase.getInstance().getHolidayDao().getHolidays();
            Collections.reverse(holidays);
            Holiday holiday = (Holiday) holidays.get(0);
            if (holiday.getImageLocation() == null) {
                holidayImage.setVisibility(View.GONE);
                holidayTitle.setVisibility(View.GONE);
                holidayDate.setVisibility(View.GONE);
            }
            else {
                String dateStartString = holiday.getStartDate();
                String dateEndString = holiday.getEndDate();

                holidayDate.setText(getFullDate(dateStartString, dateEndString));
                holidayTitle.setText(holiday.getTitle());

                holidayImage.setImageBitmap(((MainActivity)getActivity()).loadImageFromStorage(holiday.getImageLocation(), holiday.getImageLocationUUID()));
            }

            // Set the adapter
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.holidays_list);
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                }
                recyclerView.setAdapter(new MyHolidayRecyclerViewAdapter(holidays, mListener));


            return view;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public String getFullDate(String dateStartString, String dateEndString) {
        DateFormat format = new SimpleDateFormat("d/MM/yyyy", Locale.ENGLISH);
        DateFormat formatDay = new SimpleDateFormat("d", Locale.ENGLISH);
        DateFormat formatMonth = new SimpleDateFormat("MMM", Locale.ENGLISH);
        DateFormat formatMonthYear = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
        String fullDate = "No date found";
        try {
            Date startDate = format.parse(dateStartString);
            Date endDate = format.parse(dateEndString);

            String startDay = formatDay.format(startDate);
            String startMonth = formatMonth.format(startDate);
            int startResult = Integer.parseInt(startDay);
            String endDay = formatDay.format(endDate);
            int endResult = Integer.parseInt(endDay);
            String  endMonthYear = formatMonthYear.format(endDate);


            fullDate = startDay + getDayNumberSuffix(startResult) + " " + startMonth + " - " + endDay + getDayNumberSuffix(endResult) + " " + endMonthYear;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fullDate;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Holiday holiday);
    }
}
