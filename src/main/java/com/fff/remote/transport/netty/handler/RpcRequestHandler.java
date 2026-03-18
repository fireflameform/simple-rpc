package com.fff.remote.transport.netty.handler;

import com.fff.remote.dto.RpcRequest;
import com.fff.remote.dto.RpcResponse;
import com.fff.remote.utils.LocalServiceManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final LocalServiceManager localServiceManager;

    public RpcRequestHandler(LocalServiceManager serviceManager) {
        localServiceManager  = serviceManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {

        log.info("收到信息，访问接口:{}",rpcRequest.getInterfaceName());
        // 通过接口名获取对应的实例
        Object impl = localServiceManager.getImpl(rpcRequest.getInterfaceName());
        Class<?> targetClass = impl.getClass();
        Method declaredMethod = targetClass.getDeclaredMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        Object result = declaredMethod.invoke(impl, rpcRequest.getParams());
        RpcResponse<Object> response = RpcResponse.success(rpcRequest.getRequestId(), result);
        ctx.writeAndFlush(response);
    }
}
