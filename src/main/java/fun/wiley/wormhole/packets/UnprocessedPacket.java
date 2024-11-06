package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;

public class UnprocessedPacket extends Packet {
    ByteBuf data;
    short id;
    public UnprocessedPacket(short id) {
        this.id = id;
    }

    @Override
    public short getId() {
        return id;
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
