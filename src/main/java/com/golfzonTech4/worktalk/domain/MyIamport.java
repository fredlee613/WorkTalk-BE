package com.golfzonTech4.worktalk.domain;

import com.siot.IamportRestClient.IamportClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MyIamport {

    @Value("${iamport.key}")
    String key;
    @Value("${iamport.secret}")
    String secret;

    @Bean
    public IamportClient getClient() {
        return new IamportClient(key, secret);
    }
}
