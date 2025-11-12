package pl.goeuropa.tc_helper.service;

import com.google.transit.realtime.GtfsRealtime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.goeuropa.tc_helper.client.TransitclockClient;
import pl.goeuropa.tc_helper.configs.ApiProperties;
import pl.goeuropa.tc_helper.dto.Assignment;
import pl.goeuropa.tc_helper.dto.AssignmentDto;
import pl.goeuropa.tc_helper.gtfsrt.GtfsRealTimeVehicleFeed;
import pl.goeuropa.tc_helper.repository.VehicleRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleUpdatesServiceImpl implements VehicleUpdatesService {

    private final VehicleRepository vehicleRepository = VehicleRepository.getInstance();

    private final TransitclockClient tcClient;
    private final ApiProperties properties;


    @Override
    public String getVehiclePositions(String department) {
        try {
            GtfsRealtime.FeedMessage feed = new GtfsRealTimeVehicleFeed()
                    .create(vehicleRepository.getVehiclesList()
                                    .get(department)
                                    .getList(),
                            properties.getTimeZone());
            log.info("Get : {} entities.", feed.getEntityList().size());

            return feed.toString();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ex.getMessage();
        }
    }

    @Override
    public String addAllAssignments(AssignmentDto assignments) {
        final String KEY = assignments.getKey();
        int amount = 0;
        try {
            vehicleRepository.getAssignments().put(KEY, assignments);

            List<Assignment> assignmentsList = vehicleRepository.getAssignments().get(KEY).getAssignmentsList();
            amount = assignmentsList.size();

            log.info("Added {} assignments to repository.", amount);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    for (String agency : properties.getTcBaseUrls().keySet()) {

                        var list = assignmentsList.stream()
                                .filter(assignment -> {
                                    return vehicleRepository.getVehiclesList()
                                            .get(agency)
                                            .getList()
                                            .stream()
                                            .anyMatch(vehicle -> vehicle.get("number")
                                                    .equals(assignment.getVehicleId()
                                                    ));
                                }).toList();
                        if (list.isEmpty()) continue;
                        vehicleRepository.getSegregatedAssignments().put(agency, list);
                        tcClient.sendAssignments(agency, new AssignmentDto(KEY, list));
                    }
                }
            }).start();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return String.format("Added %d assignments to repository", amount);
    }
}
