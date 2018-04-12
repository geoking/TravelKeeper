package me.geoking.travelkeeper.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import me.geoking.travelkeeper.util.Constants;

@Database(entities = { Holiday.class }, version = 1)
@TypeConverters({DateRoomConverter.class})
public abstract class HolidayDatabase extends RoomDatabase {

    public abstract HolidayDao getHolidayDao();


    private static HolidayDatabase holidayDB;

    public static HolidayDatabase getInstance(Context context) {
        if (null == holidayDB) {
            holidayDB = buildDatabaseInstance(context);
        }
        return holidayDB;
    }

    private static HolidayDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                HolidayDatabase.class,
                Constants.DB_NAME).allowMainThreadQueries().build();
    }

    public  void cleanUp(){
        holidayDB = null;
    }
}
