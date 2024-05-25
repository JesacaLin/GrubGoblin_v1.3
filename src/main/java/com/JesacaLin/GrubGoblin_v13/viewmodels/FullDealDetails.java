package com.JesacaLin.GrubGoblin_v13.viewmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * Represents the full details of a deal.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FullDealDetails {
    /**
     * The unique identifier for the deal.
     */
    private int dealId;

    /**
     * The name of the place where the deal is available.
     */
    private String placeName;

    /**
     * The address of the place where the deal is available.
     */
    private String address;

    /**
     * The type of the deal.
     */
    private String typeOfDeal;

    /**
     * The description of the deal.
     */
    private String dealDescription;

    /**
     * The days of the week when the deal is available.
     */
    private String daysOfWeek;

    /**
     * The start time of the deal.
     */
    private LocalTime startTime;

    /**
     * The star rating of the deal.
     */
    private double stars;

    /**
     * The description of the review for the deal.
     */
    private String reviewDescription;

    /**
     * The date and time when the deal was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * The username of the user who created the deal.
     */
    private String createdBy;

    /**
     * Returns a string representation of the deal details.
     *
     * @return a string representation of the deal details
     */

    @Override
    public String toString() {
        return "Deal Detail {\n" +
                "   DEAL ID = " + dealId + ",\n" +
                "   PLACE NAME = " + placeName + ",\n" +
                "   ADDRESS = " + address + ",\n" +
                "   TYPE OF DEAL = " + typeOfDeal + ",\n" +
                "   DEAL DESCRIPTION = " + dealDescription + ",\n" +
                "   DAY OF THE WEEK = " + daysOfWeek + ",\n" +
                "   START TIME = " + startTime + ",\n" +
                "   DEAL RATING = " + stars + ",\n" +
                "   REVIEW = " + reviewDescription + "\n" +
                ", UPDATED AT = " + updatedAt +
                ", CREATED BY = " + createdBy +
                "}";
    }
}
