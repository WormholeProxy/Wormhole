package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ZonePacket extends Packet {
    public static final short ID = 15;

    boolean setDefault;
    String jsonString;

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeBoolean(setDefault);
        writeString(buf, jsonString);
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        setDefault = in.readBoolean();
        jsonString = readString(in);
    }
}
