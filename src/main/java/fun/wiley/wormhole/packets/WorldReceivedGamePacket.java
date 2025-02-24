package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class WorldReceivedGamePacket extends Packet {
    public static final short ID = 6;

    @Override
    public ByteBuf encode() {
        return Unpooled.EMPTY_BUFFER;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {

    }
}
