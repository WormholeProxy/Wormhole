package fun.wiley.wormhole;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {
    private Config config;
    private final File file = new File("config.yml");

    public void load() {
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Config file could not be created");
                }
                // Initialize with default config structure if file doesn't exist
                config = new Config();
                save();
                Wormhole.LOGGER.info("No config file found, creating new one");
            } else {
                Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                YamlReader yamlReader = new YamlReader(reader);
                config = yamlReader.read(Config.class);
                yamlReader.close();
                if (config == null) {
                    config = new Config();
                    Wormhole.LOGGER.warn("Config file could not be loaded, using default config");
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not load configuration!", ex);
        }

        Wormhole.LOGGER.info("Loaded configuration");
    }

    public void save() {
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            YamlWriter yamlWriter = new YamlWriter(writer);

            // Configure YamlWriter to omit class type information
            YamlConfig yamlConfig = yamlWriter.getConfig();
            yamlConfig.writeConfig.setWriteClassname(YamlConfig.WriteClassName.NEVER);
            yamlConfig.writeConfig.setWriteDefaultValues(true);

            yamlWriter.write(config);
            yamlWriter.close();
        } catch (IOException ex) {
            Wormhole.LOGGER.warn("Could not save config", ex);
        }
    }

    // Methods to access the Config properties
    public Config getConfig() {
        return config;
    }

    public static class Config {
        private int hostPort;
        private Map<String, ServerInfo> servers;

        public Config() {
            hostPort = 47137;
            servers = new HashMap<>();
            servers.put("default", new ServerInfo("localhost", 47138));
        }

        // Getters and setters
        public Map<String, ServerInfo> getServers() {
            return servers;
        }

        public void setServers(Map<String, ServerInfo> servers) {
            this.servers = servers;
        }

        public int getHostPort() {
            return hostPort;
        }

        public void setHostPort(int hostPort) {
            this.hostPort = hostPort;
        }
    }
}
