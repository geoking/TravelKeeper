package me.geoking.travelkeeper.model;

import java.io.Serializable;

/**
 * Created by george on 14/12/2017.
 */

public class Holiday implements Serializable {
    private String description;
    private String notes;
    private String startDate;
    private String endDate;

    public Holiday() {
        description = "?";
        notes = "?";
        startDate = "?";
        endDate = "?";
    }
}
