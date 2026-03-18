package com.fff.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;
import com.fff.registry.ServiceRegistry;

import java.net.InetSocketAddress;

@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    private static final String NACOS_SERVER_ADDR = "192.168.31.56:8848";
    private final NamingService namingService;

    public NacosServiceRegistry() {
        try {
            this.namingService = NamingFactory.createNamingService(NACOS_SERVER_ADDR);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerService(String serviceName, InetSocketAddress address) {
        try {
            namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
            log.info("服务注册成功：{}", serviceName);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
