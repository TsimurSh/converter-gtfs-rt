package pl.goeuropa.websocketconverter.cache;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.goeuropa.websocketconverter.models.Vehicle;

import java.util.HashMap;
import java.util.Map;

@Getter
@Slf4j
@Component
public class CacheManager{

    private final Map<Integer, Vehicle> vehicleCacheMap = new HashMap<>();

    public void add(Vehicle newUpdate) {
        log.info("-- Add a vehicle's updates: {}", newUpdate);
        vehicleCacheMap.put(newUpdate.getVehicleId(), newUpdate);
    }
}
