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
        log.info("Grab another batch of metrics...");
        for (Metric metric : metricsRepository.getRegisteredMetrics()) {
            log.info("Reading " + metric.getName() + " yielded " + metric.read());
            metricsPublisher.publish(new MetricReport(metric.getName(), metric.read()));
        }
    }

}
