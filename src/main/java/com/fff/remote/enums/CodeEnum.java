package com.fff.remote.enums;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeEnum {
    SUCCESS(1, "成功"),
    FAIL(999, "失败"),;

    private final int code;

    private final String desc;


}
