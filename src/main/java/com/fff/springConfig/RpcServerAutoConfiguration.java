package com.fff.springConfig;

import com.fff.registry.ServiceRegistry;
import com.fff.registry.nacos.NacosServiceRegistry;
import com.fff.remote.transport.netty.server.RpcServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(RpcProperties.class)
@ConditionalOnClass(RpcServer.class)
@Configuration
public class RpcServerAutoConfiguration {

    @Bean
    public NacosServiceRegistry serviceRegistry(RpcProperties rpcProperties) {
        RpcProperties.NacosConfig nacos = rpcProperties.getRegistry().getNacos();
        NacosServiceRegistry serviceRegistry = new NacosServiceRegistry();
        serviceRegistry.setServerAddr(nacos.getServerAddr());
        serviceRegistry.setNamespace(nacos.getNamespace());
        serviceRegistry.setGroup(nacos.getGroup());
        serviceRegistry.setUsername(nacos.getUsername());
        serviceRegistry.setPassword(nacos.getPassword());
        return serviceRegistry;
    }

    //注入服务器
    @Bean
    public RpcServer rpcServer(RpcProperties rpcProperties, ServiceRegistry serviceRegistry) {
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        RpcProperties.ServerConfig server = rpcProperties.getServer();
        rpcServer.setPort(server.getPort());
        rpcServer.setHost(server.getHost());
        return rpcServer;
    }

}
