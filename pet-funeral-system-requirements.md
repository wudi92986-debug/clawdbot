# 宠物殡葬管理系统 - 需求文档

## 一、项目背景

随着人们生活水平的提高和情感需求的增加，宠物已成为许多家庭的重要成员。当宠物离世时，主人希望能给予它们体面、温馨的告别。宠物殡葬行业应运而生，但目前大多数宠物殡葬机构仍采用传统的人工管理方式，存在信息不透明、服务流程混乱、客户体验差等问题。

本系统旨在为宠物殡葬机构提供一套完整的数字化管理解决方案，提升服务效率和客户满意度。

---

## 二、系统概述

### 2.1 系统目标

- 实现宠物殡葬服务全流程数字化管理
- 为宠物主人提供便捷的在线预约和纪念服务
- 建立宠物数字化纪念平台，传递温暖与关怀
- 提升机构运营效率和服务质量

### 2.2 用户角色

| 角色 | 描述 |
|------|------|
| 系统管理员 | 系统配置、用户管理、数据统计 |
| 机构员工 | 订单处理、服务执行、客户接待 |
| 宠物主人（客户） | 预约服务、在线纪念、查看订单 |

### 2.3 技术架构

- **后端框架**：Spring Boot 3.x
- **数据库**：MySQL 8.0
- **缓存**：Redis
- **前端**：Vue 3 + Element Plus（管理后台）/ 小程序或 H5（客户端）
- **文件存储**：MinIO / 阿里云 OSS
- **消息队列**：RabbitMQ（异步通知）

---

## 三、核心功能模块

### 3.1 客户管理模块

| 功能 | 描述 |
|------|------|
| 客户注册/登录 | 手机号、微信一键登录 |
| 客户档案 | 基本信息、宠物信息、服务历史 |
| 会员体系 | 积分、等级、优惠券管理 |
| 客户标签 | 自定义标签，精准营销 |

### 3.2 宠物档案模块

| 功能 | 描述 |
|------|------|
| 宠物基本信息 | 名字、品种、性别、生日、照片 |
| 生平记录 | 成长照片、视频、故事文字 |
| 健康档案 | 疫苗记录、病历（可选） |
| 离世信息 | 离世时间、原因、遗愿记录 |

### 3.3 服务套餐管理

| 功能 | 描述 |
|------|------|
| 套餐配置 | 基础告别、温馨告别、尊享告别等 |
| 单项服务 | 遗体接运、清洁美容、告别仪式、火化、骨灰处理 |
| 附加产品 | 骨灰盒、纪念品、鲜花、宠物寿衣 |
| 定价管理 | 按宠物体重/类型差异化定价 |

### 3.4 预约与订单管理

| 功能 | 描述 |
|------|------|
| 在线预约 | 选择服务、时间、地点 |
| 上门接运 | 地址管理、司机派单、轨迹追踪 |
| 订单流转 | 待确认 → 已接单 → 服务中 → 已完成 |
| 支付管理 | 微信/支付宝支付、定金/尾款 |
| 电子合同 | 服务协议在线签署 |

### 3.5 告别仪式管理

| 功能 | 描述 |
|------|------|
| 仪式预约 | 选择时间、场地、仪式流程 |
| 场地管理 | 告别厅排期、容量管理 |
| 仪式记录 | 拍照、录像、生成纪念视频 |
| 远程参与 | 视频直播告别仪式（疫情/异地场景） |

### 3.6 火化与骨灰管理

| 功能 | 描述 |
|------|------|
| 火化排期 | 设备排期、单独/集体火化 |
| 火化记录 | 时间、操作员、全程可追溯 |
| 骨灰存放 | 寄存柜位管理、到期提醒 |
| 骨灰制品 | 晶石、吊坠、植物葬等定制服务 |
| 生态安葬 | 树葬、花葬、海葬预约 |

---

## 四、特色功能模块 ⭐

### 4.1 🌈 宠物云纪念馆

> **核心亮点**：为每只宠物创建永久在线纪念空间

| 功能 | 描述 |
|------|------|
| 个性化主页 | 宠物头像、生卒日期、生平简介 |
| 时光相册 | 上传照片/视频，按时间线展示 |
| 留言墙 | 主人和亲友可留言寄托思念 |
| 虚拟祭品 | 在线献花、点烛、放天灯 |
| 背景音乐 | 自定义纪念馆背景音乐 |
| 二维码墓碑 | 生成专属二维码，扫码访问纪念馆 |
| 隐私设置 | 公开/仅好友/私密 |

**技术实现要点**：
- 为每只宠物生成唯一的纪念馆 URL
- 支持自定义域名绑定（高级会员）
- 静态页面 CDN 加速

---

### 4.2 🎬 AI 生命纪念视频

> **核心亮点**：自动生成感人的宠物生命回顾视频

| 功能 | 描述 |
|------|------|
| 智能剪辑 | 上传照片自动生成视频 |
| 多种模板 | 温馨、怀旧、治愈等风格 |
| 背景配乐 | 多首治愈系音乐可选 |
| 字幕旁白 | AI 生成或自定义文案 |
| 下载分享 | 支持下载、分享至社交平台 |

**技术实现要点**：
- 集成 FFmpeg 进行视频合成
- 可对接 AI 图像增强（老照片修复）
- 异步任务队列处理视频生成

---

### 4.3 📅 智能关怀提醒系统

> **核心亮点**：在特殊日子自动发送温暖提醒

| 提醒类型 | 描述 |
|----------|------|
| 周年祭日 | 离世周年纪念日提醒 |
| 生日纪念 | 宠物生日温馨提醒 |
| 节日关怀 | 清明节、中元节等祭祀节日 |
| 骨灰续期 | 寄存到期前 30/7/3 天提醒 |
| 心理关怀 | 离世初期定期发送治愈内容 |

**推送渠道**：微信服务号、短信、App 推送

---

### 4.4 🌍 线上追思会

> **核心亮点**：突破时空限制，让更多亲友参与告别

| 功能 | 描述 |
|------|------|
| 视频直播 | 告别仪式全程直播 |
| 多人连线 | 异地亲友视频连线发言 |
| 弹幕留言 | 实时发送祝福弹幕 |
| 电子花圈 | 远程赠送虚拟花圈 |
| 回放功能 | 仪式结束后可回看 |

**技术实现要点**：
- 集成腾讯云/阿里云直播 SDK
- WebRTC 实现多人连线
- 弹幕使用 WebSocket 实时推送

---

### 4.5 🗺️ 宠物天堂地图

> **核心亮点**：可视化展示宠物安息地点

| 功能 | 描述 |
|------|------|
| 地图标记 | 在地图上标记宠物安息位置 |
| 虚拟墓园 | 3D 虚拟墓园漫游 |
| 附近宠物 | 发现附近安息的宠物 |
| 路线导航 | 导航至实体墓园/树葬点 |
| AR 祭扫 | AR 技术实现虚拟祭扫 |

---

### 4.6 💚 宠物临终关怀指南

> **核心亮点**：提供专业的临终关怀知识支持

| 功能 | 描述 |
|------|------|
| 知识库 | 临终症状识别、陪伴技巧 |
| 心理疏导 | 主人心理调适文章/视频 |
| 专家问答 | 在线咨询宠物医生/心理咨询师 |
| 社区交流 | 宠物主人互助社区 |
| 遗愿清单 | 帮助主人完成宠物遗愿 |

---

### 4.7 🎁 纪念品定制商城

> **核心亮点**：将思念化为实物纪念品

| 产品类型 | 描述 |
|----------|------|
| 骨灰晶石 | 骨灰制作成晶石饰品 |
| 定制画像 | AI 生成宠物艺术画像 |
| 毛发纪念 | 毛发嵌入琥珀/吊坠 |
| 3D 打印 | 宠物形象 3D 打印摆件 |
| 纪念相册 | 精装照片书定制 |
| 星空命名 | 以宠物名字命名星星 |

---

### 4.8 📊 数据大屏与经营分析

> **核心亮点**：为机构管理者提供经营决策支持

| 功能 | 描述 |
|------|------|
| 实时数据 | 今日订单、收入、服务量 |
| 趋势分析 | 月度/季度/年度对比 |
| 客户画像 | 宠物类型、消费偏好分析 |
| 服务热力图 | 各区域服务分布 |
| 员工绩效 | 服务评分、订单量排名 |

---

## 五、数据库设计（核心表）

### 5.1 用户相关

```sql
-- 用户表
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    phone VARCHAR(20) UNIQUE,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    openid VARCHAR(64),
    member_level INT DEFAULT 0,
    points INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 宠物档案表
CREATE TABLE pet_profile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    species VARCHAR(20) COMMENT '物种：猫/狗/兔等',
    breed VARCHAR(50) COMMENT '品种',
    gender TINYINT COMMENT '1-公 2-母',
    birthday DATE,
    death_date DATE,
    death_reason VARCHAR(255),
    avatar VARCHAR(255),
    bio TEXT COMMENT '生平简介',
    memorial_url VARCHAR(100) COMMENT '纪念馆URL',
    status TINYINT DEFAULT 1 COMMENT '1-在世 2-已离世',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### 5.2 服务与订单

```sql
-- 服务套餐表
CREATE TABLE service_package (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    base_price DECIMAL(10,2),
    weight_range VARCHAR(50) COMMENT '适用体重范围',
    includes JSON COMMENT '包含的服务项',
    status TINYINT DEFAULT 1
);

-- 订单表
CREATE TABLE service_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) UNIQUE,
    user_id BIGINT NOT NULL,
    pet_id BIGINT NOT NULL,
    package_id BIGINT,
    total_amount DECIMAL(10,2),
    paid_amount DECIMAL(10,2) DEFAULT 0,
    status TINYINT COMMENT '0-待支付 1-已支付 2-服务中 3-已完成 4-已取消',
    appointment_time DATETIME,
    pickup_address VARCHAR(255),
    contact_name VARCHAR(50),
    contact_phone VARCHAR(20),
    remark TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### 5.3 纪念馆相关

```sql
-- 纪念馆表
CREATE TABLE memorial_hall (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pet_id BIGINT UNIQUE,
    template_id INT COMMENT '主题模板',
    background_music VARCHAR(255),
    visit_count INT DEFAULT 0,
    candle_count INT DEFAULT 0,
    flower_count INT DEFAULT 0,
    privacy TINYINT DEFAULT 1 COMMENT '1-公开 2-好友 3-私密',
    qr_code VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 纪念馆留言表
CREATE TABLE memorial_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hall_id BIGINT NOT NULL,
    user_id BIGINT,
    nickname VARCHAR(50),
    content TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 纪念相册表
CREATE TABLE memorial_album (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hall_id BIGINT NOT NULL,
    media_type TINYINT COMMENT '1-图片 2-视频',
    url VARCHAR(255),
    description VARCHAR(255),
    taken_at DATE COMMENT '拍摄日期',
    sort_order INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### 5.4 骨灰管理

```sql
-- 骨灰寄存表
CREATE TABLE ash_storage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT,
    pet_id BIGINT,
    cabinet_no VARCHAR(20) COMMENT '柜位编号',
    location VARCHAR(100) COMMENT '存放位置',
    start_date DATE,
    end_date DATE,
    status TINYINT COMMENT '1-寄存中 2-已取走 3-已到期',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

## 六、接口设计示例

### 6.1 预约服务

```
POST /api/v1/appointment/create

Request:
{
    "petId": 1001,
    "packageId": 2,
    "appointmentTime": "2026-02-01 10:00:00",
    "pickupAddress": "北京市朝阳区xxx小区",
    "contactName": "张先生",
    "contactPhone": "13800138000",
    "remark": "请轻拿轻放"
}

Response:
{
    "code": 200,
    "data": {
        "orderId": 10001,
        "orderNo": "PF202601300001",
        "totalAmount": 2980.00
    }
}
```

### 6.2 创建纪念馆

```
POST /api/v1/memorial/create

Request:
{
    "petId": 1001,
    "templateId": 3,
    "backgroundMusic": "healing_01.mp3",
    "privacy": 1
}

Response:
{
    "code": 200,
    "data": {
        "hallId": 5001,
        "memorialUrl": "https://memorial.example.com/h/abc123",
        "qrCode": "https://example.com/qr/abc123.png"
    }
}
```

---

## 七、非功能性需求

### 7.1 性能要求

- 页面加载时间 < 2 秒
- API 响应时间 < 500ms
- 支持 1000+ 并发用户

### 7.2 安全要求

- 用户密码加密存储（BCrypt）
- 敏感接口需登录鉴权（JWT）
- 防 SQL 注入、XSS 攻击
- 宠物照片/视频添加水印保护

### 7.3 可用性要求

- 系统可用性 > 99.9%
- 数据每日备份
- 支持灰度发布

---

## 八、项目里程碑

| 阶段 | 主要内容 |
|------|----------|
| 第一阶段 | 基础框架搭建、用户模块、宠物档案 |
| 第二阶段 | 服务套餐、预约订单、支付对接 |
| 第三阶段 | 纪念馆核心功能、相册留言 |
| 第四阶段 | 特色功能（AI视频、直播、地图） |
| 第五阶段 | 商城、数据分析、运营工具 |

---

## 九、竞品差异化

| 功能点 | 传统方案 | 本系统 |
|--------|----------|--------|
| 预约方式 | 电话预约 | 在线自助预约 |
| 服务透明度 | 不透明 | 全流程可追溯 |
| 纪念方式 | 无 | 永久在线纪念馆 |
| 远程参与 | 不支持 | 直播追思会 |
| 情感关怀 | 无 | 智能关怀提醒 |
| 纪念品 | 单一 | 丰富定制商城 |

---

## 十、附录

### 10.1 术语表

| 术语 | 解释 |
|------|------|
| 单独火化 | 一只宠物单独火化，骨灰完整保留 |
| 集体火化 | 多只宠物一起火化，不保留骨灰 |
| 树葬 | 将骨灰埋于树下，回归自然 |
| 海葬 | 将骨灰撒入大海 |
| 晶石 | 骨灰高温压制成的宝石 |

### 10.2 参考资料

- 《宠物殡葬服务规范》
- 《互联网个人信息保护指南》

---

*文档版本：v1.0*  
*创建日期：2026-01-30*
