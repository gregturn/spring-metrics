package org.springframework.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class MetricsGrabber {

    private static final Logger log = LoggerFactory.getLogger(MetricsGrabber.class);

    private MetricsRepository metricsRepository;

    private MetricsPublisher metricsPublisher;

    public MetricsGrabber(MetricsRepository metricsRepository, MetricsPublisher metricsPublisher) {
        this.metricsRepository = metricsRepository;
        this.metricsPublisher = metricsPublisher;
    }

    @Scheduled(fixedRate = 1000L)
    public void grabMetrics() {
        for (Metric metric : metricsRepository.getRegisteredMetrics()) {
            metricsPublisher.publish(new MetricReport(metric.getName(), metric.read()));
        }
    }

}
