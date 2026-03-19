# Simple RPC Framework

一个基于Netty的高性能、轻量级RPC框架，支持服务注册发现和配置管理。

## 🏗️ 项目结构

```
simpleRPC/
├── .idea/                          # IDE配置文件
│   ├── .gitignore
│   ├── ApifoxUploaderProjectSetting.xml
│   ├── encodings.xml
│   ├── misc.xml
│   ├── uiDesigner.xml
│   └── vcs.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── fff/
│       │           ├── registry/                  # 服务注册发现模块
│       │           │   ├── nacos/
│       │           │   │   ├── NacosServiceDiscovery.java
│       │           │   │   └── NacosServiceRegistry.java
│       │           │   ├── ServiceDiscovery.java
│       │           │   └── ServiceRegistry.java
│       │           ├── remote/                    # RPC核心模块
│       │           │   ├── dto/                   # 数据传输对象
│       │           │   │   ├── RpcRequest.java
│       │           │   │   └── RpcResponse.java
│       │           │   ├── enums/                 # 枚举定义
│       │           │   │   ├── CodeEnum.java
│       │           │   │   ├── MessageTagEnum.java
│       │           │   │   └── RoleEnum.java
│       │           │   ├── serialize/             # 序列化模块
│       │           │   │   ├── JsonSerializer.java
│       │           │   │   ├── KryoSerializer.java
│       │           │   │   └── Serializer.java
│       │           │   └── transport/             # 网络传输模块
│       │           │       ├── netty/
│       │           │       │   ├── client/        # 客户端实现
│       │           │       │   │   ├── RpcClient.java
│       │           │       │   │   ├── RpcProxyFactory.java
│       │           │       │   │   └── RpcResponseHandler.java
│       │           │       │   ├── codec/         # 编解码器
│       │           │       │   │   ├── RpcCodec.java
│       │           │       │   │   ├── RpcMessageDecoder.java
│       │           │       │   │   └── RpcMessageEncoder.java
│       │           │       │   ├── handler/       # 处理器
│       │           │       │   │   └── RpcRequestHandler.java
│       │           │       │   └── server/        # 服务端实现
│       │           │       │       ├── RpcServer.java
│       │           │       │       └── RpcServerInitializer.java
│       │           │       └── RpcRequestTransport.java
│       │           ├── springConfig/              # Spring配置模块
│       │           │   ├── RpcClientAutoConfiguration.java
│       │           │   ├── RpcProperties.java
│       │           │   └── RpcServerAutoConfiguration.java
│       │           └── utils/                     # 工具类
│       │               ├── ClientConnectionManager.java
│       │               └── LocalServiceManager.java
│       ├── testClient/                            # 客户端测试代码
│       │   ├── Test.java
│       │   ├── User.java
│       │   └── UserService.java
│       ├── testServer/                            # 服务端测试代码
│       │   ├── Test.java
│       │   └── UserServiceImpl.java
│       └── resources/                             # 配置文件
│           ├── application.yml
│           └── nacos-config.yml
├── .gitignore                                     # Git忽略文件
└── pom.xml                                        # Maven配置文件
```

## 📋 技术栈

### 核心框架
- **Java 8** - 基础开发语言
- **Netty 4.1.42.Final** - 高性能网络通信框架
- **Spring 5.2.7.RELEASE** - 配置管理和依赖注入

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
修改 `src/main/resources/application.yml` 中的Nacos配置：

```yaml
rpc:
  nacos:
    server-addr: localhost:8848
    namespace: 
    group: DEFAULT_GROUP
```

### 3. 启动服务端
```java
// 创建RPC服务器
RpcServer server = new RpcServer(8080);
```

### 4. 客户端调用
```java
// 创建RPC客户端
RpcClient client = new RpcClient("localhost:8848");
UserService userService = client.createProxy(UserService.class);

// 像调用本地方法一样调用远程服务
User user = userService.getUserById(1L);
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

### 配置管理
- YAML配置文件支持
- Spring配置属性绑定
- 多环境配置支持

### 序列化支持
- 可插拔的序列化策略
- 支持Kryo（高性能）和JSON（可读性）
- 消息头包含序列化协议标识

## 📊 架构设计

### 分层架构
1. **传输层**：基于Netty的网络通信
2. **序列化层**：支持多种序列化协议
3. **代理层**：动态代理实现透明调用
4. **注册中心**：服务注册与发现
5. **配置层**：统一配置管理

### 消息协议
```
+----------------+----------------+----------------+----------------+
| 消息类型(1字节) | 序列化协议(1字节) | 数据长度(4字节) |   数据内容(N字节)  |
+----------------+----------------+----------------+----------------+
```

## 🔍 扩展性

框架设计具有良好的扩展性，支持：
- 自定义序列化协议
- 自定义负载均衡策略
- 自定义服务发现机制
- 自定义配置源

## 📝 许可证

MIT License

## 🤝 贡献

欢迎提交Issue和Pull Request来改进这个项目。