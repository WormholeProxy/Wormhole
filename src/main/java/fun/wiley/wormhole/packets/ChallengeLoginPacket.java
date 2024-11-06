package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ChallengeLoginPacket extends Packet {
    String challenge;

    @Override
    public short getId() {
        return 8;
    }

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
