package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;

public class ProtocolSyncPacket extends Packet {
    public static final short ID = 1;

    static class SubPacket {
        public SubPacket(String packetName, int packetId) {
            this.packetName = packetName;
            this.packetId = packetId;
        }

        String packetName;
        int packetId;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    String gameVersion;
    ArrayList<SubPacket> subPackets = new ArrayList<>();

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(subPackets.size());
        for (SubPacket subPacket : subPackets) {
            writeString(buf, subPacket.packetName);
            buf.writeInt(subPacket.packetId);
        }
        writeString(buf, gameVersion);
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        int numPackets = in.readInt();
        for(int i = 0; i < numPackets; ++i) {
            String packetName = this.readString(in);
            int packetId = in.readInt();

            subPackets.add(new SubPacket(packetName, packetId));
        }

        this.gameVersion = this.readString(in);
    }
}
