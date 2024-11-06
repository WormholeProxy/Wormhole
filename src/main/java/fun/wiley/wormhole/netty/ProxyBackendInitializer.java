package fun.wiley.wormhole.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ProxyBackendInitializer extends ChannelInitializer<SocketChannel> {
    private final Channel inboundChannel;

    public ProxyBackendInitializer(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(
                new PacketDecoder(),
                new ProxyBackendHandler(inboundChannel),
                new PacketEncoder()
        );
    }
}
