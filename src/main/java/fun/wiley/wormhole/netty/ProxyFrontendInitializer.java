package fun.wiley.wormhole.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ProxyFrontendInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(
                new PacketDecoder(),
                new ProxyFrontendHandler(),
                new PacketEncoder()
        );
    }
}
