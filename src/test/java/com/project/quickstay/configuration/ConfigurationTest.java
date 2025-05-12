package com.project.quickstay.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
public class ConfigurationTest {


    @Test
    public void test() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

    }
}
