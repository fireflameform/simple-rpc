package com.fff.simplerpc.protocol.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    PROVIDER(),
    CONSUMER(),
    BOTH()
}
