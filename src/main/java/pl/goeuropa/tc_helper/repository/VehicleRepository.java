package pl.goeuropa.tc_helper.repository;

import lombok.Data;
import pl.goeuropa.tc_helper.dto.Assignment;
import pl.goeuropa.tc_helper.dto.AssignmentDto;
import pl.goeuropa.tc_helper.dto.VehiclesDto;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
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

    private ConcurrentHashMap <String, VehiclesDto> vehiclesList = new ConcurrentHashMap<>();

    private ConcurrentHashMap <String, List<Assignment>> segregatedAssignments = new ConcurrentHashMap<>();

    private Map<String, AssignmentDto> assignments = new WeakHashMap<>();
}
