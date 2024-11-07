![Wormhole Logo](images/wormhole_logo.png)

# Wormhole

**Wormhole** is a proxy server designed to seamlessly connect multiple *Cosmic Reach* servers. By routing traffic through Wormhole, players can easily transition between different servers without needing to reconnect each time.

## Features

- **Seamless Server Switching**: Allows players to switch between multiple servers without disconnecting.
- **Customizable Configuration**: Easily configure settings, server addresses, and ports through a YAML configuration file.

## Getting Started

### Prerequisites

- **Java 17+**: Make sure you have Java 17 or higher installed.

### Cloning the Repository

Clone the repository to your local machine:

```bash
git clone https://github.com/WildE-dev/Wormhole.git
cd Wormhole
```

### Building the Project

To build the project and create a runnable JAR file, run:

```bash
./gradlew build
```

This will generate a JAR in the build/libs/ directory, named ``Wormhole-<version>-all.jar``.

### Running Wormhole

Run Wormhole with the following command:

```bash
java -jar Wormhole-<version>-all.jar
```

### Configuration

The configuration file ``config.yml`` will be generated the first time Wormhole is run. You can edit this file to configure the proxy settings, such as server addresses, ports, and other options.

An example ``config.yml`` structure:
```yaml
hostPort: 47137
servers:
  lobby:
    address: localhost
    port: 47138
  game:
    address: 192.168.1.50
    port: 47139
```

#### Configuration Options
- hostPort: The port that the proxy will bind to.
- servers: A map of server names to addresses, where each server entry specifies the IP address and port.