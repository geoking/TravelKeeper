package me.geoking.travelkeeper.model;

import android.content.Context;
import java.io.Serializable;
import me.geoking.travelkeeper.R;

/**
 * Created by george on 14/12/2017.
 */

public class Holiday implements Serializable {
    private String description;
    private String notes;
    private String startDate;
    private String endDate;

    public Holiday(Context context) {
        description = context.getString(R.string.holiday_details_title);
        notes = context.getString(R.string.holiday_notes);
     //   startDate = context.getString(R.string.start_date);
     //   endDate = context.getString(R.string.end_date);
    }
}
