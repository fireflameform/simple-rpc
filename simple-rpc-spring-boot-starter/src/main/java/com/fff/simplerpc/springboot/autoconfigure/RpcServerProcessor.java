package com.fff.simplerpc.springboot.autoconfigure;

import com.fff.simplerpc.springboot.annotation.RpcService;
import com.fff.simplerpc.transport.netty.server.RpcServer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

@Slf4j
public class RpcServerProcessor implements BeanPostProcessor {

    private final RpcServer rpcServer;

    public RpcServerProcessor(RpcServer rpcServer) {
        this.rpcServer = rpcServer;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, @NonNull String beanName) throws BeansException {
        RpcService rpcService = AnnotationUtils.findAnnotation(bean.getClass(), RpcService.class);
        if (rpcService != null) {
            String interfaceName = resolveInterfaceName(rpcService, bean);

            rpcServer.register(interfaceName, bean);

            log.info("自动注入服务：{}", interfaceName);
        }
        return bean;
    }

    private String resolveInterfaceName(RpcService rpcService, Object bean) {
        //优先使用interfaceClass解析
        if (rpcService.interfaceClass() != null && rpcService.interfaceClass() != void.class) {
            return rpcService.interfaceClass().getName();
        }
        //使用指定的interfaceName
        if (rpcService.interfaceName() != null && !rpcService.interfaceName().isEmpty()) {
            return rpcService.interfaceName();
        }

        //取实现的第一个接口
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        if (interfaces.length > 0) {
            return interfaces[0].getSimpleName();
        }
        throw new IllegalArgumentException("无法确定接口名");
    }
}
