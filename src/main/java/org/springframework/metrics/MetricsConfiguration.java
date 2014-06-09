package org.springframework.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.util.ClassUtils;

@Configuration
public class MetricsConfiguration implements ImportAware {

    private static final Logger log = LoggerFactory.getLogger(MetricsConfiguration.class);

    private String[] basePackages = new String[]{};

    @Override
    public void setImportMetadata(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata
                .getAnnotationAttributes(EnableMetrics.class.getCanonicalName(),
                        true));

        this.basePackages = attributes.getStringArray("basePackages");

        if (this.basePackages.length == 0) {
            this.basePackages = new String[]{ClassUtils.getPackageName(metadata.getClassName())};
        }
    }

    @Bean
    public MetricsPostProcessor metricsPostProcessor(MetricsRepository metricsRepository) {
        return new MetricsPostProcessor(metricsRepository, basePackages);
    }

    @Bean
    public MetricsRepository metricsRepository() {
        return new MetricsRepository();
    }

    @Configuration
    @ConditionalOnBean({RedisConnectionFactory.class})
    protected static class RedisMetricCollection {

        @Bean
        public MetricsPublisher publisher(RedisConnectionFactory connectionFactory) {
            return new RedisMetricsPublisher(connectionFactory);
        }
    }

    @Configuration
    @ConditionalOnMissingBean({RedisConnectionFactory.class, RabbitTemplate.class})
    protected static class InMemoryMetricCollection {

        @Bean
        public MetricsPublisher publisher() {
            return new InMemoryMetricsPublisher();
        }
    }

    @Configuration
    @ConditionalOnBean({RabbitTemplate.class})
    protected static class RabbitMetricCollection {

        @Bean
        public MetricsPublisher publisher(RabbitTemplate rabbitTemplate) {
            return new RabbitMetricsPublisher(rabbitTemplate);
        }
    }

    @Bean
    public MetricsGrabber metricsGrabber(MetricsRepository repository, MetricsPublisher publisher) {
        return new MetricsGrabber(repository, publisher);
    }

}
