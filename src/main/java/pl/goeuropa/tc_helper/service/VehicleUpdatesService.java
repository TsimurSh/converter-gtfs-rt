package pl.goeuropa.tc_helper.service;

import pl.goeuropa.tc_helper.dto.AssignmentDto;

public interface VehicleUpdatesService {

    String getVehiclePositions(String department);

    String addAllAssignments(AssignmentDto assignments);
}
