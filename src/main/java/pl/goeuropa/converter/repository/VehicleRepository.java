package pl.goeuropa.converter.repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.goeuropa.converter.models.Vehicle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Slf4j
@Component
public class VehicleRepository {

    private final Map<Integer, Vehicle> vehicleCacheMap = new ConcurrentHashMap<>();

    public void add(Vehicle newUpdate) {
        log.info("-- Add a vehicle's updates: {}", newUpdate);
        vehicleCacheMap.put(newUpdate.getVehicleId(), newUpdate);
    }
}
