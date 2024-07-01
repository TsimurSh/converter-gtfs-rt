package pl.goeuropa.converter.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Vehicle implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int vehicleId;
    private boolean monitoringRestricted;
    private boolean positionUpdateRestricted;
    private double latitude;
    private double longitude;
    private int speed;
    private int satellites;
    private int distance;
    private long timestamp;
    private int fuel;
    private int roaming;
    private int supply;
    private double battery;
    private int dallasId;
    private int altDallasId;
    private int heading;
    private VehicleStatistics vehicleStatistics;
    private List<VehicleStatusIcon> vehicleStatusIcons;
    private String EXTSPEEDLIMIT;
    private String EXTDEGEOCODE;
    private String EXTStanZasil;
}
