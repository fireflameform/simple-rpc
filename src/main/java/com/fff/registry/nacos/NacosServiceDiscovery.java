package com.fff.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.fff.registry.ServiceDiscovery;

import java.net.InetSocketAddress;
import java.util.List;

public class NacosServiceDiscovery implements ServiceDiscovery {

    private static final String NACOS_SERVER_ADDR = "192.168.31.56:8848";

    private final NamingService namingService;

    public NacosServiceDiscovery() {
        try {
            namingService = NamingFactory.createNamingService(NACOS_SERVER_ADDR);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) throws NacosException {
        List<Instance> instances = namingService.selectInstances(serviceName, true);

        //TODO 负载均衡
        if (!instances.isEmpty()) {
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        }
        return null;
    }
}
