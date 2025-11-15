package pl.goeuropa.tc_helper.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pl.goeuropa.tc_helper.configs.ApiProperties;
import pl.goeuropa.tc_helper.model.dto.VehiclesDto;
import pl.goeuropa.tc_helper.repository.VehicleRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class GlobalTeamClient {

    private final RestClient restClient;
    private final ApiProperties properties;

    private final VehicleRepository repository = VehicleRepository.getInstance();

    public GlobalTeamClient(@Qualifier("restClientGT") RestClient restClient1, ApiProperties properties) {
        this.restClient = restClient1;
        this.properties = properties;
    }


    @Scheduled(fixedRateString = "${api.get-interval:14}",
            timeUnit = TimeUnit.SECONDS)
    public void getDataFromGlobalteam() {
        for (String key : properties.getTokens().keySet()) {
            try {
                var response = getResponse(key);

                JSONParser jsonObjectData = new JSONParser(response);

                var data = (LinkedHashMap) jsonObjectData.object().get("data");

                repository.getVehiclesList().put(key, new VehiclesDto((List<Map<String, Object>>) data.get("units")));
                log.debug("Get {} objects with vehicle locations", ((List<?>) data.get("units")).size());

            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
    }

    private String getResponse(String key) {
        final String token = properties.getTokens().get(key);
        var response = restClient.get()
                .uri("list.json?key={token}", token)
                .retrieve()
                .body(String.class);
        return response;
    }
}
