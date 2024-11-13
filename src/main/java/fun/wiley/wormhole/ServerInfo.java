package fun.wiley.wormhole;

public class ServerInfo {
    private String address;
    private int port;

    public ServerInfo() {
        address = "localhost";
        port = 47137;
    }

    public ServerInfo(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return address + ':' + port;
    }
}
