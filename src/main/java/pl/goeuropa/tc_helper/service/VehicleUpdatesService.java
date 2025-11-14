package pl.goeuropa.tc_helper.service;

import pl.goeuropa.tc_helper.model.Assignment;
import pl.goeuropa.tc_helper.model.dto.AssignmentDto;

import java.util.List;

public interface VehicleUpdatesService {

    String getVehiclePositions(String agency);

    List<Assignment> getAssignmentsByAgency(String agency);

    String sendAssignmentsToAgency(String agency);

    String addAllAssignments(AssignmentDto assignments);
}
