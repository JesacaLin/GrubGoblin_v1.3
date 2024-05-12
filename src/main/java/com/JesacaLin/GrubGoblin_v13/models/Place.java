package com.JesacaLin.GrubGoblin_v13.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    private int placeId;
    private String placeName;
    private String address;
    private double latitude;
    private double longitude;
    private double googleRating;


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
