package pl.goeuropa.tc_helper.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import pl.goeuropa.tc_helper.configs.ApiProperties;
import pl.goeuropa.tc_helper.dto.AssignmentDto;

import java.util.Objects;

@Slf4j
@Component
public class TransitclockClient {

    private final RestClient restClient;
    private final ApiProperties properties;

    public TransitclockClient(@Qualifier("restClientTC") RestClient restClient, ApiProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    @Retryable(retryFor = HttpClientErrorException.class, maxAttempts = 5,
            backoff = @Backoff(3000))
    public void sendAssignments(String agency, AssignmentDto assignments) {

        try {
            var response = restClient.post()
                    .uri(properties.getTcBaseUrls().get(agency))
                    .body(assignments)
                    .retrieve().body(String.class);

            log.info(response);

            log.info("Post {} assignments to TC {}; retry attempt(s) number: {}",
                    assignments.getAssignmentsList().size(),
                    agency,
                    Objects.requireNonNull(RetrySynchronizationManager.getContext()).getRetryCount());
        } catch (Exception ex) {
            log.error("Something went wrong while requesting: {} ", ex.getMessage());
        }
    }

    @Recover
    public String recover(Exception ex) {
        log.error("Recover method called after all retry attempt and still getting error");
        return "Error Class :: " + ex.getClass().getName();
    }
}
