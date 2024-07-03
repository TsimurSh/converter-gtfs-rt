package pl.goeuropa.converter.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

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

    private List<Map<String, Object>> vehiclesList;

}
