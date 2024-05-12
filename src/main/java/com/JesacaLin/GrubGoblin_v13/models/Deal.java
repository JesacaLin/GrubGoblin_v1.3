package com.JesacaLin.GrubGoblin_v13.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deal {
    private int dealId;
    private int placeId;
    private String typeOfDeal;
    private String dealDescription;
    private int createdBy;

    @Override
    public String toString() {
        return "Deal {" +
                " ID = " + dealId +
                ", PLACE ID = " + placeId +
                ", TYPE OF DEAL = " + typeOfDeal +
                ", DEAL DESCRIPTION = " + dealDescription +
                ", CREATED BY = " + createdBy +
                " }";
    }
}
