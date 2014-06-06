package org.springframework.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisMetricsPublisher implements MetricsPublisher {

    private static final Logger log = LoggerFactory.getLogger(RedisMetricsPublisher.class);

    private RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;

    public RedisMetricsPublisher(RedisConnectionFactory connectionFactory) {
        this.redisTemplate = new RedisTemplate<>();
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        this.redisTemplate.setConnectionFactory(connectionFactory);
        this.redisTemplate.afterPropertiesSet();

        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public void publish(MetricReport metricReport) {
        log.info("Writing " + metricReport.getMetricName() + "'s value " + metricReport.getValueRead() + " to Redis");
        redisTemplate.boundListOps(metricReport.getMetricName()).rightPush(metricReport.getValueRead());
    }
}
