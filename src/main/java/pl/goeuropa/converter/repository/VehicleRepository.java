package pl.goeuropa.converter.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Data
public class VehicleRepository {

    private VehicleRepository() {
    }

    private static final VehicleRepository singleton =
            new VehicleRepository();

    public static VehicleRepository getInstance() {
        return singleton;
    }

    private List<LinkedHashMap<String, Object>> vehiclesList;

//    public void add(Vehicle newUpdate) {
//        log.info("-- Add a vehicle's updates: {}", newUpdate);
//        vehicleCacheMap.put(newUpdate.getVehicleId(), newUpdate);
//    }
}
