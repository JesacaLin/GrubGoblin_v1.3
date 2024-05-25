package com.JesacaLin.GrubGoblin_v13.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a deal offered by a place.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deal {
    /**
     * The unique identifier for the deal.
     */
    private int dealId;

    /**
     * The unique identifier for the place offering the deal.
     */
    private int placeId;

    /**
     * The type of the deal.
     */
    private String typeOfDeal;

    /**
     * A description of the deal.
     */
    private String dealDescription;

    /**
     * The days of the week when the deal is available.
     */
    private String daysOfWeek;

    /**
     * The time when the deal starts.
     */
    private LocalTime startTime;

    /**
     * The time when the deal ends.
     */
    private LocalTime endTime;

    /**
     * The date and time when the deal was created.
     */
    private LocalDateTime createdAt;

    /**
     * The date and time when the deal was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * The username of the user who created the deal.
     */
    private String createdBy;

    /**
     * Returns a string representation of the deal.
     *
     * @return a string representation of the deal
     */
    @Override
    public String toString() {
        return "Deal {" +
                " ID = " + dealId +
                ", PLACE ID = " + placeId +
                ", TYPE OF DEAL = " + typeOfDeal +
                ", DEAL DESCRIPTION = " + dealDescription +
                ", DAY OF THE WEEK = " + daysOfWeek +
                ", START TIME = " + startTime +
                ", END TIME = " + endTime +
                ", CREATED AT = " + createdAt +
                ", UPDATED AT = " + updatedAt +
                ", CREATED BY = " + createdBy +
                " }";
    }
}
