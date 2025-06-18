package pl.goeuropa.converter.gtfsrt;

import com.google.transit.realtime.GtfsRealtime;
import lombok.extern.slf4j.Slf4j;
import pl.goeuropa.converter.dto.VehicleDto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class GtfsRealTimeVehicleFeed {

    private static final int MS_PER_SEC = 1_000;

    public GtfsRealtime.FeedMessage create(List<VehicleDto> vehicles) {

        GtfsRealtime.FeedMessage.Builder message = GtfsRealtime.FeedMessage.newBuilder();
        GtfsRealtime.FeedHeader.Builder feedheader = GtfsRealtime.FeedHeader.newBuilder()
                .setGtfsRealtimeVersion("1.0")
                .setIncrementality(GtfsRealtime.FeedHeader.Incrementality.FULL_DATASET)
                .setTimestamp(System.currentTimeMillis());
        message.setHeader(feedheader);

        vehicles.forEach((vehicle) -> {
            GtfsRealtime.FeedEntity.Builder vehiclePositionEntity = GtfsRealtime.FeedEntity
                    .newBuilder()
                    .setId(String.valueOf(vehicle.getSideNumber() != null ?
                            vehicle.getSideNumber() :
                            vehicle.getId()));
            try {
                // the VehicleDto data input
                GtfsRealtime.VehiclePosition vehiclePosition = createVehiclePosition(vehicle);
                vehiclePositionEntity.setVehicle(vehiclePosition);
                message.addEntity(vehiclePositionEntity);
            } catch (Exception ex) {
                log.error("Something get wrong while parsing vehicle data", ex);
            }
        });
        log.debug("GTFS-RT successfully created");
        return message.build();
    }

    private GtfsRealtime.VehiclePosition createVehiclePosition(VehicleDto vehicle) {

        GtfsRealtime.VehiclePosition.Builder vehiclePosition = GtfsRealtime.VehiclePosition.newBuilder();
        // the Description information
        GtfsRealtime.VehicleDescriptor.Builder vehicleDescriptor = GtfsRealtime.VehicleDescriptor.newBuilder()
                .setId(String.valueOf(vehicle.getSideNumber()));
        // the Position information
        GtfsRealtime.Position.Builder position =
                GtfsRealtime.Position.newBuilder()
                        .setLatitude((float) vehicle.getLat())
                        .setLongitude((float) (vehicle.getLon()));

        vehiclePosition.setPosition(position);
        vehiclePosition.setVehicle(vehicleDescriptor);
//        vehiclePosition.setTrip(GtfsRealtime.TripDescriptor.newBuilder().setTripId(vehicle.getTrip()).build());
        vehiclePosition.setTimestamp(getTimestamp(vehicle.getLastUpdate())
        );
        return vehiclePosition.build();
    }

    private long getTimestamp(String lastUpdate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            LocalDateTime localDateTime = LocalDateTime.parse(lastUpdate, formatter);
            return localDateTime.atZone(ZoneId.of("UTC")).toEpochSecond();
        } catch (Exception ex) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(lastUpdate, formatter);
                return localDateTime.atZone(ZoneId.of("UTC")).toEpochSecond();
            } catch (Exception ex2) {
                log.warn("Something get wrong while parsing timestamp", ex2);
                throw ex2;
            }
        }
    }
}
