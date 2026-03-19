package com.fff.simplerpc.protocol.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId;

    private String interfaceName;

    private String methodName;

    private Object[] params;

    private Class<?>[] paramTypes;

    private String returnType;

    private String version;

    private String group;
    
}
