package com.JesacaLin.GrubGoblin_v13.viewmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FullDealDetails {
    private int dealId;
    private String placeName;
    private String address;
    private String typeOfDeal;
    private String dealDescription;
    private int dayOfWeek;
    private LocalTime startTime;
    private double stars;
    private String reviewDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;

    @Override
    public String toString() {
        return "Deal Detail {\n" +
                "   DEAL ID = " + dealId + ",\n" +
                "   PLACE NAME = " + placeName + ",\n" +
                "   ADDRESS = " + address + ",\n" +
                "   TYPE OF DEAL = " + typeOfDeal + ",\n" +
                "   DEAL DESCRIPTION = " + dealDescription + ",\n" +
                "   DAY OF THE WEEK = " + dayOfWeek + ",\n" +
                "   START TIME = " + startTime + ",\n" +
                "   DEAL RATING = " + stars + ",\n" +
                "   REVIEW = " + reviewDescription + "\n" +
                ", CREATED AT = " + createdAt +
                ", UPDATED AT = " + updatedAt +
                ", CREATED BY = " + createdBy +
                "}";
    }
}
