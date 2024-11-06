package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PlayerPacket extends Packet {
    String accountPrefix;
    String accountString;
    String playerString;
    boolean justJoined;

    @Override
    public short getId() {
        return 10;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        writeString(buf, accountPrefix);
        writeString(buf, accountString);
        writeString(buf, playerString);
        buf.writeBoolean(justJoined);
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        accountPrefix = readString(in);
        accountString = readString(in);
        playerString = readString(in);
        justJoined = in.readBoolean();
    }

    public String getPlayerString() {
        return playerString;
    }

    public String getAccountString() {
        return accountString;
    }
}
