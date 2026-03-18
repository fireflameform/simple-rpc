package com.fff.remote.dto;

import com.fff.remote.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId;

    private T result;

    private int code;

    private String message;

    public static <T> RpcResponse<T> success(String requestId, T result) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setResult(result);
        response.setCode(CodeEnum.SUCCESS.getCode());
        return response;
    }

    public static <T> RpcResponse<T> fail(CodeEnum codeEnum) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(codeEnum.getCode());
        response.setMessage(codeEnum.getDesc());
        return response;
    }

    public static <T> RpcResponse<T> fail(int code, String message) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
