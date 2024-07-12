package com.locationguru.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class SdkAuthenticationApplication {

    @Value("${app.timezone}")
    private String appTimezone;

    public static void main(String[] args) {
        SpringApplication.run(SdkAuthenticationApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Setting Spring Boot TimeZone
        TimeZone.setDefault(TimeZone.getTimeZone(appTimezone));
    }
}
