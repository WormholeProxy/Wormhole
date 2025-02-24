package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ChallengeLoginPacket extends Packet {
    public static final short ID = 8;

    String challenge;

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        writeString(buf, challenge);
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        challenge = readString(in);
    }
}
