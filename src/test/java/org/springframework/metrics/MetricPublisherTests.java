package org.springframework.metrics;

import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class MetricPublisherTests {

    private AnnotationConfigApplicationContext context;

    @Test
    public void redisAutoPublisherTest() throws Exception {

        this.context = new AnnotationConfigApplicationContext();
        this.context.register(RedisAutoConfiguration.class, MetricsConfiguration.class);
        this.context.refresh();
        assertNotNull(this.context.getBean(RedisMetricsPublisher.class));
        try {
            this.context.getBean(InMemoryMetricsPublisher.class);
            fail("Expected NoSuchBeanDefinitionException");
        } catch (NoSuchBeanDefinitionException e) {}
    }

    @Test
    public void inMemoryPublisherTest() {

        this.context = new AnnotationConfigApplicationContext();
        this.context.register(MetricsConfiguration.class);
        this.context.refresh();
        assertNotNull(this.context.getBean(InMemoryMetricsPublisher.class));
    }

    @Test
    public void rabbitAutoPublisherTest() throws Exception {

        this.context = new AnnotationConfigApplicationContext();
        this.context.register(RabbitAutoConfiguration.class, MetricsConfiguration.class);
        this.context.refresh();
        assertNotNull(this.context.getBean(RabbitMetricsPublisher.class));
        try {
            this.context.getBean(InMemoryMetricsPublisher.class);
            fail("Expected NoSuchBeanDefinitionException");
        } catch (NoSuchBeanDefinitionException e) {}
    }

}
