package me.geoking.travelkeeper.model;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;
import java.util.UUID;


public class DateRoomConverter {

    @TypeConverter
    public static String toString(UUID uuid) {
        return uuid.toString();
    }

    @TypeConverter
    public static UUID toUUID(String string) {
        return UUID.fromString(string);
    }
}
