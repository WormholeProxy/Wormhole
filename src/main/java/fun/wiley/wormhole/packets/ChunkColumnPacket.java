package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;

public class ChunkColumnPacket extends Packet {
    ByteBuf data;

    @Override
    public short getId() {
        return 15;
    }

    @Override
    public ByteBuf encode() {
        return data;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        data = in.readBytes(packetSize);
    }
}
