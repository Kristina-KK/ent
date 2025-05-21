package com.ent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class TaxRule {

    private LocalTime start;
    private LocalTime end;
    private int amount;

    public boolean isWithin(LocalTime time) {
        return !time.isBefore(start) && !time.isAfter(end);
    }

}
