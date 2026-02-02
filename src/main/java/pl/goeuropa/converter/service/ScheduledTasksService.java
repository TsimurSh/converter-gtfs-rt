package pl.goeuropa.converter.service;

import com.google.transit.realtime.GtfsRealtime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.goeuropa.converter.client.WrocClient;
import pl.goeuropa.converter.dto.VehicleDto;
import pl.goeuropa.converter.gtfsrt.GtfsRealTimeVehicleFeed;
import pl.goeuropa.converter.repository.VehicleRepository;

import java.io.FileOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTasksService {

    private final WrocClient client;
    private final VehicleRepository repository = VehicleRepository.getInstance();

    @Value("${api.out-path}")
    private String outPutPath;
    @Value("${api.html-path}")
    private String htmlPath;


    @Scheduled(fixedRateString = "${api.get-interval}",
            timeUnit = TimeUnit.SECONDS)
    public void getAndParseParams() {

        try {
            var html = Jsoup.connect(htmlPath).get().outerHtml();
            Document doc = Jsoup.parse(html);

            List<String> busLines;
            List<String> tramLines;

            Elements busElements = doc.select("div.bus-gps-lines.bus li.line");
            busLines = busElements.stream().map(Element::text).toList();

            Elements tramElements = doc.select("div.bus-gps-lines.tram li.line");
            tramLines = tramElements.stream().map(Element::text).toList();

            var vehiclesList = getVehiclesDtosFromRemote(busLines, tramLines)
                    .stream()
                    .filter(VehicleDto::validateLatLon).toList();
            repository.setVehiclesList(vehiclesList);

            log.debug("Get : {} entities.", vehiclesList.size());
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
            log.info("Write to file: {} entities.", feed.getEntityList().size());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private Collection<VehicleDto> getVehiclesDtosFromRemote(List<String> busLines, List<String> tramLines) throws InterruptedException {
        StringBuilder requestBody = new StringBuilder();
        busLines.forEach(busLine -> requestBody.append("busList[bus][]=").append(busLine).append("&"));
        var responseBys = client.getPositions(requestBody.toString());
        Thread.sleep(2000);
        StringBuilder tramRequestBody = new StringBuilder();
        tramLines.forEach(tramLine -> tramRequestBody.append("busList[tram][]=").append(tramLine).append("&"));
        var responseTram = client.getPositions(tramRequestBody.toString());
        return CollectionUtils.union(responseBys, responseTram);
    }
}
