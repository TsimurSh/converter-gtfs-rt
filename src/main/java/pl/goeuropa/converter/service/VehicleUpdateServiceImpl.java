package pl.goeuropa.converter.service;

import com.google.transit.realtime.GtfsRealtime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.goeuropa.converter.gtfsrt.GtfsRealTimeVehicleFeed;
import pl.goeuropa.converter.repository.VehicleRepository;

@Slf4j
@Service
public class VehicleUpdateServiceImpl implements VehicleUpdateService {

    private final VehicleRepository vehicleRepository = VehicleRepository.getInstance();

    @Value("${api.time-zone}")
    private String timeZone;

    @Override
    public String getVehiclePositions() {
        try {
            GtfsRealtime.FeedMessage feed = new GtfsRealTimeVehicleFeed()
                    .create(vehicleRepository.getVehiclesList(), timeZone);
            log.info("Get : {} entities.", feed.getEntityList().size());

            return feed.toString();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ex.getMessage() + ex.getCause();
        }
    }
}
