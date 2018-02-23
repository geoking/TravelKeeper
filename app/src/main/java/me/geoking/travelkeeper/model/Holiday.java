package me.geoking.travelkeeper.model;

import android.content.Context;
import java.io.Serializable;
import me.geoking.travelkeeper.R;

/**
 * Created by george on 14/12/2017.
 */

public class Holiday implements Serializable {
    private String description;
    private String notes;
    private String startDate;
    private String endDate;
    private String tags;

    public Holiday() {
        description = "enter a title";
        notes = "enter notes";
        startDate = "enter start date";
        endDate = "enter end date";
        tags = "enter tags";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
