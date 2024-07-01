package pl.goeuropa.converter.gtfsrt;

import com.google.transit.realtime.GtfsRealtime;
import lombok.extern.slf4j.Slf4j;
import pl.goeuropa.converter.models.Vehicle;
import pl.goeuropa.converter.repository.VehicleRepository;

import java.text.ParseException;
import java.util.Map;

@Slf4j
public class GtfsRealTimeVehicleFeed {

    private static final int MS_PER_SEC = 1_000;

    private GtfsRealtime.FeedMessage createMessage(Map<Integer, Vehicle> vehicles) {
        GtfsRealtime.FeedMessage.Builder message = GtfsRealtime.FeedMessage.newBuilder();

        GtfsRealtime.FeedHeader.Builder feedheader = GtfsRealtime.FeedHeader.newBuilder()
                .setGtfsRealtimeVersion("1.0")
                .setIncrementality(GtfsRealtime.FeedHeader.Incrementality.FULL_DATASET)
                .setTimestamp(System.currentTimeMillis());
        message.setHeader(feedheader);

        vehicles.forEach((key, vehicle) -> {

            GtfsRealtime.FeedEntity.Builder vehiclePositionEntity = GtfsRealtime.FeedEntity.newBuilder()
                    .setId(String.valueOf(key == null ? vehicle.getVehicleId() : key));

            try {
                // the Vehicle data input
                GtfsRealtime.VehiclePosition vehiclePosition = createVehiclePosition(vehicle);
                vehiclePositionEntity.setVehicle(vehiclePosition);
                message.addEntity(vehiclePositionEntity);
            } catch (Exception ex) {
                log.error("Error parsing vehicle data for vehicle={}", key, ex);
            }
        });
        return message.build();
    }

    private GtfsRealtime.VehiclePosition createVehiclePosition(Vehicle vehicleData) throws ParseException {

        GtfsRealtime.VehiclePosition.Builder vehiclePosition = GtfsRealtime.VehiclePosition.newBuilder();
        // the Description information
        GtfsRealtime.VehicleDescriptor.Builder vehicleDescriptor = GtfsRealtime.VehicleDescriptor.newBuilder()
                .setId(String.valueOf(vehicleData.getVehicleId()));
        // the Position information
        GtfsRealtime.Position.Builder position =
                GtfsRealtime.Position.newBuilder()
                        .setLatitude((float) vehicleData.getLatitude())
                        .setLongitude((float) vehicleData.getLongitude())
                        .setSpeed((float) vehicleData.getSpeed() / 10)
                        .setBearing(vehicleData.getHeading());

        vehiclePosition.setPosition(position);
        vehiclePosition.setVehicle(vehicleDescriptor);
        vehiclePosition.setTimestamp(vehicleData.getTimestamp() * MS_PER_SEC);

        return vehiclePosition.build();
    }

    public GtfsRealtime.FeedMessage create(VehicleRepository repository) {
        Map<Integer, Vehicle> vehicles = repository.getVehicleCacheMap();
        return createMessage(vehicles);
    }
}
