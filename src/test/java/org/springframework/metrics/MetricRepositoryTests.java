package org.springframework.metrics;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class MetricRepositoryTests {

    private AnnotationConfigApplicationContext context;

    @Test
    public void testFieldMetricsWithBasePackages() {

        this.context = new AnnotationConfigApplicationContext();
        this.context.register(MetricsConfiguration.class, AppWithSpeccedBasePackages.class);
        this.context.refresh();
        MetricsRepository repository = this.context.getBean(MetricsRepository.class);
        assertNotNull(repository);
        List<Metric> registeredMetrics = repository.getRegisteredMetrics();
        assertThat(registeredMetrics.size(), equalTo(1));
        Metric metric = registeredMetrics.get(0);
        assertThat(metric.getClass().getCanonicalName(), equalTo(FieldMetric.class.getCanonicalName()));
        assertThat(metric.getName(), endsWith("::counts"));
    }

    @Test
    public void testFieldMetricsByDefault() {

        this.context = new AnnotationConfigApplicationContext();
        this.context.register(MetricsConfiguration.class, App.class);
        this.context.refresh();
        MetricsRepository repository = this.context.getBean(MetricsRepository.class);
        assertNotNull(repository);
        List<Metric> registeredMetrics = repository.getRegisteredMetrics();
        assertThat(registeredMetrics.size(), equalTo(1));
        Metric metric = registeredMetrics.get(0);
        assertThat(metric.getClass().getCanonicalName(), equalTo(FieldMetric.class.getCanonicalName()));
        assertThat(metric.getName(), endsWith("::counts"));
    }

    @Test
    public void testClassMetricsByDefault() {

        this.context = new AnnotationConfigApplicationContext();
        this.context.register(MetricsConfiguration.class, ClassApp.class);
        this.context.refresh();
        MetricsRepository repository = this.context.getBean(MetricsRepository.class);
        assertNotNull(repository);
        List<Metric> registeredMetrics = repository.getRegisteredMetrics();
        assertThat(registeredMetrics.size(), equalTo(2));
        Metric metric1 = registeredMetrics.get(0);
        Metric metric2 = registeredMetrics.get(1);
        assertThat(metric1.getName(), anyOf(endsWith("upVotes"),endsWith("downVotes")));
        assertThat(metric2.getName(), anyOf(endsWith("upVotes"),endsWith("downVotes")));
    }

    @Configuration
    @EnableMetrics(basePackages = {"org.springframework.metrics"})
    protected static class AppWithSpeccedBasePackages {

        @Collect
        private int counts;

    }

    @Configuration
    @EnableMetrics
    protected static class App {

        @Collect
        private int counts;
    }

    @Configuration
    @EnableMetrics
    @Collect
    protected static class ClassApp {
        private int upVotes;
        private int downVotes;
    }
}
