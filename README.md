# Simple RPC Framework

一个基于Netty的高性能、轻量级RPC框架，支持服务注册发现和Spring Boot自动配置。

## 🏗️ 项目结构

```
simpleRPC/
├── simple-rpc-core/                          # RPC核心模块
│   ├── src/main/java/com/fff/simplerpc/
│   │   ├── protocol/                         # 协议层
│   │   │   ├── dto/                          # 数据传输对象
│   │   │   │   ├── RpcRequest.java
│   │   │   │   └── RpcResponse.java
│   │   │   ├── enums/                        # 枚举定义
│   │   │   │   ├── CodeEnum.java
│   │   │   │   ├── MessageTagEnum.java
│   │   │   │   └── RoleEnum.java
│   │   │   └── serialize/                    # 序列化模块
│   │   │       ├── JsonSerializer.java
│   │   │       ├── KryoSerializer.java
│   │   │       └── Serializer.java
│   │   ├── proxy/                            # 代理层
│   │   │   └── RpcProxyFactory.java
│   │   ├── registry/                         # 注册中心
│   │   │   ├── nacos/
│   │   │   │   ├── NacosServiceDiscovery.java
│   │   │   │   └── NacosServiceRegistry.java
│   │   │   ├── ServiceDiscovery.java
│   │   │   └── ServiceRegistry.java
│   │   ├── transport/                        # 传输层
│   │   │   └── RpcRequestTransport.java
│   │   └── util/                             # 工具类
│   │       ├── ClientConnectionManager.java
│   │       └── LocalServiceManager.java
│   └── pom.xml
├── simple-rpc-spring-boot-starter/           # Spring Boot Starter
│   ├── src/main/java/com/fff/simplerpc/springboot/
│   │   ├── annotation/                       # 注解定义
│   │   │   └── RpcService.java
│   │   ├── autoconfigure/                    # 自动配置
│   │   │   ├── RpcClientAutoConfiguration.java
│   │   │   ├── RpcServerAutoConfiguration.java
│   │   │   └── RpcServerProcessor.java
│   │   ├── properties/                       # 配置属性
│   │   │   └── RpcProperties.java
│   │   └── starter/                          # 启动器
│   │       └── RpcServerStarter.java
│   ├── src/main/resources/META-INF/
│   │   ├── spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
│   │   └── additional-spring-configuration-metadata.json
│   └── pom.xml
├── simple-rpc-examples/                      # 示例模块
│   ├── example-api/                          # API定义模块
│   │   ├── src/main/java/com/fff/example/api/
│   │   │   └── UserService.java
│   │   └── pom.xml
│   ├── example-consumer/                     # 消费者示例（非Spring Boot）
│   │   ├── src/main/java/com/fff/example/consumer/
│   │   │   └── Test.java
│   │   └── pom.xml
│   ├── example-provider/                     # 提供者示例（非Spring Boot）
│   │   ├── src/main/java/com/fff/example/provider/
│   │   │   └── Test.java
│   │   └── pom.xml
│   ├── example-spring-boot-consumer/         # Spring Boot消费者示例
│   │   ├── src/main/resources/application.yml
│   │   └── pom.xml
│   ├── example-spring-boot-provider/         # Spring Boot提供者示例
│   │   ├── src/main/resources/application.yml
│   │   └── pom.xml
│   └── pom.xml
├── .gitignore
└── pom.xml                                   # 父项目POM
```

## 📋 技术栈

### 核心框架
- **Java 8** - 基础开发语言
- **Netty 4.1.42.Final** - 高性能网络通信框架
- **Spring Boot 2.7.18** - 自动配置和依赖管理

### 服务注册与发现
- **Nacos 2.2.3** - 服务注册中心和配置中心

### 序列化技术
- **Kryo 4.0.2** - 高性能二进制序列化
- **Gson 2.8.9** - JSON序列化支持

### 开发工具
- **Lombok 1.18.30** - 简化Java Bean开发
- **Guava 30.1.1-jre** - Google工具库

### 日志系统
- **SLF4J 1.7.25** - 日志门面

## 🚀 快速开始

### 1. 环境要求
- JDK 8+
- Maven 3.6+
- Nacos Server 2.0+

### 2. 配置Nacos
修改示例模块中的`application.yml`：

```yaml
spring:
  application:
    name: provider

rpc:
  server:
    port: 9999
    host: 127.0.0.1
  registry:
    nacos:
      server-addr: localhost:8848
      namespace: public
      group: DEFAULT_GROUP
      username: nacos
      password: nacos
```

### 3. Spring Boot方式启动

#### 服务提供者
```java
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUserInfo(String name) {
        return new User(name, 18);
    }
}
```

#### 服务消费者
```java
public class Test {
    @Autowired
    private RpcProxyFactory rpcProxyFactory;

    @Test
    public void testUserService() {
        UserService userService = rpcProxyFactory.createProxy(UserService.class);
        User user = userService.getUserInfo("test");
        System.out.println(user);
    }
}
```

### 4. 传统方式启动

#### 服务提供者
```java
public class Test {
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();
        rpcServer.register("UserService", new UserServiceImpl());
        rpcServer.start();
    }
}
```

#### 服务消费者
```java
public class Test {
    public static void main(String[] args) {
        RpcProxyFactory rpcProxyFactory = new RpcProxyFactory();
        UserService userService = rpcProxyFactory.createProxy(UserService.class);
        User user = userService.getUserInfo("test");
        System.out.println(user);
    }
}
```

## 🔧 核心特性

### 高性能网络通信
- 基于Netty的异步非阻塞IO
- 自定义编解码器，支持多种序列化协议
- TCP长连接和心跳机制

### 服务治理
- 基于Nacos的服务注册发现
- 支持服务分组和版本控制
- 内置负载均衡策略

### Spring Boot集成
- 自动配置和条件装配
- 配置属性绑定和验证
- Starter方式快速集成

### 序列化支持
- 可插拔的序列化策略
- 支持Kryo（高性能）和JSON（可读性）
- 消息头包含序列化协议标识

### 消息协议
```
+----------------+----------------+----------------+----------------+
| 消息类型(1字节) | 序列化协议(1字节) | 数据长度(4字节) |   数据内容(N字节)  |
+----------------+----------------+----------------+----------------+
```

## 📝 许可证

MIT License

## 🤝 贡献

欢迎提交Issue和Pull Request来改进这个项目。