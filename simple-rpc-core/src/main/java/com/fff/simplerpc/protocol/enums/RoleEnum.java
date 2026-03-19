package com.fff.simplerpc.protocol.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    SERVER(),
    CLIENT(),
    BOTH()
}
