package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class WorldReceivedGamePacket extends Packet {

    @Override
    public short getId() {
        return 6;
    }

    @Override
    public ByteBuf encode() {
        return Unpooled.EMPTY_BUFFER;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {

    }
}
