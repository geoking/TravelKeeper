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


    private static HolidayDatabase instance;

    public static HolidayDatabase createInstance(Context context) {
        if (null == instance) {
            instance = buildDatabaseInstance(context);
        }
        return instance;
    }

    public static HolidayDatabase getInstance() {
        return instance;
    }

    private static HolidayDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                HolidayDatabase.class,
                Constants.DB_NAME).allowMainThreadQueries().build();
    }

    public  void cleanUp(){
        instance = null;
    }
}
