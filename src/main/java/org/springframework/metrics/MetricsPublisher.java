package org.springframework.metrics;

public interface MetricsPublisher {

    public void publish(MetricReport metricReport);

}
