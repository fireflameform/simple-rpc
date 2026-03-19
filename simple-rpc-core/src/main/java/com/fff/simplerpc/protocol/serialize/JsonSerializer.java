package com.fff.simplerpc.protocol.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializer implements Serializer {
    
    private static final Gson gson = new GsonBuilder().create();

    @Override
    public byte[] serialize(Object obj) {
        return gson.toJson(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return gson.fromJson(new String(bytes), clazz);
    }

    @Override
    public int getCode() {
        return 1;
    }
}