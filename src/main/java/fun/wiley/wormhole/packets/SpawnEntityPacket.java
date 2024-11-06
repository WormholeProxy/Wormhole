package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

@Deprecated
public class SpawnEntityPacket extends Packet {
    String entityTypeId;
    String[] strings;

    @Override
    public short getId() {
        return 31;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        writeString(buf, entityTypeId);

        for (String string : strings) {
            writeString(buf, string);
        }

        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        this.entityTypeId = this.readString(in);
        int numStrings = in.readInt();
        this.strings = new String[numStrings];
        for(int i = 0; i < numStrings; ++i) {
            this.strings[i] = readString(in);
        }
    }
}
