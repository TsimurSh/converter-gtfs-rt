package pl.goeuropa.converter.repository;

import lombok.Data;
import pl.goeuropa.converter.dto.VehiclesDto;

import java.util.concurrent.ConcurrentHashMap;

@Data
public class VehicleRepository {

    private VehicleRepository() {
    }

    private static final VehicleRepository singleton =
            new VehicleRepository();

    public static VehicleRepository getInstance() {
        return singleton;
    }

    private ConcurrentHashMap <String, VehiclesDto> vehiclesList;
}
