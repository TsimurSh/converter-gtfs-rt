package pl.goeuropa.converter.models;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class VehicleStatistics implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int rideTime;
    private int idleTime;
    private int parkTime;
    private int maxIdleTime;
    private int distance;
    private int starts;
    private int avgSpeed;
    private int lastStartedAt;
    private int todayStartedAt;
    private int avgFuelConsumed;
    private int maxSpeed;
    private int maxSpeedPercentage;
    private int over90Percentage;
    private int drivingStyle;
    private int realParkTime;
    private int currParkTime;
    private int engineBrakingTime;
    private int engineBrakingPercentage;
    private int ridingOnIdleTime;
    private int ridingOnIdlePercentage;
}
