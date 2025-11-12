package pl.goeuropa.tc_helper.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Assignment {

    @NotNull
    private String vehicleId;
    private String blockId;
    private String tripId;
    @NotNull
    private String validFrom;
    private String validTo;
}
