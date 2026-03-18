package com.fff.springConfig;


import com.fff.remote.enums.RoleEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rpc")
@Data
public class RpcProperties {

    private ServerConfig server = new ServerConfig();

    private ClientConfig client = new ClientConfig();

    private RegistryConfig registry = new RegistryConfig();

    private SerializerConfig serializer = new SerializerConfig();

    private RoleEnum role;

    @Data
    public static class ServerConfig {
        private String host = "127.0.0.1";
        private int port = 9999;
    }

    @Data
    public static class ClientConfig {
        private int connectTimeout = 5000;
        private int invokeTimeout = 5000;
    }

    @Data
    public static class RegistryConfig {
        private String type = "nacos";

        private NacosConfig nacos = new NacosConfig();

    }

    @Data
    public static class NacosConfig {
        private String serverAddr = "127.0.0.1:8848";
        /**
         * Nacos命名空间
         */
        private String namespace = "public";

        /**
         * Nacos分组
         */
        private String group = "DEFAULT_GROUP";

        /**
         * Nacos用户名
         */
        private String username;

        /**
         * Nacos密码
         */
        private String password;
    }

    @Data
    public static class SerializerConfig {
        private String type = "kryo";

    }
}
