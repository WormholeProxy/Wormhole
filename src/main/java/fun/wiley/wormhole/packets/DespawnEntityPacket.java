package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class DespawnEntityPacket extends Packet {
    public static final short ID = 36;

    long time;
    int rand;
    int num;

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeLong(time);
        buf.writeInt(rand);
        buf.writeInt(num);
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        time = in.readLong();
        rand = in.readInt();
        num = in.readInt();
    }
}
