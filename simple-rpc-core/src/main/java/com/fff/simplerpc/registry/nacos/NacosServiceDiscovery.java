package com.fff.simplerpc.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.fff.simplerpc.registry.ServiceDiscovery;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

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

    private final Map<String, EventListener> listeners;

    public NacosServiceDiscovery() {
        listeners = new ConcurrentHashMap<>();
        try {
            namingService = NamingFactory.createNamingService(serverAddr);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Instance> lookupService(String serviceName) throws NacosException {

        return namingService.selectInstances(serviceName, true);
    }

    @Override
    public void subscribe(String serviceName, Consumer<String> consumer) {
        if (listeners.containsKey(serviceName)) {
            return;
        }
        EventListener nacosListener = event -> {
            consumer.accept(serviceName);
        };
        try {
            namingService.subscribe(serviceName, nacosListener);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
