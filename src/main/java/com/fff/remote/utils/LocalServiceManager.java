package com.fff.remote.utils;

import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;

@Data
public class LocalServiceManager {

    private ConcurrentHashMap<String, Object> INTERFACE_IMPL_MAP;

    public LocalServiceManager() {
        INTERFACE_IMPL_MAP = new ConcurrentHashMap<>();
    }

    public void register(String interfaceName, Object impl) {
        INTERFACE_IMPL_MAP.put(interfaceName, impl);
    }

    public Object getImpl(String interfaceName) {
        return INTERFACE_IMPL_MAP.get(interfaceName);
    }


}
