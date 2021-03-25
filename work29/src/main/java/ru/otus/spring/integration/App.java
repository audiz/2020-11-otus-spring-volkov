package ru.otus.spring.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;

import org.springframework.integration.config.EnableIntegration;

import ru.otus.spring.integration.hrm.HrMeter;


@Slf4j
@IntegrationComponentScan
@ComponentScan
@Configuration
@EnableIntegration
public class App {

    public static void main(String[] args) throws Exception {
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(App.class);
        HrMeter hrMeter = ctx.getBean(HrMeter.class);
        hrMeter.start();
    }
}
