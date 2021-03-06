package com.netty.handler;

import com.netty.utils.RandomName;
import com.netty.utils.RedisUtil;
import io.netty.channel.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("websocketHandler")
@ChannelHandler.Sharable
public class WebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Autowired
    private RedisUtil redisUtil;

    // 活跃用户
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("用户 "+redisUtil.get(channel.id().toString())+"在线");
    }

    // 非活跃用户
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("用户 "+redisUtil.get(channel.id().toString())+"掉线");
    }

    // 连接成功
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        String username = new RandomName().getRandomName();

        Channel inComing = ctx.channel();
        //遍历所有通道
        for (Channel channel : channels) {
            // 将信息写入到通道中
            channel.writeAndFlush(new TextWebSocketFrame("[新用户："+username+"]：加入群聊"));
        }
        // 将通道id作为key，用户名作为value保存在redis
        this.redisUtil.set(inComing.id().toString(),username);
        // 将该通道放到通道集合中
        channels.add(ctx.channel());
    }

    // 断开
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 根据通道id从redis中获取用户名
        String username  = (String)this.redisUtil.get(ctx.channel().id().toString());
        for (Channel channel: channels) {
            channel.writeAndFlush(new TextWebSocketFrame("[用户："+username+"]："+"退出群聊"));
        }
        // 删除redis中保存的通道信息
        this.redisUtil.del(String.valueOf(ctx.channel().id()));
        // 将该通道放到通道集合中
        channels.add(ctx.channel());
    }

    // 读取数据并进行转发
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame text) throws Exception {
        // 获取通道
        Channel inComing = ctx.channel();
        // 获取用户名称
        String username = (String)this.redisUtil.get(String.valueOf(inComing.id()));
        // 遍历所有通道
        for (Channel channel : channels) {
            // 判断本身通道与其他通道
            if (channel != inComing) {
                // 其他通道
                channel.writeAndFlush(new TextWebSocketFrame("["+username+"]："+text.text()));
            }else {
                // 本身通道
                channel.writeAndFlush(new TextWebSocketFrame("[自己]："+text.text()));
            }
        }
    }

    // 空闲处理
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            // evt向下转型IdleStateEvent
            IdleStateEvent event = (IdleStateEvent) evt;

            Integer eventType = 0;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = 1;
                    //eventType = "对方长时间未读您的消息，需要提醒他吗？";
                    break;
                case WRITER_IDLE:
                    eventType = 2;
                    //eventType = "对方长时间未回复消息，需要提醒他吗？";
                    break;
                case ALL_IDLE:
                    eventType = 3;
                    //eventType = "对方长时间未读或回复您的消息，可能下线了";
                    break;
                default:
                    eventType = 0;
                    break;
            }
            // 从redis中获取用户信息
            String username = (String)this.redisUtil.get(String.valueOf(ctx.channel().id()));

            // 遍历通道
            for (Channel channel : channels) {
                if (ctx.channel() != channel) {
                    String text = "";
                    // 两个通道不一致
                    if (1 == eventType) {
                        // 未读
                        text = username+" 长时间未读您的消息，亲亲，这边建议您打个电话催一下喔";
                    }
                    if(2 == eventType) {
                        // 未写
                        text = username+" 长时间未回复消息，亲亲，这边建议您直接拉黑喔";
                    }
                    if (3 == eventType){
                        // 未读未写
                        text = username + " 长时间未读或回复您的消息，亲亲，这边建议您下次见他的时候带个砖头喔";
                    }
                    channel.writeAndFlush(new TextWebSocketFrame("[系统提醒]："+text));
                }else {
                    System.out.println("您已长时间未操作群聊");
                }
            }
        }
    }

    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        // 根据用户id在redis中查询
        System.out.println("用户 "+redisUtil.get(channel.id().toString())+"出现异常，已关闭连接");
        cause.printStackTrace();
        ctx.close();
    }

}
