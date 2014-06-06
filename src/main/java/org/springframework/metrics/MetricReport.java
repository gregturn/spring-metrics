package org.springframework.metrics;

public class MetricReport {

    private String metricName;
    private Object valueRead;

    public MetricReport(String metricName, Object valueRead) {
        this.metricName = metricName;
        this.valueRead = valueRead;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public Object getValueRead() {
        return valueRead;
    }

    public void setValueRead(Object valueRead) {
        this.valueRead = valueRead;
    }
}
