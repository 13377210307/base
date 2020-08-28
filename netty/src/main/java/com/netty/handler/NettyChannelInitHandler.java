package com.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Qualifier("NettyChannelInitHandler")
public class NettyChannelInitHandler extends ChannelInitializer<io.netty.channel.socket.SocketChannel> {

    @Autowired
    private WebsocketHandler websocketHandler;

    @Override
    protected void initChannel(io.netty.channel.socket.SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 编码解码器
        pipeline.addLast(new HttpServerCodec());

        // 处理大数据
        pipeline.addLast(new HttpObjectAggregator(64*1024));

        pipeline.addLast(new ChunkedWriteHandler());

        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 设置空闲识别
        pipeline.addLast(new IdleStateHandler(60,60,120, TimeUnit.SECONDS));

        pipeline.addLast(websocketHandler);
    }
}
