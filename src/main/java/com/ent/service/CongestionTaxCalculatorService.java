package com.ent.service;

import com.ent.model.VehicleType;

import java.time.LocalDateTime;
import java.util.List;

public interface CongestionTaxCalculatorService {

    int calculateTax(VehicleType vehicleType, List<LocalDateTime> timestamp);

}
