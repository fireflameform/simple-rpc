package com.fff.remote.serialize;

public interface Serializer {
    
    byte[] serialize(Object obj);
    
    <T> T deserialize(byte[] bytes, Class<T> clazz);
    
    int getCode();
    
    static Serializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}