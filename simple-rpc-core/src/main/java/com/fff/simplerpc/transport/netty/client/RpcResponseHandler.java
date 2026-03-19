package com.fff.simplerpc.transport.netty.client;

import com.fff.simplerpc.protocol.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponse<?>> {

    private final Map<String, Promise<RpcResponse<?>>> promiseMap;

    public RpcResponseHandler(Map<String, Promise<RpcResponse<?>>> promiseMap) {
        this.promiseMap = promiseMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) {
        log.info("收到响应，原始请求id：{}", rpcResponse.getRequestId());

        Promise<RpcResponse<?>> promise = promiseMap.get(rpcResponse.getRequestId());
        if (promise != null) {
            promise.setSuccess(rpcResponse);
        }
    }

}
