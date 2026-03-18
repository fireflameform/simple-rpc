package testClient;

import com.fff.remote.dto.RpcRequest;
import com.fff.remote.transport.netty.client.RpcClient;
import com.fff.remote.transport.netty.client.RpcProxyFactory;

public class Test {
    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient();
        RpcProxyFactory rpcProxyFactory = new RpcProxyFactory(rpcClient);
        UserService userService = rpcProxyFactory.createProxy(UserService.class);
        User alice = userService.getUserInfo("alice");
    }
}
