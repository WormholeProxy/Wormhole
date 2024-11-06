package fun.wiley.wormhole;

import com.esotericsoftware.jsonbeans.JsonReader;
import com.esotericsoftware.jsonbeans.JsonValue;
import fun.wiley.wormhole.netty.ProxyBackendInitializer;
import fun.wiley.wormhole.packets.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;

import java.util.Collection;

public class PlayerConnection {

    private final Channel clientChannel;
    private Channel serverChannel;

    private boolean switched = false;
    private boolean loaded = false;

    private PlayerPositionPacket lastPosition;
    private String displayName;
    private String currentServer;

    public PlayerConnection(Channel clientChannel) {
        this.clientChannel = clientChannel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCurrentServer() {
        return currentServer;
    }

    public void connectToServer(String serverName) {
        if (clientChannel == null) {
            return;
        }

        ServerInfo server = Wormhole.servers.get(serverName);
        if (server == null) {
            return;
        }

        Bootstrap b = new Bootstrap();
        b.group(clientChannel.eventLoop())
                .channel(clientChannel.getClass())
                .handler(new ProxyBackendInitializer(clientChannel))
                .option(ChannelOption.AUTO_READ, false);
        ChannelFuture f = b.connect(server.getAddress(), server.getPort());

        f.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                loaded = false;

                currentServer = serverName;

                Channel channelToClose = serverChannel;
                serverChannel = f.channel();

                if (channelToClose != null) {
                    switched = true;
                    if (channelToClose.isActive())
                        Wormhole.closeOnFlush(channelToClose);
                }

                Wormhole.LOGGER.info("Client {} connected to server: {}", clientChannel.remoteAddress(), serverChannel.remoteAddress());
                clientChannel.read();

                // Hacky way of unloading old server chunks
                // You could probably send packets with empty chunks
                // to overwrite before switching but this works fine
                if (switched) {
                    if (lastPosition != null) {
                        lastPosition.changePosition(10000, 0, 0);
                        clientChannel.writeAndFlush(lastPosition);
                    } else {
                        clientChannel.close();
                    }
                }
            } else {
                Wormhole.LOGGER.warn("Client {} failed to connect to server: {}", clientChannel.remoteAddress(), future.channel().remoteAddress());
                sendMessage("Failed to connect to server: " + serverName);
                if (serverChannel == null) {
                    Wormhole.closeOnFlush(clientChannel);
                } else if (!serverChannel.isActive()) {
                    Wormhole.closeOnFlush(clientChannel);
                }
            }
        });
    }

    public void connectToFallback(Channel closedChannel) {
        if (serverChannel == closedChannel) {
            connectToServer((String) Wormhole.servers.keySet().toArray()[0]);
            Wormhole.closeOnFlush(closedChannel);
        }
    }

    public void handlePacketFromClient(Packet packet) {
        if (serverChannel == null) {
            return;
        }
        if (!serverChannel.isActive()) {
            return;
        }

        try {
            boolean sendPacket = true;

            if (packet instanceof PlayerPositionPacket positionPacket) {
                if (loaded) {
                    lastPosition = positionPacket;
                } else {
                    sendPacket = false;
                }
            }

            if (packet instanceof CommandPacket) {
                sendPacket = !handleProxyCommand((CommandPacket) packet);
            }

            if (sendPacket) {
                serverChannel.writeAndFlush(packet).addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        clientChannel.read();
                    } else {
                        serverChannel.close();
                    }
                });
            } else {
                clientChannel.read();
            }

        } catch (Exception e) {
            Wormhole.LOGGER.error("Error while handling packet from client", e);
        }
    }

    public void handlePacketFromServer(Packet packet) {
        if (clientChannel == null) {
            return;
        }
        if (!clientChannel.isActive()) {
            return;
        }

        try {
            // Not sure when worldReceived packet should be sent so just do it after we get the zone
            if (packet instanceof ZonePacket) {
                if (switched) {
                    switched = false;
                    WorldReceivedGamePacket worldReceivedGamePacket = new WorldReceivedGamePacket();
                    serverChannel.writeAndFlush(worldReceivedGamePacket);
                }
            }

            boolean sendPacket = true;

            if (packet instanceof PlayerPacket playerPacket) {
                if (lastPosition != null) {
                    JsonReader reader = new JsonReader();
                    JsonValue value = reader.parse(playerPacket.getPlayerString());
                    JsonValue entity = value.get("entity");
                    if (!loaded) {
                        JsonValue pos = entity.get("position");
                        float x = pos.get("x").asFloat();
                        float y = pos.get("y").asFloat();
                        float z = pos.get("z").asFloat();

                        // This is bad, but I'm lazy
                        // We should really wait for chunks to load
                        Thread.sleep(1000);
                        lastPosition.setPosition(x, y, z);
                        clientChannel.writeAndFlush(lastPosition);
                    }
                }
                loaded = true;

                if (displayName == null) {
                    JsonReader reader = new JsonReader();
                    JsonValue value = reader.parse(playerPacket.getAccountString());
                    displayName = value.get("profile").get("display_name").asString();
                }
            }

            if (sendPacket) {
                clientChannel.writeAndFlush(packet).addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        serverChannel.read();
                    } else {
                        clientChannel.close();
                    }
                });
            } else {
                serverChannel.read();
            }
        } catch (Exception e) {
            Wormhole.LOGGER.error("Error while handling packet from server", e);
        }
    }

    private boolean handleProxyCommand(CommandPacket packet) {
        String command = packet.getCommand();
        String[] args = packet.getArgs();
        if (command != null) {
            switch (command) {
                case "server": {
                    if (args.length == 0) {
                        sendMessage("Servers: " + Wormhole.servers.keySet());
                    } else if (args.length == 1) {
                        ServerInfo server = Wormhole.servers.get(args[0]);
                        if (server != null) {
                            connectToServer(args[0]);
                        } else {
                            sendMessage("Unable to connect to server: " + args[0]);
                        }
                    } else {
                        sendMessage("Incorrect number of arguments!");
                    }

                    return true;
                }
                case "players": {
                    Collection<PlayerConnection> playerConnections = Wormhole.connections.values();
                    StringBuilder msg = new StringBuilder("Players (" + playerConnections.size() + "):");

                    for (PlayerConnection playerConnection : playerConnections) {
                        msg.append("\n")
                                .append(playerConnection.getDisplayName())
                                .append(" in ")
                                .append(playerConnection.getCurrentServer());
                    }
                    sendMessage(msg.toString());

                    return true;
                }
                case "broadcast": {
                    MessagePacket msg = new MessagePacket("[" + displayName + "]> " + String.join(" ", args));
                    Wormhole.sendToAllPlayers(msg);
                    return true;
                }
            }
        }
        return false;
    }

    public void sendMessage(String message) {
        MessagePacket packet = new MessagePacket(message);
        sendPacket(packet);
    }

    public void sendPacket(Packet packet) {
        clientChannel.writeAndFlush(packet);
    }

    public void clientDisconnect() {
        if (serverChannel != null) {
            Wormhole.closeOnFlush(serverChannel);
        }
    }
}
