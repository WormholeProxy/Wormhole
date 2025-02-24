package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PlayerPositionPacket extends Packet {
    public static final short ID = 12;

    public String playerUniqueId;
    public String zoneId;
    public float positionX, positionY, positionZ;
    public float viewDirX, viewDirY, viewDirZ;
    public float viewDirOffX, viewDirOffY, viewDirOffZ;

    public PlayerPositionPacket() {}

    public PlayerPositionPacket(PlayerPositionPacket packet) {
        playerUniqueId = packet.playerUniqueId;
        zoneId = packet.zoneId;
        positionX = packet.positionX;
        positionY = packet.positionY;
        positionZ = packet.positionZ;
        viewDirX = packet.viewDirX;
        viewDirY = packet.viewDirY;
        viewDirZ = packet.viewDirZ;
        viewDirOffX = packet.viewDirOffX;
        viewDirOffY = packet.viewDirOffY;
        viewDirOffZ = packet.viewDirOffZ;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        writeString(buf, playerUniqueId);
        buf.writeFloat(positionX);
        buf.writeFloat(positionY);
        buf.writeFloat(positionZ);
        buf.writeFloat(viewDirX);
        buf.writeFloat(viewDirY);
        buf.writeFloat(viewDirZ);
        buf.writeFloat(viewDirOffX);
        buf.writeFloat(viewDirOffY);
        buf.writeFloat(viewDirOffZ);
        writeString(buf, zoneId);
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        playerUniqueId = readString(in);
        positionX = in.readFloat();
        positionY = in.readFloat();
        positionZ = in.readFloat();
        viewDirX = in.readFloat();
        viewDirY = in.readFloat();
        viewDirZ = in.readFloat();
        viewDirOffX = in.readFloat();
        viewDirOffY = in.readFloat();
        viewDirOffZ = in.readFloat();
        zoneId = readString(in);
    }

    public void setPosition(float x, float y, float z) {
        positionX = x;
        positionY = y;
        positionZ = z;
    }

    public void changePosition(float x, float y, float z) {
        positionX += x;
        positionY += y;
        positionZ += z;
    }
}
