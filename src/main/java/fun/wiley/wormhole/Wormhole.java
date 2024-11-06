package fun.wiley.wormhole;

import fun.wiley.wormhole.netty.ProxyFrontendInitializer;
import fun.wiley.wormhole.packets.Packet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Wormhole {
    public static Map<ChannelId, PlayerConnection> connections = new HashMap<>();
    public static Map<String, ServerInfo> servers;

    public static final Logger LOGGER = LoggerFactory.getLogger("Wormhole");

    public static void main(String[] args) throws Exception {
        ConfigLoader conf = new ConfigLoader();
        conf.load();

        int port = conf.getConfig().getHostPort();
        servers = conf.getConfig().getServers();

        if (servers.isEmpty()) {
            LOGGER.warn("No servers configured, shutting down");
            return;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ProxyFrontendInitializer())
                    .childOption(ChannelOption.AUTO_READ, false)
                    .bind(port).addListener(future -> LOGGER.info("Proxy started on port {}", port))
                    .sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    public static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static void sendToAllPlayers(Packet packet) {
        connections.forEach((k, v) -> {
            v.sendPacket(packet);
        });
    }
}
