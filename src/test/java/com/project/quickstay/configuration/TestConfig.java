package com.project.quickstay.configuration;

import org.springframework.context.annotation.Bean;

public class TestConfig {

    @Bean
    public TestService testService() {
        System.out.println("테스트 서비스");
        return new TestService(testRepository());
    }

    @Bean
    public TestRepository testRepository() {
        System.out.println("테스트 리포지토리");
        return new TestRepository();
    }
}
