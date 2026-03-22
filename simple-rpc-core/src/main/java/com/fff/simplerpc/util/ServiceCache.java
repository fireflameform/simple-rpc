package com.fff.simplerpc.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.fff.simplerpc.registry.ServiceDiscovery;
import com.fff.simplerpc.registry.nacos.NacosServiceDiscovery;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceCache {
    private final Map<String, List<Instance>> serviceCache;

    private final ServiceDiscovery serviceDiscovery;

    public ServiceCache() {
        this.serviceCache = new ConcurrentHashMap<>();
        this.serviceDiscovery = new NacosServiceDiscovery();
    }

    //设置interfaceName对应的实例列表
    public void getInstances(String interfaceName) throws NacosException {
        List<Instance> instances = serviceDiscovery.lookupService(interfaceName);
        serviceCache.put(interfaceName, instances);
    }

    //更新缓存（直接替换）
    public void updateCache(String interfaceName, List<Instance> instances) throws NacosException {
        serviceCache.put(interfaceName, instances);
    }

    //删除实例
    public void deleteInstance(String interfaceName, Instance instance) throws NacosException {
        List<Instance> instances = serviceCache.get(interfaceName);
        if (instances != null) {
            instances.remove(instance);
        }
    }

    //定时同步（全量拉取）
}
