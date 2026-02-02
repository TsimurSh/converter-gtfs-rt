package pl.goeuropa.converter.gtfsrt;

import com.google.transit.realtime.GtfsRealtime;
import lombok.extern.slf4j.Slf4j;
import pl.goeuropa.converter.dto.VehicleDto;

import java.util.List;

@Slf4j
public class GtfsRealTimeVehicleFeed {

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
                    .setId(String.valueOf(vehicle.getK()));
            try {
                // the VehicleDto data input
                GtfsRealtime.VehiclePosition vehiclePosition = createVehiclePosition(vehicle);
                vehiclePositionEntity.setVehicle(vehiclePosition);
                message.addEntity(vehiclePositionEntity);
            } catch (Exception ex) {
                log.error("Something get wrong while parsing vehicle data", ex);
            }
        });
        log.debug("GTFS-RT successfully created: {}", message.getEntityCount());
        return message.build();
    }

    private GtfsRealtime.VehiclePosition createVehiclePosition(VehicleDto vehicle) {

        GtfsRealtime.VehiclePosition.Builder vehiclePosition = GtfsRealtime.VehiclePosition.newBuilder();
        // the Description information
        GtfsRealtime.VehicleDescriptor.Builder vehicleDescriptor = GtfsRealtime.VehicleDescriptor.newBuilder()
                .setId(vehicle.getName()
                        .concat("_")
                        .concat(String.valueOf(vehicle.getK())))
                .setLabel(vehicle.getType());
        // the Position information
        GtfsRealtime.Position.Builder position = GtfsRealtime.Position.newBuilder()
                .setLatitude(vehicle.getX())
                .setLongitude((vehicle.getY()));
        //TripDescriptor info - set route ID
        GtfsRealtime.TripDescriptor.Builder tripDescriptor = GtfsRealtime.TripDescriptor.newBuilder()
                .setRouteId(vehicle.getName());

        vehiclePosition.setPosition(position);
        vehiclePosition.setVehicle(vehicleDescriptor);
        vehiclePosition.setTrip(tripDescriptor);
        vehiclePosition.setTimestamp(System.currentTimeMillis());

        return vehiclePosition.build();
    }
}
