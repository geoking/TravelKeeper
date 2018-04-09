package me.geoking.travelkeeper.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.geoking.travelkeeper.fragments.HolidayFragment.OnListFragmentInteractionListener;
import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.model.Holiday;

import java.util.List;

public class MyHolidayRecyclerViewAdapter extends RecyclerView.Adapter<MyHolidayRecyclerViewAdapter.ViewHolder> {

    private final List<Holiday> mHolidays;
    private final OnListFragmentInteractionListener mListener;

    public MyHolidayRecyclerViewAdapter(List<Holiday> holidays, OnListFragmentInteractionListener listener) {
        mHolidays = holidays;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_holiday, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mHoliday = mHolidays.get(position);
        holder.mHolidayTitleView.setText(mHolidays.get(position).getTitle());
        holder.mHolidayNotesView.setText(mHolidays.get(position).getNotes());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an holiday has been selected.
                    Holiday holiday = holder.mHoliday;
                    mListener.onListFragmentInteraction(holiday);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHolidays.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mHolidayTitleView;
        public final TextView mHolidayNotesView;
        public Holiday mHoliday;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mHolidayTitleView = (TextView) view.findViewById(R.id.holiday_title_view);
            mHolidayNotesView = (TextView) view.findViewById(R.id.holiday_notes_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mHolidayNotesView.getText() + "'";
        }
    }
}
