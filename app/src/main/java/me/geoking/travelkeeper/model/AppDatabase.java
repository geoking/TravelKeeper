package me.geoking.travelkeeper.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import me.geoking.travelkeeper.util.Constants;

@Database(entities = { Holiday.class, Visit.class }, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract HolidayDao getHolidayDao();
    public abstract VisitDao getVisitDao();


    private static AppDatabase instance;

    public static AppDatabase createInstance(Context context) {
        if (null == instance) {
            instance = buildDatabaseInstance(context);
        }
        return instance;
    }

    public static AppDatabase getInstance() {
        return instance;
    }

    private static AppDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                Constants.DB_NAME).allowMainThreadQueries().build();
    }

    public  void cleanUp(){
        instance = null;
    }
}
