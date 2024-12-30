package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public abstract class Packet {
    public abstract short getId();

    // Abstract method to decode packet-specific data
    public abstract ByteBuf encode();
    public abstract void decode(ByteBuf in, int packetSize);

    // Factory method to create a Packet based on the packet ID
    public static Packet createPacket(short packetId) {
        if (packetId > 37 || packetId <= 0) {
            throw new IllegalArgumentException("Invalid packetId: " + packetId);
        }
        return switch (packetId) {
            case 1 -> new ProtocolSyncPacket();
            case 3 -> new LoginPacket();
            case 5 -> new EndTickPacket();
            case 6 -> new WorldReceivedGamePacket();
            case 7 -> new SetNetworkSettingPacket();
            case 8 -> new ChallengeLoginPacket();
            case 10 -> new PlayerPacket();
            case 11 -> new MessagePacket();
            case 12 -> new PlayerPositionPacket();
            case 14 -> new ZonePacket();
            case 15 -> new ChunkColumnPacket();
            case 16 -> new CommandPacket();
            case 17 -> new DisconnectPacket();
            case 18 -> new PlaceBlockPacket();
            case 33 -> new DespawnEntityPacket();
            default -> new UnprocessedPacket(packetId);
        };
    }

    protected String readString(ByteBuf in) {
        int length = in.readInt();
        if (length == -1) {
            return null;
        } else {
            ByteBuf strBuf = in.readBytes(length);
            String str = strBuf.toString(StandardCharsets.UTF_8);
            strBuf.release();
            return str;
        }
    }

    public static void writeString(ByteBuf buf, String str) {
        if (str == null) {
            buf.writeInt(-1);
        } else {
            byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
            buf.writeInt(strBytes.length);
            buf.writeBytes(strBytes);
        }
    }
}

