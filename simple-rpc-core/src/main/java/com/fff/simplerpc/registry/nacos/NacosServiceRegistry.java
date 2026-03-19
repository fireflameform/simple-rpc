package com.fff.simplerpc.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import com.fff.simplerpc.registry.ServiceRegistry;

import java.net.InetSocketAddress;
import java.util.Properties;

@Slf4j
@Data
public class NacosServiceRegistry implements ServiceRegistry {

    private String serverAddr = "192.168.0.105:8848";
    /**
     * Nacos命名空间
     */
    private String namespace = "public";

    /**
     * Nacos分组
     */
    private String group = "DEFAULT_GROUP";

    /**
     * Nacos用户名
     */
    private String username = "nacos";

    /**
     * Nacos密码
     */
    private String password = "nacos";

    private final NamingService namingService;

    public NacosServiceRegistry() {
        try {
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            properties.put("namespace", namespace);
            properties.put("group", group);
            properties.put("username", username);
            properties.put("password", password);
            this.namingService = NamingFactory.createNamingService(properties);
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
