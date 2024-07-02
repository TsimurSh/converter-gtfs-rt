package pl.goeuropa.converter.gtfsrt;

import com.google.transit.realtime.GtfsRealtime;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class GtfsRealTimeVehicleFeed {

    private static final int MS_PER_SEC = 1_000;

    public GtfsRealtime.FeedMessage create(List<LinkedHashMap<String, Object>> vehicles) {
        log.debug("GTFS-RT successfully created");
        return createMessage(vehicles);
    }

    private GtfsRealtime.FeedMessage createMessage(List<LinkedHashMap<String, Object>> vehicles) {
        GtfsRealtime.FeedMessage.Builder message = GtfsRealtime.FeedMessage.newBuilder();

        GtfsRealtime.FeedHeader.Builder feedheader = GtfsRealtime.FeedHeader.newBuilder()
                .setGtfsRealtimeVersion("1.0")
                .setIncrementality(GtfsRealtime.FeedHeader.Incrementality.FULL_DATASET)
                .setTimestamp(System.currentTimeMillis());
        message.setHeader(feedheader);

        vehicles.forEach((vehicle) -> {
            GtfsRealtime.FeedEntity.Builder vehiclePositionEntity = GtfsRealtime.FeedEntity
                    .newBuilder()
                    .setId(String.valueOf(vehicle.get("vehicle_title") != null ?
                            vehicle.get("vehicle_title") :
                            vehicle.get("unit_id")));
            try {
                // the Vehicle data input
                GtfsRealtime.VehiclePosition vehiclePosition = createVehiclePosition(vehicle);
                vehiclePositionEntity.setVehicle(vehiclePosition);
                message.addEntity(vehiclePositionEntity);
            } catch (Exception ex) {
                log.error("Something get wrong while parsing vehicle data", ex);
            }
        });
        return message.build();
    }

    private GtfsRealtime.VehiclePosition createVehiclePosition(LinkedHashMap<String, Object> vehicle) {

        GtfsRealtime.VehiclePosition.Builder vehiclePosition = GtfsRealtime.VehiclePosition.newBuilder();
        // the Description information
        GtfsRealtime.VehicleDescriptor.Builder vehicleDescriptor = GtfsRealtime.VehicleDescriptor.newBuilder()
                .setId(String.valueOf(vehicle.get("number")));
        // the Position information
        GtfsRealtime.Position.Builder position =
                GtfsRealtime.Position.newBuilder()
                        .setLatitude(getFloat(vehicle.get("lat")))
                        .setLongitude(getFloat(vehicle.get("lng")))
                        .setSpeed(getFloat(vehicle.get("speed")))
                        .setBearing(getFloat(vehicle.get("direction")));

        vehiclePosition.setPosition(position);
        vehiclePosition.setVehicle(vehicleDescriptor);
        vehiclePosition.setTimestamp(getTimestamp((String) vehicle.get("last_update")) * MS_PER_SEC);

        return vehiclePosition.build();
    }

    private float getFloat(Object object) throws ClassCastException {
        if (object instanceof BigDecimal bigDecimal) {
            return bigDecimal.floatValue();
        }
        BigInteger bigInteger = (BigInteger) object;
        return bigInteger.floatValue();
    }

    private long getTimestamp(String lastUpdate) {
        ZoneId targetZoneId = ZoneId.of("Europe/Warsaw");
        Instant instant = Instant.parse(lastUpdate);
        ZonedDateTime zonedDateTime = instant.atZone(targetZoneId);

        return zonedDateTime.toEpochSecond();
    }
}
