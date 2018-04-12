package me.geoking.travelkeeper.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import me.geoking.travelkeeper.util.Constants;

@Dao
public interface VisitDao {

    @Query("SELECT * FROM "+ Constants.TABLE_NAME_HOLIDAYS)
    List<Visit> getVisit();

    @Insert
    void insertVisit(Visit visit);

    @Update
    void updateVisit(Visit visit);

    @Delete
    void deleteVisit(Visit visit);

}
