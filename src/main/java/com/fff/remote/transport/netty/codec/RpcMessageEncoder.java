package com.fff.remote.transport.netty.codec;

import com.fff.remote.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RpcMessageEncoder extends MessageToByteEncoder<Object> {
    
    private final Serializer serializer;
    private final Class<?> genericClass;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (genericClass.isInstance(msg)) {
            byte[] data = serializer.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}