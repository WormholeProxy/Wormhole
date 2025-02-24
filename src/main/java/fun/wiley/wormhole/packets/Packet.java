package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class Packet {
    public abstract ByteBuf encode();
    public abstract void decode(ByteBuf in, int packetSize);

    private static final Map<Short, Class<? extends Packet>> packetRegistry = new HashMap<>();

    static {
        registerPacket(ProtocolSyncPacket.class);
        registerPacket(LoginPacket.class);
        registerPacket(EndTickPacket.class);
        registerPacket(WorldReceivedGamePacket.class);
        registerPacket(SetNetworkSettingPacket.class);
        registerPacket(ChallengeLoginPacket.class);
        registerPacket(PlayerPacket.class);
        registerPacket(MessagePacket.class);
        registerPacket(PlayerPositionPacket.class);
        registerPacket(ZonePacket.class);
        registerPacket(ChunkColumnPacket.class);
        registerPacket(CommandPacket.class);
        registerPacket(DisconnectPacket.class);
        registerPacket(PlaceBlockPacket.class);
        registerPacket(DespawnEntityPacket.class);
    }

    public short getId() {
        try {
            Field idField = this.getClass().getField("ID");
            return idField.getShort(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve ID for class: " + this.getClass().getName(), e);
        }
    }

    private static void registerPacket(Class<? extends Packet> packetClass) {
        try {
            // Retrieve the static field "ID" from the packet class
            short id = (short) packetClass.getField("ID").get(null);
            packetRegistry.put(id, packetClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register packet: " + packetClass.getName(), e);
        }
    }

    /**
     * Factory method to create a Packet instance based on the packet ID.
     * If the ID is not found in the registry, returns an UnprocessedPacket.
     */
    public static Packet createPacket(short packetId) {
        Class<? extends Packet> packetClass = packetRegistry.get(packetId);
        if (packetClass != null) {
            try {
                // Instantiate the packet using its no-arg constructor
                return packetClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate packet for ID: " + packetId, e);
            }
        }
        // Fallback: if not found, return an UnprocessedPacket
        return new UnprocessedPacket(packetId);
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
