package fun.wiley.wormhole.netty;

import fun.wiley.wormhole.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        ByteBuf encodedData = packet.encode();

        int packetSize = 2 + encodedData.readableBytes();

        out.writeInt(packetSize);
        out.writeShort(packet.getId());
        out.writeBytes(encodedData);
        encodedData.release();
    }
}
