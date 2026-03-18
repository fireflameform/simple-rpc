package com.fff.remote.transport.netty.server;

import com.fff.remote.serialize.KryoSerializer;
import com.fff.remote.transport.netty.codec.RpcCodec;
import com.fff.remote.transport.netty.handler.RpcRequestHandler;
import com.fff.remote.utils.LocalServiceManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {

    private final LocalServiceManager serviceManager;

    public RpcServerInitializer(LocalServiceManager manager) {
        this.serviceManager = manager;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //心跳
        pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));

        //编解码
        //TODO 允许通过配置选择编解码方式
        pipeline.addLast(new RpcCodec(new KryoSerializer()));

        //处理请求
        pipeline.addLast(new RpcRequestHandler(serviceManager));
    }
}
