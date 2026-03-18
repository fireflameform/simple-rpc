package com.fff.remote.utils;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class ClientConnectionManager {
    public  ConcurrentHashMap<String, Channel> IP_CHANNEL_MAP;
    public  ConcurrentHashMap<Channel, String> CHANNEL_IP_MAP;

    public ClientConnectionManager() {
        IP_CHANNEL_MAP = new ConcurrentHashMap<>();
        CHANNEL_IP_MAP = new ConcurrentHashMap<>();
    }

    public  void addConnection(String ip, Channel channel) {
        IP_CHANNEL_MAP.put(ip, channel);
        CHANNEL_IP_MAP.put(channel, ip);
    }

    public  Channel getChannel(String ip) {
        return IP_CHANNEL_MAP.get(ip);
    }

    public  void clear(Channel channel) {
        String ip = CHANNEL_IP_MAP.remove(channel);
        IP_CHANNEL_MAP.remove(ip);
    }
}
