package com.ent.service;

import com.ent.config.CityTaxConfig;
import com.ent.model.TaxRule;
import com.ent.model.VehicleType;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Log4j2
@Service
public class CongestionTaxCalculatorServiceImpl implements CongestionTaxCalculatorService {

    private final CityTaxConfig config;
    private final TaxFreeDateService taxFreeService;

    public CongestionTaxCalculatorServiceImpl(CityTaxConfig config, TaxFreeDateService taxFreeService) {
        this.config = config;
        this.taxFreeService = taxFreeService;
    }

    @Override
    public int calculateTax(VehicleType vehicle, List<LocalDateTime> timestamps) {
        log.info("Starting tax calculation for vehicle: {} with {} timestamps", vehicle, timestamps.size());

        if (isTaxExempt(vehicle)) {
            log.info("Vehicle {} is tax-exempt. Returning 0.", vehicle);
            return 0;
        }

        Map<LocalDate, List<LocalDateTime>> timestampsByDate = groupTimestampsByDate(timestamps);
        log.debug("Grouped timestamps by date: {}", timestampsByDate.keySet());

        int totalTax = 0;

        for (Map.Entry<LocalDate, List<LocalDateTime>> entry : timestampsByDate.entrySet()) {
            LocalDate date = entry.getKey();
            List<LocalDateTime> dailyTimestamps = entry.getValue();

            if (taxFreeService.isTaxFreeDate(date)) {
                log.debug("Skipping tax calculation for tax-free date: {}", date);
                continue;
            }

            int dailyTax = calculateDailyTax(dailyTimestamps);
            int cappedTax = Math.min(dailyTax, config.getMaxDailyTax());

            log.debug("Tax for {}: calculated={}, capped={}", date, dailyTax, cappedTax);
            totalTax += cappedTax;
        }

        log.info("Total tax for vehicle {}: {} SEK", vehicle, totalTax);
        return totalTax;
    }

    private boolean isTaxExempt(VehicleType vehicle) {
        return config.getTaxFreeVehicleTypes().contains(vehicle);
    }

    private Map<LocalDate, List<LocalDateTime>> groupTimestampsByDate(List<LocalDateTime> timestamps) {
        Map<LocalDate, List<LocalDateTime>> grouped = new HashMap<>();
        for (LocalDateTime timestamp : timestamps) {
            grouped.computeIfAbsent(timestamp.toLocalDate(), k -> new ArrayList<>()).add(timestamp);
        }
        return grouped;
    }

    private int calculateDailyTax(List<LocalDateTime> timestamps) {
        if (timestamps == null || timestamps.isEmpty()) {
            return 0;
        }

        timestamps.sort(Comparator.naturalOrder());

        int totalTax = 0;
        LocalDateTime intervalStart = timestamps.get(0);
        int highestFeeInInterval = getFee(intervalStart.toLocalTime());

        for (int i = 1; i < timestamps.size(); i++) {
            LocalDateTime current = timestamps.get(i);
            int currentFee = getFee(current.toLocalTime());

            if (isWithinOneHour(intervalStart, current)) {
                highestFeeInInterval = Math.max(highestFeeInInterval, currentFee);
                log.debug("Within 60 min window ({} → {}, {} min): updating highestFeeInInterval to {}",
                        intervalStart, current,
                        Duration.between(intervalStart, current).toMinutes(),
                        highestFeeInInterval);
            } else {
                totalTax += highestFeeInInterval;
                intervalStart = current;
                highestFeeInInterval = currentFee;

                log.debug("Adding fee {} to total. New interval starts at {}", totalTax, intervalStart);
            }
        }

        totalTax += highestFeeInInterval;
        log.debug("Adding final fee {} to total for the day", highestFeeInInterval);

        return totalTax;
    }

    private boolean isWithinOneHour(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes() <= 60;
    }

    private int getFee(LocalTime time) {
        return config.getRules().stream()
                .filter(rule -> rule.isWithin(time))
                .mapToInt(TaxRule::getAmount)
                .findFirst()
                .orElse(0);
    }

}