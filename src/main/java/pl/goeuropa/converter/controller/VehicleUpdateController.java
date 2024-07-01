package pl.goeuropa.converter.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.goeuropa.converter.service.VehicleUpdateService;

import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/kombus")
public class VehicleUpdateController {

    private final VehicleUpdateService service;

    public VehicleUpdateController(VehicleUpdateService service) {
        this.service = service;
    }

    @GetMapping("/positions.text")
    public String get() {
        var asText = service.getUpdatedVehiclePositions();
        log.info("Got positions as text: {}", asText.lines()
                .collect(Collectors.joining()));
        return asText;
    }
}
