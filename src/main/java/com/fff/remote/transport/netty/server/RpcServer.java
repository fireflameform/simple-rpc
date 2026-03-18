package com.fff.remote.transport.netty.server;

import com.fff.registry.ServiceRegistry;
import com.fff.registry.nacos.NacosServiceRegistry;
import com.fff.remote.utils.LocalServiceManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
@Setter
public class RpcServer {

    private String host = "127.0.0.1";

    private int port = 9999;

    private final ServiceRegistry registry;

    private static final LocalServiceManager manager = new LocalServiceManager();

    public RpcServer() {
        registry = new NacosServiceRegistry();
    }


    public void start() {
        //启动一个netty服务器，监听并处理消息
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ChannelFuture startFuture = serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RpcServerInitializer(manager))
                    .bind("localhost", port)
                    .sync();
            log.info("netty服务器正常开启");
            startFuture.channel().closeFuture().sync();
            log.info("netty服务器关闭");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.info("关闭eventLoop");
        }
    }

    public void register(String interfaceName, Object service) {
        //本地注册
        manager.register(interfaceName, service);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", port);
        //注册中心注册
        registry.registerService(interfaceName, inetSocketAddress);
    }

}
