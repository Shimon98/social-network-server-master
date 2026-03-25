package com.socialNetwork.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SocialNetworkServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialNetworkServerApplication.class, args);
    }

}
