# 宠物殡葬管理系统

> 让爱延续，让思念永恒

## 项目简介

宠物殡葬管理系统是一套完整的数字化解决方案，包含管理后台和客户端 H5，帮助宠物殡葬机构提升服务效率和客户满意度。

## 项目结构

```
pet-funeral-system/
├── admin/                 # 管理后台 (Vue 3 + Element Plus)
│   ├── src/
│   │   ├── assets/       # 静态资源
│   │   ├── components/   # 公共组件
│   │   ├── layouts/      # 布局组件
│   │   ├── router/       # 路由配置
│   │   ├── stores/       # Pinia 状态管理
│   │   ├── views/        # 页面组件
│   │   └── main.ts       # 入口文件
│   └── package.json
│
├── h5/                    # 客户端 H5 (Vue 3 + Vant)
│   ├── src/
│   │   ├── assets/       # 静态资源
│   │   ├── components/   # 公共组件
│   │   ├── layouts/      # 布局组件
│   │   ├── router/       # 路由配置
│   │   ├── views/        # 页面组件
│   │   └── main.ts       # 入口文件
│   └── package.json
│
└── README.md
```

## 技术栈

### 管理后台 (Admin)

- **框架**: Vue 3 + TypeScript
- **UI 组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **图表**: ECharts + vue-echarts
- **构建工具**: Vite 5

### 客户端 H5

- **框架**: Vue 3 + TypeScript
- **UI 组件**: Vant 4
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **构建工具**: Vite 5

## 功能模块

### 管理后台

- 🏠 工作台 - 数据概览、待办事项、今日排期
- 📋 订单中心 - 订单列表、订单详情、订单流程
- 👥 客户管理 - 客户档案、会员等级
- 🐕 宠物档案 - 宠物信息、生平记录
- 📦 服务套餐 - 套餐配置、定价管理
- 🕯️ 告别仪式 - 仪式预约、场地管理
- 🔥 火化管理 - 火化排期、记录追溯
- 📍 骨灰寄存 - 柜位管理、到期提醒
- 🏛️ 纪念馆 - 纪念馆管理、数据统计
- 🎁 纪念商城 - 商品管理、订单管理
- 📊 数据统计 - 业务趋势、客户画像
- ⚙️ 系统设置 - 基础设置、员工管理、角色权限

### 客户端 H5

- 🏠 首页 - 服务入口、热门套餐
- 📋 服务 - 套餐列表、单项服务、纪念品商城
- 📝 预约 - 在线预约服务
- 🏛️ 纪念馆 - 纪念馆列表、纪念馆详情、点烛献花
- 👤 我的 - 个人中心、订单管理
- 🔐 登录 - 手机号登录、微信登录

## 快速开始

### 管理后台

```bash
cd admin
npm install
npm run dev
```

访问 http://localhost:3000

### 客户端 H5

```bash
cd h5
npm install
npm run dev
```

访问 http://localhost:3001

## 设计规范

### 色彩

| 用途 | 色值 | 说明 |
|------|------|------|
| 主色 | #8B7355 | 温暖棕色 |
| 辅助色 | #A8C686 | 治愈绿 |
| 强调色 | #E8B89D | 柔和橙 |
| 背景色 | #FAF8F5 | 米白色 |

### 设计风格

- 温暖、治愈、有仪式感
- 避免过于悲伤的元素
- 使用柔和的圆角和渐变
- 动效轻柔，过渡自然

## 后端接口

本项目前端需要配合 Spring Boot 后端使用，后端接口文档请参考 `pet-funeral-system-requirements.md`。

## 文档

- [需求文档](../pet-funeral-system-requirements.md)
- [UI 原型图](../pet-funeral-ui-wireframes.md)

## License

MIT
