package org.springframework.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class MetricsConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MetricsConfiguration.class);

    private String[] basePackages;

    public MetricsConfiguration(String[] basePackages) {

        this.basePackages = basePackages;
        for (String basePackage : basePackages) {
            log.info("Will scan " + basePackage + " for metrics");
        }
    }

    public String[] getBasePackages() {
        return this.basePackages;
    }

    @Bean
    public MetricsPostProcessor metricsPostProcessor(MetricsRepository metricsRepository) {
        return new MetricsPostProcessor(metricsRepository, basePackages);
    }

    @Bean
    public MetricsRepository metricsRepository() {
        return new MetricsRepository();
    }

    @Bean
    public MetricsPublisher publisher(RedisConnectionFactory connectionFactory) {
        //return new InMemoryMetricsPublisher();
        return new RedisMetricsPublisher(connectionFactory);
    }

    @Bean
    public MetricsGrabber metricsGrabber(MetricsRepository repository, MetricsPublisher publisher) {
        return new MetricsGrabber(repository, publisher);
    }

}
