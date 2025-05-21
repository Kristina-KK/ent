package com.ent.service;

import com.ent.config.CityTaxConfig;
import com.ent.model.TaxRule;
import com.ent.model.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CongestionTaxCalculatorServiceTest {

    private CongestionTaxCalculatorService service;

    @BeforeEach
    void setUp() {
        CityTaxConfig config = new CityTaxConfig();
        config.setMaxDailyTax(60);
        config.setTaxFreeVehicleTypes(Set.of(
                VehicleType.MOTORCYCLE,
                VehicleType.EMERGENCY,
                VehicleType.BUS,
                VehicleType.MILITARY
        ));
        config.setRules(List.of(
                new TaxRule(LocalTime.of(6, 0), LocalTime.of(6, 29), 8),
                new TaxRule(LocalTime.of(6, 30), LocalTime.of(6, 59), 13),
                new TaxRule(LocalTime.of(7, 0), LocalTime.of(7, 59), 18),
                new TaxRule(LocalTime.of(8, 0), LocalTime.of(8, 29), 13),
                new TaxRule(LocalTime.of(8, 30), LocalTime.of(14, 59), 8),
                new TaxRule(LocalTime.of(15, 0), LocalTime.of(15, 29), 13),
                new TaxRule(LocalTime.of(15, 30), LocalTime.of(16, 59), 18),
                new TaxRule(LocalTime.of(17, 0), LocalTime.of(17, 59), 13),
                new TaxRule(LocalTime.of(18, 0), LocalTime.of(18, 29), 8)
        ));

        TaxFreeDateService taxFreeDateService = new TaxFreeDateServiceImpl();
        service = new CongestionTaxCalculatorServiceImpl(config, taxFreeDateService);
    }

    @Test
    void calculateTax_shouldReturnZeroForTaxExemptVehicle() {
        var timestamps = List.of(LocalDateTime.of(2013, 2, 8, 7, 0));
        int tax = service.calculateTax(VehicleType.MOTORCYCLE, timestamps);
        assertEquals(0, tax);
    }

    @Test
    void calculateTax_shouldSkipTaxFreeDateInJuly() {
        var timestamps = List.of(LocalDateTime.of(2013, 7, 15, 7, 0));
        int tax = service.calculateTax(VehicleType.CAR, timestamps);
        assertEquals(0, tax);
    }

    @Test
    void calculateTax_shouldSkipWeekend() {
        var timestamps = List.of(LocalDateTime.of(2013, 2, 9, 7, 0)); // Saturday
        int tax = service.calculateTax(VehicleType.CAR, timestamps);
        assertEquals(0, tax);
    }

    @Test
    void calculateTax_shouldReturnCorrectFeeForSinglePass() {
        var timestamps = List.of(LocalDateTime.of(2013, 2, 8, 6, 45)); // 13 SEK
        int tax = service.calculateTax(VehicleType.CAR, timestamps);
        assertEquals(13, tax);
    }

    @Test
    void calculateTax_shouldApplyHighestFeeWithin60Minutes() {
        var timestamps = List.of(
                LocalDateTime.of(2013, 2, 8, 6, 20), // 8 SEK
                LocalDateTime.of(2013, 2, 8, 6, 50), // 13 SEK
                LocalDateTime.of(2013, 2, 8, 7, 5)   // 18 SEK (new window)
        );
        int tax = service.calculateTax(VehicleType.CAR, timestamps);
        assertEquals(18, tax);
    }

    @Test
    void calculateTax_shouldNotExceedDailyMaximum() {
        var timestamps = List.of(
                LocalDateTime.of(2013, 2, 8, 6, 0),   // 8
                LocalDateTime.of(2013, 2, 8, 7, 0),   // 18
                LocalDateTime.of(2013, 2, 8, 8, 0),   // 13
                LocalDateTime.of(2013, 2, 8, 15, 30), // 18
                LocalDateTime.of(2013, 2, 8, 17, 0)   // 13
        );
        int tax = service.calculateTax(VehicleType.CAR, timestamps);
        assertEquals(60, tax);
    }

    @Test
    void calculateTax_shouldHandleMultipleDaysSeparately() {
        var timestamps = List.of(
                LocalDateTime.of(2013, 2, 7, 7, 0),  // 18
                LocalDateTime.of(2013, 2, 8, 8, 0)   // 13
        );
        int tax = service.calculateTax(VehicleType.CAR, timestamps);
        assertEquals(31, tax);
    }

    @Test
    void calculateTax_shouldReturnZeroIfNoTimestamps() {
        int tax = service.calculateTax(VehicleType.CAR, List.of());
        assertEquals(0, tax);
    }

}

