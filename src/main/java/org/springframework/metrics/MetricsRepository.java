package org.springframework.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MetricsRepository {

    private static final Logger log = LoggerFactory.getLogger(MetricsRepository.class);
    private List<Metric> registeredMetrics = new ArrayList<>();

    public void register(Object object, Field field) {
        log.info("Registering metric collection for " + object + "::" + field.getName());
        this.registeredMetrics.add(new FieldMetric(object, field));
    }

    public List<Metric> getRegisteredMetrics() {
        return registeredMetrics;
    }

    public void setRegisteredMetrics(List<Metric> registeredMetrics) {
        this.registeredMetrics = registeredMetrics;
    }
}
