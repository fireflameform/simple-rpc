package testServer;

import com.fff.remote.transport.netty.server.RpcServer;

public class Test {
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();
        rpcServer.register("UserService", new UserServiceImpl());
        rpcServer.start();
    }
}
