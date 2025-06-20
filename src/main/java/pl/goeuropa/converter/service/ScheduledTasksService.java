package pl.goeuropa.converter.service;

import com.google.transit.realtime.GtfsRealtime;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.goeuropa.converter.client.ODWClient;
import pl.goeuropa.converter.dto.VehicleDto;
import pl.goeuropa.converter.gtfsrt.GtfsRealTimeVehicleFeed;
import pl.goeuropa.converter.repository.VehicleRepository;

import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTasksService {

    private final ODWClient client;

    @Value("${api.out-path}")
    private String outPutPath;

    private final VehicleRepository repository = VehicleRepository.getInstance();

    @Scheduled(fixedRateString = "${api.get-interval}",
            timeUnit = TimeUnit.SECONDS)
    public void getAndParseData() {

        try {
            var csvData = client.get();

            CsvToBean<VehicleDto> csvToBean = new CsvToBeanBuilder<VehicleDto>(new StringReader(csvData))
                    .withType(VehicleDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            repository.setVehiclesList(csvToBean.parse());
            log.debug("Get : {} entities.", repository.getVehiclesList().size());
        } catch (Exception ex) {
            log.error("Something get wrong while getting and parsing data", ex);
        }
    }

    @Scheduled(fixedRateString = "${api.refresh-interval}",
            timeUnit = TimeUnit.SECONDS)
    public void updateVehiclesPositionsProtoBufFile() {
        try (FileOutputStream toFile = new FileOutputStream(
                outPutPath)) {
            GtfsRealtime.FeedMessage feed = new GtfsRealTimeVehicleFeed()
                    .create(repository.getVehiclesList());
//        Writing to protobuf file
            feed.writeTo(toFile);
            log.debug("Write to file: {} entities.", feed.getEntityList().size());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
