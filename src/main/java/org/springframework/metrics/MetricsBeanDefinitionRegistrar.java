package org.springframework.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

public class MetricsBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    private static final Logger log = LoggerFactory.getLogger(MetricsBeanDefinitionRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        log.info("Checking out some annotations...");

        for (String type : metadata.getAnnotationTypes()) {
            log.info("Type => " + type);
        }

        final Map<String, Object> annotationAttributes = metadata
                .getAnnotationAttributes(EnableMetrics.class.getCanonicalName(),
                        true);

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationAttributes);

        for (String basePackage : attributes.getStringArray("basePackages")) {
            log.info("Value => " + basePackage);
        }

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MetricsConfiguration.class);
        builder.addConstructorArgValue(attributes.getStringArray("basePackages"));
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        log.info(beanDefinition.toString());
        registry.registerBeanDefinition("metricsConfiguration", beanDefinition);
    }
}
