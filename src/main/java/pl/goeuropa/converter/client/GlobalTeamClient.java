package pl.goeuropa.converter.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Slf4j
@Component
public class GlobalTeamClient {

    private final RestClient restClient;

    @Value("${api.security.token}")
    private String TOKEN;

    public GlobalTeamClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Scheduled(fixedDelay = 5_000)
    public void getTestOfLocationGlobalteam() {
        String lastUpdateGlobalteam = "";

        try { var response = restClient.get()
                .uri("?key=" + TOKEN)
                .retrieve()
                .body(String.class);

            JSONParser jsonObjectData = new JSONParser(response);

            var data = (LinkedHashMap) jsonObjectData.object().get("data");
            var units = (ArrayList<LinkedHashMap>) data.get("units");
            lastUpdateGlobalteam = (String) units.get(0).get("last_update") + " | " +
                    units.get(0).get("lat") + " : " +
                    units.get(0).get("lng") + " " ;
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
