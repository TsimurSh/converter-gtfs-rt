package pl.goeuropa.converter.service;

import com.google.transit.realtime.GtfsRealtime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.goeuropa.converter.configs.ApiProperties;
import pl.goeuropa.converter.gtfsrt.GtfsRealTimeVehicleFeed;
import pl.goeuropa.converter.repository.VehicleRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleUpdateServiceImpl implements VehicleUpdateService {

    private final VehicleRepository vehicleRepository = VehicleRepository.getInstance();
    private final ApiProperties properties;


    @Override
    public String getVehiclePositions(String department) {
        try {
            GtfsRealtime.FeedMessage feed = new GtfsRealTimeVehicleFeed()
                    .create(vehicleRepository.getVehiclesList()
                                    .get(department)
                                    .getList(),
                            properties.getTimeZone());
            log.info("Get : {} entities.", feed.getEntityList().size());

            return feed.toString();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ex.getMessage() + ex.getCause();
        }
    }
}
