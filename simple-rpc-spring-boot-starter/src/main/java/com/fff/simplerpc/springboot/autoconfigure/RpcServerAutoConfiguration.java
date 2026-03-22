package com.fff.simplerpc.springboot.autoconfigure;

import com.fff.simplerpc.registry.ServiceRegistry;
import com.fff.simplerpc.registry.nacos.NacosServiceRegistry;
import com.fff.simplerpc.springboot.properties.RpcProperties;
import com.fff.simplerpc.springboot.starter.RpcServerStarter;
import com.fff.simplerpc.transport.api.RpcServer;
import com.fff.simplerpc.transport.netty.server.NettyRpcServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(RpcProperties.class)
@ConditionalOnClass(NettyRpcServer.class)
@Configuration
@ConditionalOnExpression(
        "'${rpc.role}'.equalsIgnoreCase('provider') " +
                "or '${rpc.role}'.equalsIgnoreCase('both')"
)
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
        //由于项目只实现了以netty作为通信框架的模式，就直接初始化nettyRpcServer了，
        // 否则应该根据properties配置来选择初始化
        NettyRpcServer rpcServer = new NettyRpcServer(serviceRegistry);
        RpcProperties.ServerConfig server = rpcProperties.getServer();
        rpcServer.setPort(server.getPort());
        rpcServer.setHost(server.getHost());
        return rpcServer;
    }

    @Bean
    public RpcServerStarter rpcServerStarter(RpcServer rpcServer) {
        return new RpcServerStarter(rpcServer);
    }

    @Bean
    public RpcServerProcessor rpcServerProcessor(RpcServer rpcServer) {
        return new RpcServerProcessor(rpcServer);
    }

}
