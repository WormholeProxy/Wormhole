package fun.wiley.wormhole.netty;

import fun.wiley.wormhole.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // Check if we have enough bytes for the packet size and ID
        if (in.readableBytes() < 6) { // 4 bytes for size + 2 bytes for ID
            return;
        }

        in.markReaderIndex(); // Mark current position
        int packetSize = in.readInt();

        if (in.readableBytes() < packetSize) { // Check for complete packet
            in.resetReaderIndex();
            return;
        }

        // Read the packet ID and create the appropriate packet
        short packetId = in.readShort();
        Packet packet = Packet.createPacket(packetId);

        // Decode packet-specific data
        packet.decode(in, packetSize - 2);

        // Add the decoded packet to the output list
        out.add(packet);
    }
}
