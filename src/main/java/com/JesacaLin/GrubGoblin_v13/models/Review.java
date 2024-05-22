package com.JesacaLin.GrubGoblin_v13.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private int reviewId;
    private int dealId;
    private double stars;
    private String reviewDescription;
    private String reviewedBy;

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
