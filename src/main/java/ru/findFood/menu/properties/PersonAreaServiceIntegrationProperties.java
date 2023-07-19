package ru.findFood.menu.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "integrations.person-service")
@Data
public class PersonAreaServiceIntegrationProperties {

        private String url;
        private Integer connectTimeout;
        private Integer readTimeout;
        private Integer writeTimeout;


}
