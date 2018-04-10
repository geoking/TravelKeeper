package me.geoking.travelkeeper.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by george on 14/12/2017.
 */

public class Holiday implements Serializable {
    private String title;
    private String notes;
    private String startDate;
    private String endDate;
    private String tags;
    private Bitmap image;

    public Holiday() {
        title = "enter a title";
        notes = "enter notes";
        startDate = "enter start date";
        endDate = "enter end date";
        tags = "enter tags";
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
