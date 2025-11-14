package pl.goeuropa.tc_helper.service;

import com.google.transit.realtime.GtfsRealtime;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.goeuropa.tc_helper.configs.ApiProperties;
import pl.goeuropa.tc_helper.gtfsrt.GtfsRealTimeVehicleFeed;
import pl.goeuropa.tc_helper.model.Assignment;
import pl.goeuropa.tc_helper.repository.VehicleRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilesService {

    private final static String FILE_PATH = "./.back.up";

    private final VehicleRepository vehicleRepository = VehicleRepository.getInstance();
    private final ApiProperties properties;

    @Scheduled(fixedRateString = "${api.refresh-interval:15}",
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
                log.debug("Write to file: {} entities.", feed.getEntityList().size());

            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }

    @PostConstruct
    public void getFromBackupFile() {
        Map<String, List<Assignment>> probablyJavaData;
        try {
            var dataFromFile = new FileInputStream(FILE_PATH);
            var objectFromFile = new ObjectInputStream(dataFromFile);

            if (objectFromFile == null) return;

                probablyJavaData = (ConcurrentHashMap<String, List<Assignment>>) objectFromFile.readObject();
                if (probablyJavaData != null && probablyJavaData instanceof Map) {
                    vehicleRepository.setSegregatedAssignments((
                            ConcurrentHashMap<String, List<Assignment>>) probablyJavaData);

                    log.info("{} - assignments from file is added to repository.",
                            probablyJavaData.values().stream()
                                    .mapToInt(List::size)
                                    .sum());
                }
            objectFromFile.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @PreDestroy
    public void saveToBackFile() {
        var segregatedAssignments = vehicleRepository.getSegregatedAssignments();
        try {
            var toFile = new FileOutputStream(FILE_PATH);
            var objectToFile = new ObjectOutputStream(toFile);
            objectToFile.writeObject(segregatedAssignments);

            log.info(" Successfully save {} assignments to back-up file : {}",
                    segregatedAssignments.values().stream()
                            .mapToInt(List::size)
                            .sum(),
                    FILE_PATH);

            objectToFile.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
