package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class DisconnectPacket extends Packet {
    String reason;

    @Override
    public short getId() {
        return 17;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        writeString(buf, reason);
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        this.reason = readString(in);
    }
}
