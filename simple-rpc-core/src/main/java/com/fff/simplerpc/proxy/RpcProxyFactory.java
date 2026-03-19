package com.fff.simplerpc.proxy;

import com.fff.simplerpc.protocol.dto.RpcRequest;
import com.fff.simplerpc.protocol.dto.RpcResponse;
import com.fff.simplerpc.protocol.enums.CodeEnum;
import com.fff.simplerpc.transport.RpcRequestTransport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RpcProxyFactory {

    private final RpcRequestTransport rpcClient;
    public RpcProxyFactory(RpcRequestTransport rpcClient) {
        this.rpcClient = rpcClient;
    }

    @SuppressWarnings("unchecked")
    public <T> T createProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new RpcInvocationHandler(clazz));
    }


    private class RpcInvocationHandler implements InvocationHandler {

        private final Class<?> serviceClass;

        public RpcInvocationHandler(final Class<?> clazz) {
            this.serviceClass = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            // 处理Object类的方法
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            }

            RpcRequest request = RpcRequest.builder()
                    .requestId(UUID.randomUUID().toString())
                    .interfaceName(serviceClass.getSimpleName())
                    .methodName(method.getName())
                    .paramTypes(method.getParameterTypes())
                    .params(args)
                    .build();

            RpcResponse<?> response = (RpcResponse<?>) rpcClient.sendRPCRequest(request);

            if (response == null) {
                throw new RuntimeException("调用失败");
            }

            if (response.getCode() != CodeEnum.SUCCESS.getCode()) {
                throw new RuntimeException("调用失败," + response.getMessage());
            }
            return response.getResult();
        }
    }
}
