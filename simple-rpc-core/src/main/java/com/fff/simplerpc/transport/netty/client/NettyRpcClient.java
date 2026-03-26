package com.fff.simplerpc.transport.netty.client;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.fff.simplerpc.protocol.dto.RpcRequest;
import com.fff.simplerpc.protocol.dto.RpcResponse;
import com.fff.simplerpc.protocol.serialize.KryoSerializer;
import com.fff.simplerpc.transport.api.RpcClient;
import com.fff.simplerpc.transport.netty.codec.RpcCodec;
import com.fff.simplerpc.util.ClientConnectionManager;
import com.fff.simplerpc.util.ServiceCache;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Data
public class NettyRpcClient implements RpcClient {
    private final Bootstrap bootstrap;
    private final ClientConnectionManager connectionManager;
    private final RpcResponseHandler rpcResponseHandler;
    private final Map<String, Promise<RpcResponse<?>>> promiseMap;
    private final ServiceCache serviceCache;

    private int connectTimeOut = 5000;

    private int invokeTimeOut = 5000;

    public NettyRpcClient() {
        this(new ServiceCache());
    }

    public NettyRpcClient(ServiceCache serviceCache) {
        bootstrap = new Bootstrap();
        connectionManager = new ClientConnectionManager();
        promiseMap = new ConcurrentHashMap<>();
        rpcResponseHandler = new RpcResponseHandler(promiseMap);
        this.serviceCache = serviceCache;
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeOut)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline pipeline = socketChannel.pipeline();

                        //编解码
                        pipeline.addLast(new RpcCodec(new KryoSerializer()));

                        //连接断开处理器

                        pipeline.addLast(new IdleStateHandler(0, 0, 30));

                        //业务处理
                        pipeline.addLast(rpcResponseHandler);

                    }

                });
    }

    @Override
    public Object sendRPCRequest(RpcRequest rpcRequest) {
        // 如果有连接，就直接发请求，如果没有连接则建立连接
        Instance address;

        System.out.println("寻找服务，服务名称" + rpcRequest.getInterfaceName());
        //通过注册中心寻找服务
        List<Instance> instances = serviceCache.getInstances(rpcRequest.getInterfaceName());
        if (instances == null || instances.isEmpty()) {
            throw new RuntimeException("服务列表为空");
        }
        //TODO 这里可以做负载均衡
        address = instances.get(0);

        System.out.println("找到服务" + address);
        //获取对应channel
        Channel channel = getChannel(address.getIp(), address.getPort());
        //使用promise接收结果
        DefaultPromise<RpcResponse<?>> promise = new DefaultPromise<>(channel.eventLoop());
        //将promise暂存入map，当结果返回时，将promise取出并标记为成功，即可取得结果
        promiseMap.put(rpcRequest.getRequestId(), promise);
        channel.writeAndFlush(rpcRequest);
        try {
            return promise.get(invokeTimeOut, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 如果已有连接，则直接返回，如果没有连接，则建立后返回
     * @param ip 目标ip
     * @param port 目标端口
     * @return channel
     */
    private Channel getChannel(String ip, int port) {
        String target = ip + port;
        Channel channel = connectionManager.getChannel(target);
        if (channel != null && channel.isActive()) {
            return channel;
        }
        //TODO 非active的处理

        //创建一个新连接
        try {
            ChannelFuture connectFuture = bootstrap.connect(ip, port).sync();
            channel = connectFuture.channel();
            connectionManager.addConnection(target, channel);
            return channel;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
