package pl.goeuropa.converter.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pl.goeuropa.converter.configs.ApiProperties;
import pl.goeuropa.converter.repository.VehicleRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalTeamClient {

    private final ApiProperties properties;
    private final RestClient restClient;
    private final VehicleRepository repository = VehicleRepository.getInstance();



    @Scheduled(fixedDelay = 5_000)
    public void getDataFromGlobalteam() {
        try {
            var response = restClient.get()
                    .uri("?{uriParam}=" +
                            properties.getTokens().get("sroda"),
                            properties.getUriParam())
                    .retrieve()
                    .body(String.class);
            JSONParser jsonObjectData = new JSONParser(response);

            var data = (LinkedHashMap) jsonObjectData.object().get("data");

            repository.getVehiclesList().get("").setList((List<Map<String, Object>>) data.get("units"));
            log.debug("Get {} objects with vehicle locations", ((List<?>) data.get("units")).size());

        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
