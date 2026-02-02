package pl.goeuropa.converter.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pl.goeuropa.converter.dto.VehicleDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WrocClient {

    private final RestClient restClient;

    List<VehicleDto> list;

    public List<VehicleDto> getPositions(String body) throws ArrayIndexOutOfBoundsException {

        var response = restClient.post()
                .body(body)
                .retrieve()
                .body(new ParameterizedTypeReference<List<VehicleDto>>() {});

        if (response != null && !response.isEmpty()) list = new ArrayList<>(response);
        else throw new ArrayIndexOutOfBoundsException("Response is empty! Check the sources! 👨🏻‍💻");
        log.debug("Get response has lines : {}", list.size());
        return response;
    }
}
