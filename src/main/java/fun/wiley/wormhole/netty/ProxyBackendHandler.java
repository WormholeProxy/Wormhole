package fun.wiley.wormhole.netty;

import fun.wiley.wormhole.PlayerConnection;
import fun.wiley.wormhole.Wormhole;
import fun.wiley.wormhole.packets.Packet;
import io.netty.channel.*;

public class ProxyBackendHandler extends ChannelInboundHandlerAdapter {
    private final Channel inboundChannel;

    public ProxyBackendHandler(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (!inboundChannel.isActive()) {
            Wormhole.closeOnFlush(ctx.channel());
        } else {
            ctx.read();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Wormhole.connections.get(inboundChannel.id()).handlePacketFromServer((Packet) msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // TODO: Should be able to tell when the server closed unexpectedly
        PlayerConnection connection = Wormhole.connections.get(inboundChannel.id());
        if (connection != null) {
            connection.connectToFallback(ctx.channel());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Wormhole.LOGGER.error("Error from proxy backend", cause);
        Wormhole.closeOnFlush(ctx.channel());
    }
}
