package me.geoking.travelkeeper.model;

import android.content.Context;

/**
 * Created by george on 14/12/2017.
 */

public class HolidayData {
    private Context context;
    private Holiday currentHoliday;
    private static HolidayData instance;

    private HolidayData (Context context) {
        this.context = context;
        setCurrentHoliday(new Holiday(context));
    }

    public static HolidayData getInstance(Context context) {
        if (instance == null) {
            instance = new HolidayData(context);
        }
        return instance;
    }

    /**
     * In case we don't have a context...
     * @return the singleton instance
     */
    public static HolidayData getInstance() {
        return instance;
    }

    public Holiday getCurrentHoliday() {
        return currentHoliday;
    }

    public void setCurrentHoliday(Holiday currentHoliday) {
        this.currentHoliday = currentHoliday;
    }
}
