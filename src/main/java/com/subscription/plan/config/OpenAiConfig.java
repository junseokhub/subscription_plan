package com.subscription.plan.config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {
    @Value("${open.ai.key}")
    private String openAiKey;

    @Bean
    public OpenAIClient client() {
        return OpenAIOkHttpClient.builder()
                .apiKey(openAiKey)
                .build();
    }
}
