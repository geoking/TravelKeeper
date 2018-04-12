package me.geoking.travelkeeper.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import me.geoking.travelkeeper.util.Constants;

@Database(entities = { Visit.class }, version = 1)
public abstract class VisitDatabase extends RoomDatabase {

    public abstract VisitDao getVisitDao();


    private static VisitDatabase instance;

    public static VisitDatabase createInstance(Context context) {
        if (null == instance) {
            instance = buildDatabaseInstance(context);
        }
        return instance;
    }

    public static VisitDatabase getInstance() {
        return instance;
    }

    private static VisitDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                VisitDatabase.class,
                Constants.DB_NAME).allowMainThreadQueries().build();
    }

    public  void cleanUp(){
        instance = null;
    }
}
