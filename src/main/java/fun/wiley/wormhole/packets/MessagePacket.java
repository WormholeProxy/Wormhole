package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class MessagePacket extends Packet {
    public String playerUniqueId;
    public String message;

    public MessagePacket() {}

    public MessagePacket(String playerUniqueId, String message) {
        this.playerUniqueId = playerUniqueId;
        this.message = message;
    }

    public MessagePacket(String message) {
        this.playerUniqueId = "";
        this.message = message;
    }

    @Override
    public short getId() {
        return 11;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        writeString(buf, message);
        writeString(buf, playerUniqueId);
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        this.message = readString(in);
        this.playerUniqueId = readString(in);
    }
}
