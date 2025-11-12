package pl.goeuropa.tc_helper.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.goeuropa.tc_helper.dto.AssignmentDto;
import pl.goeuropa.tc_helper.service.VehicleUpdatesService;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class VehicleUpdatesController {

    private final VehicleUpdatesService service;

    @GetMapping("/api/v1/command/vehicles/{agency}")
    public String get(@PathVariable("agency") String agency, @RequestParam("f") String f) {
        String asText = "Chose a format";
        if (f.equals("text")) asText = service.getVehiclePositions(agency);

        log.info("Get feed message include {} lines", asText.lines()
                .count());
        return asText;
    }

    @PostMapping("/api/v1/command/vehicles")
    public String putAllAssignments(
            @RequestBody AssignmentDto assignments,
            @RequestParam("to") String to) {
        try {
            if (to.equals("BlockAssignments")) return service.addAllAssignments(assignments).toString();
            log.trace("Receive {} assignments with key {}", assignments.getAssignmentsList().size(), assignments.getKey());
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        } throw new IllegalArgumentException("Check URI or body JSON what you sent");
    }
}
