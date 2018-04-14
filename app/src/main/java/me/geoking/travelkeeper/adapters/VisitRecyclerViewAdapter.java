package me.geoking.travelkeeper.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.geoking.travelkeeper.fragments.VisitFragment.OnFragmentInteractionListener;
import me.geoking.travelkeeper.R;
import me.geoking.travelkeeper.model.Visit;

public class VisitRecyclerViewAdapter extends RecyclerView.Adapter<VisitRecyclerViewAdapter.ViewHolder> {

    private final List<Visit> mVisits;
    private final OnFragmentInteractionListener mListener;

    public VisitRecyclerViewAdapter(List<Visit> visits, OnFragmentInteractionListener listener) {
        mVisits = visits;
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
        holder.mVisit = mVisits.get(position);
        holder.mVisitLetterView.setText(mVisits.get(position).getTitle());
        holder.mVisitTitleView.setText(mVisits.get(position).getTitle() + " (" + mVisits.get(position).getVisitDate() + ")");
        holder.mVisitNotesView.setText(mVisits.get(position).getNotes());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that a visit has been selected.
                    Visit visit = holder.mVisit;
                    mListener.onListFragmentInteraction(visit);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVisits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mVisitTitleView;
        public final TextView mVisitNotesView;
        public final TextView mVisitLetterView;
        public Visit mVisit;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mVisitLetterView = (TextView) view.findViewById(R.id.holiday_letter_view);
            mVisitTitleView = (TextView) view.findViewById(R.id.holiday_title_view);
            mVisitNotesView = (TextView) view.findViewById(R.id.holiday_notes_view);
            mVisitTitleView.setTypeface(null, Typeface.BOLD);
            mVisitLetterView.setTypeface(null, Typeface.BOLD);
            mVisitLetterView.setAllCaps(true);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mVisitNotesView.getText() + "'";
        }
    }
}
