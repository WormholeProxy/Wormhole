package fun.wiley.wormhole.netty;

import fun.wiley.wormhole.PlayerConnection;
import fun.wiley.wormhole.Wormhole;
import fun.wiley.wormhole.packets.*;
import io.netty.channel.*;

public class ProxyFrontendHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final Channel inboundChannel = ctx.channel();

        Wormhole.LOGGER.info("Client connected to proxy: {}", inboundChannel.remoteAddress());

        PlayerConnection playerConnection = new PlayerConnection(inboundChannel);
        Wormhole.connections.put(inboundChannel.id(), playerConnection);

        playerConnection.connectToLobby();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        final Channel inboundChannel = ctx.channel();

        Wormhole.connections.get(inboundChannel.id()).handlePacketFromClient((Packet) msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        final Channel inboundChannel = ctx.channel();
        Wormhole.LOGGER.info("Client disconnected from proxy: {}", inboundChannel.remoteAddress());

        Wormhole.connections.get(inboundChannel.id()).clientDisconnect();
        Wormhole.connections.remove(inboundChannel.id());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Wormhole.LOGGER.error("Error from proxy frontend", cause);
        Wormhole.closeOnFlush(ctx.channel());
    }
}
