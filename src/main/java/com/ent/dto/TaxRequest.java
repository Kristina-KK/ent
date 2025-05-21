package com.ent.dto;

import com.ent.model.VehicleType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class TaxRequest {

    private VehicleType vehicleType;
    private List<LocalDateTime> timestamp;

}
