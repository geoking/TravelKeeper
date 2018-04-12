package me.geoking.travelkeeper.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

import me.geoking.travelkeeper.util.Constants;

/**
 * Created by george on 14/12/2017.
 */
@Entity(tableName = Constants.TABLE_NAME_HOLIDAYS)
public class Holiday implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long holiday_id;
    @ColumnInfo(name = "holiday_content") // column name will be "holiday_content" instead of "content" in table
    private String content;

    private String title;
    private String notes;
    private String startDate;
    private String endDate;
    private String tags;
    private String imageLocation;
    private String imageLocationUUID;


    public Holiday() {
        title = "enter a title";
        notes = "enter notes";
        startDate = "enter start date";
        endDate = "enter end date";
        tags = "enter tags";
        imageLocation = "image location";
        imageLocationUUID = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getImageLocationUUID() {
        return imageLocationUUID;
    }

    public void setImageLocationUUID(String imageLocationUUID) {
        this.imageLocationUUID = imageLocationUUID;
    }

    public long getHoliday_id() {
        return holiday_id;
    }

    public void setHoliday_id(long holiday_id) {
        this.holiday_id = holiday_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
