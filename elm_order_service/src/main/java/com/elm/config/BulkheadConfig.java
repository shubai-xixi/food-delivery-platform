package com.elm.config;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.spring6.bulkhead.configure.BulkheadAspect;
import io.github.resilience4j.spring6.bulkhead.configure.BulkheadAspectExt;
import io.github.resilience4j.spring6.bulkhead.configure.BulkheadConfigurationProperties;
import io.github.resilience4j.spring6.fallback.FallbackExecutor;
import io.github.resilience4j.spring6.spelresolver.SpelResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BulkheadConfig {

    @Bean
    public BulkheadAspect bulkheadAspect(
            BulkheadConfigurationProperties properties,
            ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry,
            BulkheadRegistry bulkheadRegistry,
            @Autowired(required = false) List<BulkheadAspectExt> aspectExts,
            FallbackExecutor fallbackExecutor,
            SpelResolver spelResolver) {
        return new BulkheadAspect(
                properties,
                threadPoolBulkheadRegistry,
                bulkheadRegistry,
                aspectExts != null ? aspectExts : List.of(),
                fallbackExecutor,
                spelResolver
        );
    }
}