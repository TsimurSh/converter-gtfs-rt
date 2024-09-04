package pl.goeuropa.converter.service;

import com.google.transit.realtime.GtfsRealtime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.goeuropa.converter.configs.ApiProperties;
import pl.goeuropa.converter.gtfsrt.GtfsRealTimeVehicleFeed;
import pl.goeuropa.converter.repository.VehicleRepository;

import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final VehicleRepository vehicleRepository = VehicleRepository.getInstance();
    private final ApiProperties properties;


    @Scheduled(fixedRateString = "${api.refresh-interval}",
            timeUnit = TimeUnit.SECONDS)
    public void updateVehiclesPositionsProtoBufFile() {

        for (String key : properties.getTokens().keySet()) {
            try (FileOutputStream toFile = new FileOutputStream(
                    properties.getOutPath() + key + properties.getPostfix())) {

                GtfsRealtime.FeedMessage feed = new GtfsRealTimeVehicleFeed()
                        .create(vehicleRepository.getVehiclesList().get(key).getList(),
                                properties.getTimeZone());
                //Writing to protobuf file
                feed.writeTo(toFile);
                log.info("Write to file: {} entities.", feed.getEntityList().size());

            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }
}
