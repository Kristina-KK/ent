package com.ent.controller;

import com.ent.dto.TaxRequest;
import com.ent.dto.TaxResponse;
import com.ent.service.CongestionTaxCalculatorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/calculate-tax")
public class TaxController {

    private final CongestionTaxCalculatorService congestionTaxCalculatorService;

    public TaxController(CongestionTaxCalculatorService congestionTaxCalculatorService) {
        this.congestionTaxCalculatorService = congestionTaxCalculatorService;
    }

    @PostMapping
    public TaxResponse calculateTax(@RequestBody TaxRequest request) {
        log.info("Received tax calculation request for vehicleType={} with {} timestamps",
                request.getVehicleType(), request.getTimestamp().size());

        log.debug("Full request payload: {}", request);

        int tax = congestionTaxCalculatorService.calculateTax(request.getVehicleType(), request.getTimestamp());

        log.info("Calculated total tax: {} SEK", tax);
        return new TaxResponse(tax);
    }

}
