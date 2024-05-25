package com.JesacaLin.GrubGoblin_v13.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a place in the system.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    /**
     * The unique identifier for the place.
     */
    private int placeId;

    /**
     * The name of the place.
     */
    private String placeName;

    /**
     * The address of the place.
     */
    private String address;

    /**
     * The latitude of the place's location.
     */
    private double latitude;

    /**
     * The longitude of the place's location.
     */
    private double longitude;

    /**
     * The Google rating of the place.
     */
    private double googleRating;

    /**
     * Returns a string representation of the place.
     *
     * @return a string representation of the place
     */


    @Override
    public String toString() {
        return "Place {" +
                " ID = " + placeId +
                ", NAME = " + placeName +
                ", ADDRESS = " + address +
                ", LATITUDE = " + latitude +
                ", LONGITUDE = " + longitude +
                ", GOOGLE RATING = " + googleRating +
                " }";
    }
}
