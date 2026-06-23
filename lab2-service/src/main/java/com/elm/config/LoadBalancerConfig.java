package com.elm.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.*;

@Configuration
public class LoadBalancerConfig {

    // ========== 策略1：轮询 ==========
//    @Bean
//    @ConditionalOnProperty(name = "spring.cloud.loadbalancer.strategy", havingValue = "round", matchIfMissing = true)
//    public RoundRobinLoadBalancer roundRobinLoadBalancer(LoadBalancerClientFactory factory) {
//        System.out.println(">>> 启用轮询策略（Round Robin）");
//        return new RoundRobinLoadBalancer(
//                factory.getLazyProvider("user-service", ServiceInstanceListSupplier.class),
//                "user-service"
//        );
//    }

    // ========== 策略2：随机 ==========
    @Bean
    @ConditionalOnProperty(name = "spring.cloud.loadbalancer.strategy", havingValue = "random")
    public RandomLoadBalancer randomLoadBalancer(LoadBalancerClientFactory factory) {
        System.out.println(">>> 启用随机策略（Random）");
        return new RandomLoadBalancer(
                factory.getLazyProvider("user-service", ServiceInstanceListSupplier.class),
                "user-service"
        );
    }

    // ========== 策略3：加权轮询 ==========
    @Bean
    @ConditionalOnProperty(name = "spring.cloud.loadbalancer.strategy", havingValue = "weight")
    public WeightedRoundRobinLoadBalancer weightedLoadBalancer(
            LoadBalancerClientFactory factory, Environment env) {
        System.out.println(">>> 启用加权轮询策略（Weighted Round Robin）");

        // 实例权重配置：18081=3, 18091=2
        Map<String, Integer> weightConfig = new HashMap<>();
        weightConfig.put("18081", 5);
        weightConfig.put("18091", 1);

        return new WeightedRoundRobinLoadBalancer(
                factory.getLazyProvider("user-service", ServiceInstanceListSupplier.class),
                "user-service",
                weightConfig
        );
    }
}