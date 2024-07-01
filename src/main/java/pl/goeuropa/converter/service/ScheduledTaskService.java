package pl.goeuropa.converter.service;

import com.google.transit.realtime.GtfsRealtime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.goeuropa.converter.gtfsrt.GtfsRealTimeVehicleFeed;
import pl.goeuropa.converter.repository.VehicleRepository;

import java.io.FileOutputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduledTaskService {

    private final VehicleRepository vehicleRepository;

    @Value("${api.out-path}")
    private String OUTPUT_PATH;


    @Scheduled(cron = "*/5 * * * * *")
    public void updateVehiclesPositionsProtoFile() {
        try {
            GtfsRealtime.FeedMessage feed = new GtfsRealTimeVehicleFeed().create(vehicleRepository);
            log.info("Wrote: {} entities.", feed.getEntityCount());

            //Writing to protobuf file
            var toFile = new FileOutputStream(OUTPUT_PATH);
            feed.writeTo(toFile);
            toFile.close();

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
