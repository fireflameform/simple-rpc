package com.fff.remote.transport.netty.codec;

import com.fff.remote.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RpcMessageDecoder extends ByteToMessageDecoder {
    
    private final Serializer serializer;
    private final Class<?> genericClass;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        
        in.markReaderIndex();
        int dataLength = in.readInt();
        
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        
        Object obj = serializer.deserialize(data, genericClass);
        out.add(obj);
    }
}