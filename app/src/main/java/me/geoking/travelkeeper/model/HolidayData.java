package me.geoking.travelkeeper.model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by george on 14/12/2017.
 */

public class HolidayData {
    private Context context;
    private static HolidayData instance;
    private ArrayList<Holiday> holidays;

    private HolidayData (Context context) {
        this.context = context;
        holidays = new ArrayList<Holiday>();
        createData();
    }

    public static HolidayData createInstance(Context context) {
        if (instance == null) {
            instance = new HolidayData(context);
        }
        return instance;
    }

    public static HolidayData getInstance() {
        return instance;
    }

    private void createData() {

    }

    public ArrayList<Holiday> getHolidays() {
        return holidays;
    }

    public void addHoliday (Holiday holiday) {
        holidays.add(holiday);
    }

    public void deleteHoliday (Holiday holiday) {
        holidays.remove(holiday);
    }
}
