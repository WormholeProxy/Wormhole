package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PlaceBlockPacket extends Packet {
    String zoneString;
    int blockPosX, blockPosY, blockPosZ;
    String targetBlockStateString;
    @Override
    public short getId() {
        return 18;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        writeString(buf, zoneString);
        buf.writeInt(blockPosX);
        buf.writeInt(blockPosY);
        buf.writeInt(blockPosZ);
        writeString(buf, targetBlockStateString);
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        zoneString = readString(in);
        blockPosX = in.readInt();
        blockPosY = in.readInt();
        blockPosZ = in.readInt();
        targetBlockStateString = readString(in);
    }
}
