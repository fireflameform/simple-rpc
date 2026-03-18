package com.fff.remote.transport;

import com.fff.remote.dto.RpcRequest;


public interface RpcRequestTransport {
    Object sendRPCRequest(RpcRequest rpcRequest);
}
