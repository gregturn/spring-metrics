package org.springframework.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;

public class MetricsPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private static final Logger log = LoggerFactory.getLogger(MetricsPostProcessor.class);

    private BeanFactory beanFactory;

    private String something;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if (o.getClass().getPackage().getName().startsWith(this.getClass().getPackage().getName())) {
            log.info(o + " is in this package, so we'll check it out!");

            final Class<?> beanType;
            if (ClassUtils.isCglibProxy(o)) {
                beanType = ClassUtils.getUserClass(o);
            } else {
                beanType = o.getClass();
            }

            for (Field field : beanType.getDeclaredFields()) {
                log.info("Inspecting " + field.getName());
                Collect collect = AnnotationUtils.getAnnotation(field, Collect.class);
                if (collect != null) {
                    log.info(field.getName() + " is tagged for metric collection!");
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
