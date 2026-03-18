package com.fff.remote.transport.netty.codec;

import com.fff.remote.dto.RpcRequest;
import com.fff.remote.dto.RpcResponse;
import com.fff.remote.enums.MessageTagEnum;
import com.fff.remote.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RpcCodec extends MessageToMessageCodec<ByteBuf, Object> {
    
    private final Serializer serializer;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();

        //消息首部的第一个字节存储消息类型
        if (msg instanceof RpcRequest) {
            buffer.writeByte(MessageTagEnum.Request.getCode()); // 请求消息标识
        } else if (msg instanceof RpcResponse) {
            buffer.writeByte(MessageTagEnum.Response.getCode()); // 响应消息标识
        } else {
            throw new IllegalArgumentException("不支持的消息类型： " + msg.getClass());
        }

        //第二个字节存储消息的序列化方式
        buffer.writeByte(serializer.getCode()); // 序列化协议代码
        
        byte[] data = serializer.serialize(msg);
        // 用4个字节存储消息长度
        buffer.writeInt(data.length); // 数据长度
        //后续存储消息内容
        buffer.writeBytes(data); // 数据内容
        
        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        //和编码对应，第一个字节是类型，第二个字节是序列化方式，后续四个字节是数据长度
        byte messageType = msg.readByte();
        byte serializerCode = msg.readByte();
        int dataLength = msg.readInt();
        
        byte[] data = new byte[dataLength];
        msg.readBytes(data);
        
        Serializer deserializer = Serializer.getByCode(serializerCode);
        if (deserializer == null) {
            throw new IllegalArgumentException("不支持的解码方式： " + serializerCode);
        }
        
        Object result;
        if (messageType == MessageTagEnum.Request.getCode()) {
            result = deserializer.deserialize(data, RpcRequest.class);
        } else if (messageType == MessageTagEnum.Response.getCode()) {
            result = deserializer.deserialize(data, RpcResponse.class);
        } else {
            throw new IllegalArgumentException("不支持的消息类型： " + messageType);
        }
        
        out.add(result);
    }
}