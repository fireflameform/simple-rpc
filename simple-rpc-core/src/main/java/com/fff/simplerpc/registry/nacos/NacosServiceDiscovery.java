package com.fff.simplerpc.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.fff.simplerpc.registry.ServiceDiscovery;
import lombok.Data;

import java.net.InetSocketAddress;
import java.util.List;

@Data
public class NacosServiceDiscovery implements ServiceDiscovery {

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

    public NacosServiceDiscovery() {
        try {
            namingService = NamingFactory.createNamingService(serverAddr);
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
