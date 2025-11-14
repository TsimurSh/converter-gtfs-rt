package pl.goeuropa.tc_helper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.goeuropa.tc_helper.model.Assignment;
import pl.goeuropa.tc_helper.model.dto.AssignmentDto;
import pl.goeuropa.tc_helper.service.VehicleUpdatesService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class VehicleUpdatesController {

    private final VehicleUpdatesService service;

    @GetMapping("/api/v1/vehicles/positions")
    public String getPositionsByAgency(@RequestParam("agency") String agency) {
        String asText = service.getVehiclePositions(agency);
        log.info("Get feed message include {} lines", asText.lines()
                .count());
        return asText;
    }

    @PostMapping("/api/v1/vehicles")
    public String putAllAssignments(
            @RequestBody AssignmentDto assignments,
            @RequestParam("to") String to) {
        try {
            if (to.equals("BlockAssignments")) return service.addAllAssignments(assignments);
            log.info("Receive {} assignments with key {}", assignments.getAssignmentsList().size(), assignments.getKey());
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
        throw new IllegalArgumentException("Check URI or body JSON what you sent");
    }

    @GetMapping("/api/v1/vehicles/assignments")
    public List<Assignment> getAssignmentsByAgency(@RequestParam("agency") String agency) {
        log.info("Get assignments for agency {}", agency);
        return service.getAssignmentsByAgency(agency);
    }

    @PostMapping("/api/v1/vehicles/assignments")
    public String manualRetryToSendAssignments(@RequestBody String agency) {
        try {
            String regex = ":\\s*\"([^\"]+)";
            Matcher matcher = Pattern.compile(regex).matcher(agency);
            if (matcher.find()) {
                log.info("Resend assignments to agency: {}", matcher.group(1).toUpperCase());
                return service.sendAssignmentsToAgency(matcher.group(1));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
        throw new IllegalArgumentException("Check body JSON what you sent");
    }
}
