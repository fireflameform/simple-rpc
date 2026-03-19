package com.fff.simplerpc.protocol.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageTagEnum {
    Request(0x01, "请求"),
    Response(0x02, "响应"),;

    private final int code;

    private final String desc;
}
