# 宠物殡葬管理系统 - 后端

基于 Spring Boot 3.2 + MyBatis Plus + MySQL 的后端服务。

## 技术栈

- **框架**: Spring Boot 3.2
- **ORM**: MyBatis Plus 3.5
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **认证**: JWT
- **文档**: SpringDoc OpenAPI (Swagger)

## 项目结构

```
backend/
├── src/main/java/com/petfuneral/
│   ├── common/           # 通用类
│   │   ├── exception/    # 异常处理
│   │   └── response/     # 统一响应
│   ├── config/           # 配置类
│   ├── controller/       # 控制器
│   ├── dto/              # 数据传输对象
│   ├── entity/           # 实体类
│   ├── mapper/           # MyBatis Mapper
│   ├── security/         # 安全相关
│   ├── service/          # 服务层
│   └── util/             # 工具类
├── src/main/resources/
│   ├── mapper/           # MyBatis XML
│   ├── application.yml   # 配置文件
│   └── schema.sql        # 数据库脚本
└── pom.xml
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis (可选)

### 2. 初始化数据库

```bash
# 登录 MySQL
mysql -u root -p

# 执行初始化脚本
source src/main/resources/schema.sql
```

### 3. 修改配置

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pet_funeral
    username: your_username
    password: your_password
```

### 4. 运行项目

```bash
# 使用 Maven
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/pet-funeral-backend-1.0.0.jar
```

### 5. 访问接口

- API 文档: http://localhost:8080/swagger-ui.html
- 健康检查: http://localhost:8080/actuator/health

## API 接口

### 认证相关

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/v1/auth/login | 管理员登录 |
| POST | /api/v1/auth/init | 初始化管理员 |

### 订单管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/v1/orders | 分页查询订单 |
| GET | /api/v1/orders/{id} | 获取订单详情 |
| POST | /api/v1/orders | 创建订单 |
| POST | /api/v1/orders/{id}/confirm | 确认订单 |
| POST | /api/v1/orders/{id}/cancel | 取消订单 |
| POST | /api/v1/orders/{id}/complete | 完成订单 |

### 纪念馆

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/v1/memorials | 分页查询纪念馆 |
| GET | /api/v1/memorials/{id} | 获取纪念馆详情 |
| POST | /api/v1/memorials | 创建纪念馆 |
| POST | /api/v1/memorials/{id}/candle | 点烛 |
| POST | /api/v1/memorials/{id}/flower | 献花 |

### 公开接口 (无需登录)

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/v1/public/packages | 获取套餐列表 |
| GET | /api/v1/public/memorial/{urlKey} | 访问纪念馆 |

## 默认账号

初始化后默认管理员账号：

- 用户名: `admin`
- 密码: `123456`

## 开发说明

### 添加新接口

1. 在 `entity/` 创建实体类
2. 在 `mapper/` 创建 Mapper 接口
3. 在 `service/` 创建 Service 类
4. 在 `controller/` 创建 Controller 类
5. 在 `dto/` 创建 DTO 类（如需要）

### 数据库迁移

建议使用 Flyway 或 Liquibase 管理数据库版本。
