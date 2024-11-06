package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class EndTickPacket extends Packet {
    long worldTick;

    @Override
    public short getId() {
        return 5;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeLong(worldTick);
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        worldTick = in.readLong();
    }
}
