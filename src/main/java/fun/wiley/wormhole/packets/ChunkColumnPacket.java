package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;

public class ChunkColumnPacket extends Packet {
    public static final short ID = 16;

    ByteBuf data;

    @Override
    public ByteBuf encode() {
        return data;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        data = in.readBytes(packetSize);
    }
}
