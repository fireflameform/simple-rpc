package com.fff.springConfig;

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
    public RpcServer rpcServer(RpcProperties rpcProperties) {
        RpcServer rpcServer = new RpcServer();


    }
}
