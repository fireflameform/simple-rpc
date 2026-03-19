package com.fff.simplerpc.transport;


import com.fff.simplerpc.protocol.dto.RpcRequest;

public interface RpcRequestTransport {
    Object sendRPCRequest(RpcRequest rpcRequest);
}
