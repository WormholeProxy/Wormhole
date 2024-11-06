package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class LoginPacket extends Packet {
    String accountPrefixString;
    String accountString;

    @Override
    public short getId() {
        return 3;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        writeString(buf, accountPrefixString);
        writeString(buf, accountString);
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        accountPrefixString = readString(in);
        accountString = readString(in);
    }
}
