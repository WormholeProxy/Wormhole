package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class DisconnectPacket extends Packet {
    public static final short ID = 18;

    String reason;

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
