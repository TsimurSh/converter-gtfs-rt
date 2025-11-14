package pl.goeuropa.tc_helper.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

    private String uriParam;
    private String outPath;
    private String timeZone;
    private String postfix;

    private Map <String, String> tokens;
    private Map <String, String> tcBaseUrls;
}
