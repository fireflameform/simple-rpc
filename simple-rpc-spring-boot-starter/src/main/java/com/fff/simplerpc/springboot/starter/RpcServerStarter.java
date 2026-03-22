package com.fff.simplerpc.springboot.starter;

import com.fff.simplerpc.transport.api.RpcServer;
import com.fff.simplerpc.transport.netty.server.NettyRpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;

@Slf4j
public class RpcServerStarter implements SmartInitializingSingleton, DisposableBean {

    private final RpcServer rpcServer;

    private volatile boolean started = false;

    public RpcServerStarter(RpcServer rpcServer) {
        this.rpcServer = rpcServer;
    }

    @Override
    public void destroy() {
        if (started) {
            log.info("Spring容器关闭，停止RPC服务");
            stopServer();
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        if (!started) {
            log.info("Spring容器初始化完成，开始启动rpcServer");
            startServer();
            started = true;
        }
    }

    private void startServer() {
        Thread serverThread = new Thread(() -> {
            try {
                rpcServer.start();
            } catch (Exception e) {
                log.error("rpcServer启动失败");
            }
        }, "rpc-server-thread");
        serverThread.setDaemon(false);
        serverThread.start();
    }

    private void stopServer() {
        rpcServer.close();
        log.info("rpcServer已停止");
    }
}
