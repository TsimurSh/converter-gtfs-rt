package pl.goeuropa.converter.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.goeuropa.converter.service.VehicleUpdateService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/odw")
public class VehicleUpdateController {

    private final VehicleUpdateService service;

    @GetMapping("/vehiclePositions.text")
    public String get() {
        var asText = service.getVehiclePositions();
        log.info("Get feed message include {} lines", asText.lines()
                .count());
        return asText;
    }
}
