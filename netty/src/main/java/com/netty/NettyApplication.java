package com.netty;

import com.netty.config.TCPServer;
import com.netty.handler.NettyChannelInitHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
@PropertySource("classpath:application.yml")  //读取application.yml文件
@EnableDiscoveryClient
public class NettyApplication {

    // 获取主机及端口信息
    @Value("${netty.host}")
    private String host;

    @Value("${netty.port}")
    private int port;

    @Autowired
    @Qualifier("NettyChannelInitHandler")
    private NettyChannelInitHandler nettyChannelInitHandler;

    /**
     * 引导类
     */
    @Bean(name="serverBootStrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 配置引导类
        serverBootstrap.group(bossGroup(),workGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(nettyChannelInitHandler);
        Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
        Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
        for (ChannelOption option : keySet) {
            serverBootstrap.option(option,tcpChannelOptions.get(option));
        }
        return serverBootstrap;
    }

    /**
     * bossGroup
     */
    @Bean(name = "bossGroup",destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(1);
    }

    /**
     * workGroup
     */
    @Bean(name = "workGroup",destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workGroup() {
        return new NioEventLoopGroup();
    }

    @Bean(name = "tcpChannelOptions")
    public Map<ChannelOption<?>, Object> tcpChannelOptions() {
        Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
        options.put(ChannelOption.SO_KEEPALIVE, true);
        options.put(ChannelOption.SO_BACKLOG, 100);
        return options;
    }

    @Bean(name = "tcpSocketAddress")
    public InetSocketAddress tcpPort() {
        return new InetSocketAddress(host,port);
    }

    public static void main(String[] args) throws Exception{
        ConfigurableApplicationContext context = SpringApplication.run(NettyApplication.class, args);
        TCPServer tcpServer = context.getBean(TCPServer.class);
        tcpServer.start();
    }
}
