package org.springframework.metrics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {

    @Bean
    public MetricsPostProcessor metricsPostProcessor() {
        return new MetricsPostProcessor();
    }

}
