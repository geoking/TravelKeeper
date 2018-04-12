package me.geoking.travelkeeper.model;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.UUID;

import me.geoking.travelkeeper.MainActivity;

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

    public Holiday addHoliday (Holiday holiday, String title, String tags, String startDate, String endDate, String notes, String imageLocation, UUID uuid) {
        holiday.setTitle(title);
        holiday.setTags(tags);
        holiday.setStartDate(startDate);
        holiday.setEndDate(endDate);
        holiday.setNotes(notes);
        holiday.setImageLocation(imageLocation);
        holiday.setImageLocationUUID(uuid);
        return holiday;
    }

    public void deleteHoliday (Holiday holiday) {
        holidays.remove(holiday);
    }
}
