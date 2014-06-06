package org.springframework.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryMetricsPublisher implements MetricsPublisher {

    private static final Logger log = LoggerFactory.getLogger(InMemoryMetricsPublisher.class);

    @Override
    public void publish(MetricReport metricReport) {
        log.info(metricReport.getMetricName() + " measured " + metricReport.getValueRead());
    }
}
