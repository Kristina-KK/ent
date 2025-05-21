package com.ent.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

@Log4j2
@Service
public class TaxFreeDateServiceImpl implements TaxFreeDateService {

    @Override
    public boolean isTaxFreeDate(LocalDate date) {
        log.debug("Checking if {} is a tax-free date", date);

        boolean isWeekend = date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY;
        boolean isJuly = date.getMonth() == Month.JULY;

        if (isWeekend || isJuly) {
            log.info("Date {} is tax-free (Weekend or July)", date);
            return true;
        }

        // TODO: Add logic for national holidays and day-before-holidays
        return false;
    }

}
