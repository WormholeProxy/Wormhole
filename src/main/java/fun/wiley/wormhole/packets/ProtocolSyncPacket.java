package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;

public class ProtocolSyncPacket extends Packet {

    static class SubPacket {
        public SubPacket(String packetName, int packetId) {
            this.packetName = packetName;
            this.packetId = packetId;
        }

        String packetName;
        int packetId;
    }

    String gameVersion;
    ArrayList<SubPacket> subPackets = new ArrayList<>();

    @Override
    public short getId() {
        return 1;
    }

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
