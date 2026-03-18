package com.fff.springConfig;


import com.fff.registry.ServiceDiscovery;
import com.fff.registry.nacos.NacosServiceDiscovery;
import com.fff.remote.transport.netty.client.RpcClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConditionalOnMissingBean(RpcClient.class)
@ConditionalOnExpression(
        "'${rpc.role}'.equalsIgnoreCase('client') " +
                "or '${rpc.role}'.equalsIgnoreCase('both')"
)
public class RpcClientAutoConfiguration {

    @Bean
    public NacosServiceDiscovery nacosServiceDiscovery(RpcProperties rpcProperties) {
        RpcProperties.NacosConfig nacos = rpcProperties.getRegistry().getNacos();

        NacosServiceDiscovery serviceDiscovery = new NacosServiceDiscovery();
        serviceDiscovery.setServerAddr(nacos.getServerAddr());
        serviceDiscovery.setNamespace(nacos.getNamespace());
        serviceDiscovery.setGroup(nacos.getGroup());
        serviceDiscovery.setUsername(nacos.getUsername());
        serviceDiscovery.setPassword(nacos.getPassword());
        return serviceDiscovery;
    }

    @Bean
    public RpcClient rpcClient(RpcProperties rpcProperties, ServiceDiscovery serviceDiscovery) {
        RpcClient rpcClient = new RpcClient(serviceDiscovery);
        RpcProperties.ClientConfig client = rpcProperties.getClient();
        rpcClient.setConnectTimeOut(client.getConnectTimeout());
        rpcClient.setInvokeTimeOut(client.getInvokeTimeout());
        return rpcClient;
    }
}
