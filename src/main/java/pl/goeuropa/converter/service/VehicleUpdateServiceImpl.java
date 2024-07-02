package pl.goeuropa.converter.service;

import com.google.transit.realtime.GtfsRealtime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.goeuropa.converter.gtfsrt.GtfsRealTimeVehicleFeed;
import pl.goeuropa.converter.repository.VehicleRepository;

@Slf4j
@Service
public class VehicleUpdateServiceImpl implements VehicleUpdateService {

    private final VehicleRepository vehicleRepository = VehicleRepository.getInstance();

    @Override
    public String getVehiclePositions() {
        try {
            GtfsRealtime.FeedMessage feed = new GtfsRealTimeVehicleFeed()
                    .create(vehicleRepository.getVehiclesList());
            log.info("Wrote: {} entities.", feed.getEntityCount());

            return feed.toString();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ex.getMessage() + ex.getCause();
        }
    }
}
