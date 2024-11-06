package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;

public class SetNetworkSettingPacket extends Packet {

    private static final Map<Integer, NetworkSettingType> SETTING_TYPES = new HashMap<>();

    String key;
    NetworkSettingType type;
    int intValue;

    @Override
    public short getId() {
        return 7;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        writeString(buf, key);
        buf.writeByte(type.id);
        switch (this.type.ordinal()) {
            case 0 -> buf.writeInt(this.intValue);
            case 1 -> buf.writeByte(this.intValue);
            default -> throw new IllegalArgumentException("Unexpected value: " + this.type);
        }
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        this.key = this.readString(in);
        byte typeId = in.readByte();
        this.type = NetworkSettingType.getSettingType(typeId);
        switch (this.type.ordinal()) {
            case 0 -> this.intValue = in.readInt();
            case 1 -> this.intValue = in.readByte();
            default -> throw new IllegalArgumentException("Unexpected value: " + this.type);
        }
    }

    enum NetworkSettingType {
        INT(0),
        BOOL(1);

        final byte id;

        NetworkSettingType(int i) {
            this.id = (byte)i;
            SETTING_TYPES.put(i, this);
        }

        static NetworkSettingType getSettingType(byte typeId) {
            return SETTING_TYPES.get((int)typeId);
        }
    }
}
