package com.JesacaLin.GrubGoblin_v13.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Availability {
    private int availabilityId;
    private int dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public String toString() {
        return "Availability {" +
                " ID = " + availabilityId +
                ", DAY OF THE WEEK = " + dayOfWeek +
                ", START TIME = " + startTime +
                ", END TIME = " + endTime +
                " }";
    }
}
