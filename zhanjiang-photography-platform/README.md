# 湛江高校大学生约拍平台

基于微信小程序的湛江高校大学生约拍平台，连接摄影师与有拍摄需求的大学生。

## 项目概述

本平台为湛江地区高校大学生提供约拍服务，包括：
- **客户端**：发布需求、搜索摄影师、预约下单、作品交付、评价分享
- **摄影师端**：个人主页展示、作品管理、接单管理、档期管理、钱包提现
- **管理端**：用户审核、内容审核、订单监控、数据统计

## 技术栈

### 后端
- **框架**: Spring Boot 3.2
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **ORM**: MyBatis Plus
- **认证**: JWT
- **文档**: Knife4j (Swagger)
- **微信SDK**: weixin-java-miniapp

### 前端
- **框架**: UniApp (Vue 3)
- **状态管理**: Pinia
- **UI风格**: 苹果简约风
- **平台**: 微信小程序

## 项目结构

```
zhanjiang-photography-platform/
├── docs/                       # 文档目录
│   ├── 需求规格说明书.md        # 需求文档
│   ├── 数据库设计文档.md        # 数据库设计
│   ├── API接口文档.md          # API文档
│   └── database_schema.sql    # 建表SQL
│
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/zhanjiang/photography/
│   │   │   │   ├── config/        # 配置类
│   │   │   │   ├── controller/    # 控制器
│   │   │   │   ├── service/       # 服务层
│   │   │   │   ├── mapper/        # 数据访问层
│   │   │   │   ├── entity/        # 实体类
│   │   │   │   ├── dto/           # 数据传输对象
│   │   │   │   ├── common/        # 公共类
│   │   │   │   └── security/      # 安全相关
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   └── pom.xml
│
└── frontend/                   # 前端项目
    ├── pages/                  # 页面
    │   ├── index/             # 首页
    │   ├── photographer/      # 摄影师相关
    │   ├── order/             # 订单相关
    │   ├── message/           # 消息
    │   ├── community/         # 社区
    │   └── mine/              # 我的
    ├── components/            # 组件
    ├── api/                   # API接口
    ├── store/                 # 状态管理
    ├── utils/                 # 工具类
    ├── styles/                # 样式
    └── static/                # 静态资源
```

## 快速开始

### 后端

1. 配置MySQL数据库，执行 `docs/database_schema.sql` 创建表

2. 修改 `backend/src/main/resources/application.yml` 配置：
   - 数据库连接信息
   - Redis连接信息
   - 微信小程序appid和secret
   - 阿里云OSS配置（可选）

3. 运行项目：
```bash
cd backend
mvn spring-boot:run
```

4. 访问API文档：`http://localhost:8080/api/v1/doc.html`

### 前端

1. 安装依赖：
```bash
cd frontend
npm install
```

2. 修改 `manifest.json` 中的微信小程序appid

3. 修改 `utils/request.js` 中的 `BASE_URL` 为后端地址

4. 开发运行：
```bash
npm run dev:mp-weixin
```

5. 使用微信开发者工具打开 `dist/dev/mp-weixin` 目录

## 功能特性

### 客户功能
- 需求发布：发布拍摄需求，让摄影师主动联系
- 摄影师发现：搜索、筛选摄影师，查看作品和评价
- 在线沟通：即时消息沟通拍摄细节
- 预约下单：选择套餐、时间，在线支付
- 订单管理：查看订单状态，改期、取消
- 作品交付：在线选片、接收精修作品
- 评价分享：评价摄影师，分享作品到社区
- 灵感社区：浏览优质作品，获取拍摄灵感
- 紧急联系：突发状况一键求助

### 摄影师功能
- 个人主页：展示个人简介、作品集、服务套餐
- 作品管理：上传、分类管理作品
- 接单管理：浏览需求、处理预约
- 日程管理：可视化管理档期
- 数据看板：查看访问量、收入等数据
- 钱包提现：查看收入，申请提现

### 平台功能
- 用户管理：实名认证、学生认证审核
- 内容审核：作品、动态、评价审核
- 订单监控：监控异常订单
- 数据统计：用户增长、订单、收入等统计
- 社区运营：话题、活动管理

## UI设计

采用苹果简约风设计风格：
- 简洁大方的界面布局
- 精致的卡片式设计
- 流畅的动画过渡
- 统一的色彩规范（主色 #007aff）
- 清晰的层次结构

## 开发团队

湛江高校大学生约拍平台开发组

## 许可证

MIT License
