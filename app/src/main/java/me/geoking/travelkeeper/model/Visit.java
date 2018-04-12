package me.geoking.travelkeeper.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import me.geoking.travelkeeper.util.Constants;

/**
 * Created by george on 14/12/2017.
 */
@Entity(tableName = Constants.TABLE_NAME_VISITED)
public class Visit implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long visit_id;
    @ColumnInfo(name = "visited_content") // column name will be "visited_content" instead of "content" in table
    private String content;

    private String title;
    private String notes;
    private String visitDate;
    private String tags;
    private String imageLocation;
    private String imageLocationUUID;
    private String location;


    public Visit() {
        title = "enter a title";
        notes = "enter notes";
        visitDate = "enter visit date";
        tags = "enter tags";
        imageLocation = "image location";
        imageLocationUUID = null;
        location = null;
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

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String startDate) {
        this.visitDate = visitDate;
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

    public long getVisit_id() {
        return visit_id;
    }

    public void setVisit_id(long holiday_id) {
        this.visit_id = holiday_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
