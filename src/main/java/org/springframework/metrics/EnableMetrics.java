package org.springframework.metrics;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MetricsConfiguration.class)
public @interface EnableMetrics {

    String[] basePackages() default {};

}
