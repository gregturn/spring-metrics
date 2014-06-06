package org.springframework.metrics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class MetricsConfiguration {

    @Bean
    public MetricsPostProcessor metricsPostProcessor(MetricsRepository metricsRepository) {
        return new MetricsPostProcessor(metricsRepository);
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
