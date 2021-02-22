package com.f0x1d.foxbin;

import com.f0x1d.foxbin.utils.RandomStringGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FoxbinConfiguration {

    @Bean
    public RandomStringGenerator accessTokenGenerator() {
        return RandomStringGenerator.getInstance();
    }
}
