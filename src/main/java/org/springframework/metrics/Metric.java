package org.springframework.metrics;

public interface Metric {

    public Object read();

    public String getName();
}
