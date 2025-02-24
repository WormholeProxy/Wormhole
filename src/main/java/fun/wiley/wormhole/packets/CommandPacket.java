package fun.wiley.wormhole.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

public class CommandPacket extends Packet {
    public static final short ID = 17;

    String[] commandArgs;

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(commandArgs.length);
        for (String arg : commandArgs) {
            writeString(buf, arg);
        }
        return buf;
    }

    @Override
    public void decode(ByteBuf in, int packetSize) {
        int length = in.readInt();
        this.commandArgs = new String[length];
        for (int i = 0; i < length; i++) {
            this.commandArgs[i] = readString(in);
        }
    }

    @Override
    public String toString() {
        return "CommandPacket{" +
                "commandArgs=" + Arrays.toString(commandArgs) +
                '}';
    }

    public String getCommand(){
        if (commandArgs == null) return null;
        if (commandArgs.length == 0) return null;
        return commandArgs[0];
    }

    public String[] getArgs(){
        if (commandArgs == null) return null;
        if (commandArgs.length == 0) return null;
        String[] args = new String[commandArgs.length - 1];
        System.arraycopy(commandArgs, 1, args, 0, args.length);
        return args;
    }
}
