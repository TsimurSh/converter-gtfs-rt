package pl.goeuropa.converter.controller;


import com.google.transit.realtime.GtfsRealtime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/vehiclePositions.text", produces = {MediaType.TEXT_PLAIN_VALUE})
    public String get() {
        var asText = service.getVehiclePositions();
        log.info("Get feed message include {} lines", asText.lines()
                .count());
        return asText;
    }

    @GetMapping(value = "/vehiclePositions.pb", produces = {MediaType.APPLICATION_PROTOBUF_VALUE})
    public ResponseEntity<Object> getGtfsRealtimeVehiclePositionsFeed() {
        // Determine if output should be in human-readable format or in
        // standard binary GTFS-realtime format.

        GtfsRealtime.FeedMessage message = service.getPBVehiclePositions();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROTOBUF);
        log.info("Get feed message include {} entities", message.getEntityList().size());
        return ResponseEntity.ok()
                .headers(headers)
                .cacheControl(CacheControl.noCache())
                .body(message);
    }
}
