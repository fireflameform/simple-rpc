package com.fff.registry;

import com.alibaba.nacos.api.exception.NacosException;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {
    InetSocketAddress lookupService(String serviceName) throws NacosException;
}
