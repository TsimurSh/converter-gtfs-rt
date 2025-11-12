package pl.goeuropa.tc_helper.service;

import com.google.transit.realtime.GtfsRealtime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.goeuropa.tc_helper.configs.ApiProperties;
import pl.goeuropa.tc_helper.gtfsrt.GtfsRealTimeVehicleFeed;
import pl.goeuropa.tc_helper.repository.VehicleRepository;

import java.io.FileOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final VehicleRepository vehicleRepository = VehicleRepository.getInstance();
    private final ApiProperties properties;


//    @Scheduled(fixedRateString = "${api.refresh-interval:15}",
//            timeUnit = TimeUnit.SECONDS)
    public void updateVehiclesPositionsProtoBufFile() {

        for (String key : properties.getTokens().keySet()) {
            try (FileOutputStream toFile = new FileOutputStream(
                    properties.getOutPath() + key + properties.getPostfix())) {

                GtfsRealtime.FeedMessage feed = new GtfsRealTimeVehicleFeed()
                        .create(vehicleRepository.getVehiclesList().get(key).getList(),
                                properties.getTimeZone());
                //Writing to protobuf file
                feed.writeTo(toFile);
                log.debug("Write to file: {} entities.", feed.getEntityList().size());

            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }
}
