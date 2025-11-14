package pl.goeuropa.tc_helper.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Assignment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String vehicleId;
    private String blockId;
    private String tripId;
    @NotNull
    private String validFrom;
    private String validTo;
}
