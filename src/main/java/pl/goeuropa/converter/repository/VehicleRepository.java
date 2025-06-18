package pl.goeuropa.converter.repository;

import lombok.Data;
import pl.goeuropa.converter.dto.VehicleDto;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class VehicleRepository {

    private VehicleRepository() {
    }

    private static final VehicleRepository singleton =
            new VehicleRepository();

    public static VehicleRepository getInstance() {
        return singleton;
    }

    private List<VehicleDto> vehiclesList = new CopyOnWriteArrayList<>();
}
