package me.geoking.travelkeeper.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import me.geoking.travelkeeper.util.Constants;
@Dao
public interface HolidayDao {

    @Query("SELECT * FROM "+ Constants.TABLE_NAME_HOLIDAYS)
    List<Holiday> getHolidays();

    @Insert
    void insertHoliday(Holiday holiday);

    @Update
    void updateHoliday(Holiday holiday);

    @Delete
    void deleteHoliday(Holiday holiday);

}
