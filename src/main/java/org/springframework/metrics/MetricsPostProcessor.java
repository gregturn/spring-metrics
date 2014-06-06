package org.springframework.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;

public class MetricsPostProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(MetricsPostProcessor.class);

    private MetricsRepository metricsRepository;

    private EnableMetrics enableMetrics;

    public MetricsPostProcessor(MetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if (o.getClass().getPackage().getName().startsWith(this.getClass().getPackage().getName())) {

            final Class<?> beanType;
            if (ClassUtils.isCglibProxy(o)) {
                beanType = ClassUtils.getUserClass(o);
            } else {
                beanType = o.getClass();
            }

            for (Field field : beanType.getDeclaredFields()) {
                Collect collect = AnnotationUtils.getAnnotation(field, Collect.class);
                if (collect != null) {
                    metricsRepository.register(o, field);
                }
            }
        }

        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }
}
