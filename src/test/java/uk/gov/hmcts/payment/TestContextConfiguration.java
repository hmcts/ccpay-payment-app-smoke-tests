package uk.gov.hmcts.payment;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("uk.gov.hmcts.payment")
@PropertySource("classpath:application-${spring.profiles.active}.properties")
@EnableAutoConfiguration
public class TestContextConfiguration {
}
