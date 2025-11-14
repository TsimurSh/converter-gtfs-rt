package pl.goeuropa.tc_helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
@ConfigurationPropertiesScan
public class UtilsApi {

    public static void main(String[] args) {
        SpringApplication.run(UtilsApi.class, args);
    }

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }
}
