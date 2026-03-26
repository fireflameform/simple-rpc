package com.fff.simplerpc.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.fff.simplerpc.registry.ServiceDiscovery;
import com.fff.simplerpc.registry.nacos.NacosServiceDiscovery;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceCache {
    private final Map<String, List<Instance>> serviceCache;

    private final ServiceDiscovery serviceDiscovery;

    private final Set<String> subscribedServices;

    public ServiceCache() {
        this(new NacosServiceDiscovery());
    }

    public ServiceCache(ServiceDiscovery serviceDiscovery) {
        this.serviceCache = new ConcurrentHashMap<>();
        this.serviceDiscovery = serviceDiscovery;
        this.subscribedServices = ConcurrentHashMap.newKeySet();
    }

    //获取实例列表
    public List<Instance> getInstances(String serviceName) {
        List<Instance> instances = serviceCache.get(serviceName);
        if (instances == null) {
            try {
                instances = pullInstances(serviceName);
                if (!subscribedServices.contains(serviceName)) {
                    serviceDiscovery.subscribe(serviceName, (service) -> {
                        try {
                            pullInstances(serviceName);
                        } catch (NacosException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } catch (NacosException e) {
                throw new RuntimeException(e);
            }
        }
        return instances;
    }

    //从注册中心拉取并存入缓存
    private List<Instance> pullInstances(String serviceName) throws NacosException {
        List<Instance> instances = serviceDiscovery.lookupService(serviceName);
        serviceCache.put(serviceName, instances);
        return instances;
    }


    //TODO
    //定时同步（全量拉取）
}
