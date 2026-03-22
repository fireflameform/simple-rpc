package com.fff.simplerpc.transport.api;


import com.fff.simplerpc.protocol.dto.RpcRequest;

public interface RpcClient {
    Object sendRPCRequest(RpcRequest rpcRequest);
}
