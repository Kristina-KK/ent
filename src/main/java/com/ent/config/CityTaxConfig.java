package com.ent.config;

import com.ent.model.TaxRule;
import com.ent.model.VehicleType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "tax-config")
public class CityTaxConfig {

    private String city;
    private int maxDailyTax;
    private List<TaxRule> rules;
    private Set<Integer> taxFreeMonths;
    private Set<VehicleType> taxFreeVehicleTypes;

}
