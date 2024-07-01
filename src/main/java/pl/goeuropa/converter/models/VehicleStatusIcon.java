package pl.goeuropa.converter.models;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class VehicleStatusIcon implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String token;
    private int priority;
}
