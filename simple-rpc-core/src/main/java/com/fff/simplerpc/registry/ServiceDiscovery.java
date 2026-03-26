package com.fff.simplerpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.function.Consumer;

public interface ServiceDiscovery {
    List<Instance> lookupService(String serviceName) throws NacosException;

    void subscribe(String serviceName, Consumer<String> consumer);
}
