package com.fff.simplerpc.transport.netty.server;

import com.fff.simplerpc.registry.ServiceRegistry;
import com.fff.simplerpc.registry.nacos.NacosServiceRegistry;
import com.fff.simplerpc.util.LocalServiceManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
@Data
public class RpcServer {

    private String host = "127.0.0.1";

    private int port = 9999;

    private final ServiceRegistry registry;

    private NioEventLoopGroup bossGroup = new NioEventLoopGroup();

    private NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    private static final LocalServiceManager manager = new LocalServiceManager();

    public RpcServer() {
        this(new NacosServiceRegistry());
    }

    public RpcServer(ServiceRegistry registry) {
        this.registry = registry;
    }


    public void start() {
        //启动一个netty服务器，监听并处理消息
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ChannelFuture startFuture = serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RpcServerInitializer(manager))
                    .bind(host, port)
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

    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public void register(String interfaceName, Object service) {
        //本地注册
        manager.register(interfaceName, service);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
        //注册中心注册
        registry.registerService(interfaceName, inetSocketAddress);
    }

}
