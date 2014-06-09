package org.springframework.metrics;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitMetricsPublisher implements MetricsPublisher {

    private RabbitTemplate rabbitTemplate;

    public RabbitMetricsPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(MetricReport metricReport) {
        rabbitTemplate.convertAndSend(metricReport.getMetricName(), metricReport.getValueRead());
    }
}
