package org.springframework.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;

public class MetricsPostProcessor implements BeanPostProcessor, InitializingBean, BeanFactoryAware {

    private static final Logger log = LoggerFactory.getLogger(MetricsPostProcessor.class);

    private MetricsRepository metricsRepository;

    private BeanFactory beanFactory;

    private String[] basePackages;

    public MetricsPostProcessor(MetricsRepository metricsRepository, String[] basePackages) {

        this.metricsRepository = metricsRepository;
        this.basePackages = basePackages;

        for (String basePackage : basePackages) {
            log.info("Wills can " + basePackage);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {

        boolean validForScanning = false;

        for (String basePackage : basePackages) {

            log.info(s + " is in package " + o.getClass().getPackage().getName());
            log.info("The package we are checking against is " + basePackage);

            if (o.getClass().getPackage().getName().startsWith(basePackage)) {
                validForScanning = true;
                log.info(s + " is in " + basePackage + " so we'll register it for metric scanning.");
                break;
            }
        }

        if (validForScanning) {

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
