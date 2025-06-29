package pl.goeuropa.converter.service;

import com.google.transit.realtime.GtfsRealtime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.goeuropa.converter.gtfsrt.GtfsRealTimeVehicleFeed;
import pl.goeuropa.converter.repository.VehicleRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleUpdateServiceImpl implements VehicleUpdateService {

    private final VehicleRepository repository = VehicleRepository.getInstance();

    @Override
    public String getVehiclePositions() {
        try {
            GtfsRealtime.FeedMessage feed = new GtfsRealTimeVehicleFeed()
                    .create(repository.getVehiclesList());
            log.info("Get : {} entities.", feed.getEntityList().size());
            return feed.toString();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ex.getMessage() + ex.getCause();
        }
    }
}
