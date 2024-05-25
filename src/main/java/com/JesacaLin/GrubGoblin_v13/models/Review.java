package com.JesacaLin.GrubGoblin_v13.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a review of a deal.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    /**
     * The unique identifier for the review.
     */
    private int reviewId;

    /**
     * The unique identifier for the deal that the review is for.
     */
    private int dealId;

    /**
     * The star rating given in the review.
     */
    private double stars;

    /**
     * The text of the review.
     */
    private String reviewDescription;

    /**
     * The username of the user who wrote the review.
     */
    private String reviewedBy;

    /**
     * Returns a string representation of the review.
     *
     * @return a string representation of the review
     */

    @Override
    public String toString() {
        return "Review {" +
                " ID = " + reviewId +
                ", DEAL ID = " + dealId +
                ", STARS = " + stars +
                ", REVIEW DESCRIPTION = " + reviewDescription +
                ", REVIEWED BY = " + reviewedBy +
                " }";
    }
}
