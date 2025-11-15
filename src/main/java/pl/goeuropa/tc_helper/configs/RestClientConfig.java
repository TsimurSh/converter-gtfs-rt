package pl.goeuropa.tc_helper.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${api.base-url}")
    private String basePath;

    @Bean
    public RestClient restClientGT() {
        return RestClient.builder()
                .baseUrl(basePath)
                .defaultHeaders(
                        httpHeaders -> {
                            httpHeaders.set(HttpHeaders.ACCEPT,
                                    MediaType.APPLICATION_JSON_VALUE
                            );
                        })
                .build();
    }

    @Bean
    public RestClient restClientTC() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(3000);
        clientHttpRequestFactory.setConnectionRequestTimeout(3000);

        return RestClient.builder()
                .requestFactory(clientHttpRequestFactory)
                .defaultHeaders(
                        httpHeaders -> {
                            httpHeaders.set(HttpHeaders.CONTENT_TYPE,
                                    MediaType.APPLICATION_JSON_VALUE
                            );
                            httpHeaders.set(HttpHeaders.ACCEPT,
                                    MediaType.APPLICATION_JSON_VALUE
                            );
                        })
                .build();
    }
}
