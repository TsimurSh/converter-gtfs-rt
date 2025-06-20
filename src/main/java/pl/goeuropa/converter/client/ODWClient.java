package pl.goeuropa.converter.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ODWClient {

    private final RestClient restClient;

    public String get() {
        var response = restClient.get()
                .retrieve()
                .body(String.class);
        log.debug("Get response has lines : {}", response.lines().count());
        return response;
    }
}
