package org.springframework.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableAutoConfiguration
@EnableMetrics
@EnableScheduling
public class Application {

	Logger log = LoggerFactory.getLogger(Application.class);

	@Collect
	private AtomicInteger counter1 = new AtomicInteger(0);

	@Scheduled(fixedDelay = 5000L)
	public void method1() {
		log.info("counter1 is now " + counter1.incrementAndGet());
	}

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
