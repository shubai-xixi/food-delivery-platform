package com.elm.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.*;

public class WeightedRoundRobinLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final ObjectProvider<ServiceInstanceListSupplier> supplierProvider;
    private final Map<String, Integer> weights;
    private final Map<String, Integer> currentWeights;

    public WeightedRoundRobinLoadBalancer(
            ObjectProvider<ServiceInstanceListSupplier> supplierProvider,
            String serviceId,
            Map<String, Integer> weights) {
        this.supplierProvider = supplierProvider;
        this.weights = weights;
        this.currentWeights = new HashMap<>();
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = supplierProvider.getIfAvailable();
        if (supplier == null) {
            return Mono.just(new EmptyResponse());
        }

        return supplier.get().next().map(instances -> {
            if (instances.isEmpty()) {
                return new EmptyResponse();
            }

            // 1. 每轮开始，所有实例加上自己的初始权重
            for (ServiceInstance instance : instances) {
                String key = String.valueOf(instance.getPort());
                currentWeights.putIfAbsent(key, 0);
                currentWeights.put(key, currentWeights.get(key) + weights.getOrDefault(key, 1));
            }

            // 2. 选当前权重最大的
            ServiceInstance selected = null;
            int maxWeight = -1;
            for (ServiceInstance instance : instances) {
                String key = String.valueOf(instance.getPort());
                int current = currentWeights.get(key);
                if (current > maxWeight) {
                    maxWeight = current;
                    selected = instance;
                }
            }

            // 3. 被选中的减去总权重
            int totalWeight = weights.values().stream().mapToInt(Integer::intValue).sum();
            String selectedKey = String.valueOf(selected.getPort());
            currentWeights.put(selectedKey, currentWeights.get(selectedKey) - totalWeight);

            System.out.println("[加权轮询] 选中端口: " + selected.getPort() + " | 当前权重: " + currentWeights);
            return new DefaultResponse(selected);
        });
    }
}