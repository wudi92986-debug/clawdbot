# 湛江高校大学生约拍平台 - API接口文档

## 文档信息

| 项目名称 | 湛江高校大学生约拍平台 |
|---------|---------------------|
| 文档版本 | V1.0 |
| 基础路径 | `https://api.example.com/api/v1` |
| 创建日期 | 2026-02-04 |

---

## 1. 接口规范

### 1.1 基本约定

| 项目 | 说明 |
|-----|------|
| 协议 | HTTPS |
| 请求格式 | JSON (Content-Type: application/json) |
| 响应格式 | JSON |
| 字符编码 | UTF-8 |
| 时间格式 | yyyy-MM-dd HH:mm:ss |
| 认证方式 | Bearer Token (JWT) |

### 1.2 请求头

```
Authorization: Bearer <access_token>
Content-Type: application/json
X-Request-Id: <uuid>          // 请求追踪ID
X-Platform: miniprogram       // 平台标识
X-Version: 1.0.0              // 客户端版本
```

### 1.3 统一响应格式

**成功响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": { },
  "timestamp": 1706972400000
}
```

**分页响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [],
    "pagination": {
      "page": 1,
      "pageSize": 10,
      "total": 100,
      "totalPages": 10
    }
  },
  "timestamp": 1706972400000
}
```

**错误响应**：
```json
{
  "code": 40001,
  "message": "参数错误",
  "errors": [
    { "field": "phone", "message": "手机号格式不正确" }
  ],
  "timestamp": 1706972400000
}
```

### 1.4 错误码定义

| 错误码 | 说明 |
|-------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未登录/Token失效 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
| 40001 | 参数校验失败 |
| 40002 | 数据不存在 |
| 40003 | 数据已存在 |
| 40101 | Token无效 |
| 40102 | Token过期 |
| 40301 | 无操作权限 |
| 40302 | 账号被禁用 |

---

## 2. 用户模块 (User)

### 2.1 微信登录

**POST** `/user/login/wechat`

微信小程序登录，获取Token

**请求参数**：
```json
{
  "code": "wxcode123456",      // 微信临时登录凭证（必填）
  "encryptedData": "...",      // 加密数据（可选，获取手机号时需要）
  "iv": "..."                  // 加密算法初始向量（可选）
}
```

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 7200,
    "userInfo": {
      "userId": 10001,
      "nickname": "用户昵称",
      "avatar": "https://...",
      "userType": 1,
      "isVerified": false,
      "isStudentVerified": false,
      "isNewUser": true
    }
  }
}
```

---

### 2.2 刷新Token

**POST** `/user/token/refresh`

**请求参数**：
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 7200
  }
}
```

---

### 2.3 获取用户信息

**GET** `/user/profile`

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "userId": 10001,
    "openid": "oXXXX",
    "nickname": "用户昵称",
    "avatar": "https://...",
    "gender": 1,
    "phone": "138****8888",
    "realName": "张*",
    "userType": 1,
    "isVerified": true,
    "isStudentVerified": true,
    "university": {
      "id": 1,
      "name": "广东海洋大学",
      "shortName": "海大"
    },
    "createTime": "2026-01-01 10:00:00"
  }
}
```

---

### 2.4 更新用户信息

**PUT** `/user/profile`

**请求参数**：
```json
{
  "nickname": "新昵称",
  "avatar": "https://...",
  "gender": 1
}
```

---

### 2.5 绑定手机号

**POST** `/user/bindPhone`

**请求参数**：
```json
{
  "encryptedData": "...",
  "iv": "..."
}
```

---

### 2.6 提交实名认证

**POST** `/user/verification/realname`

**请求参数**：
```json
{
  "realName": "张三",
  "idCard": "440000199901011234",
  "idCardFront": "https://...",
  "idCardBack": "https://..."
}
```

---

### 2.7 提交学生认证

**POST** `/user/verification/student`

**请求参数**：
```json
{
  "universityId": 1,
  "studentId": "2022010001",
  "enrollmentYear": 2022,
  "studentCardImg": "https://..."
}
```

---

### 2.8 获取认证状态

**GET** `/user/verification/status`

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "realname": {
      "status": 1,
      "submitTime": "2026-01-01 10:00:00",
      "auditTime": "2026-01-01 12:00:00"
    },
    "student": {
      "status": 0,
      "submitTime": "2026-01-02 10:00:00",
      "rejectReason": null
    }
  }
}
```

---

### 2.9 紧急联系人管理

#### 2.9.1 获取紧急联系人列表

**GET** `/user/emergency-contacts`

**响应数据**：
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "contactName": "李四",
      "contactPhone": "139****9999",
      "relationship": "朋友",
      "isDefault": true
    }
  ]
}
```

#### 2.9.2 添加紧急联系人

**POST** `/user/emergency-contacts`

**请求参数**：
```json
{
  "contactName": "李四",
  "contactPhone": "13999999999",
  "relationship": "朋友",
  "isDefault": true
}
```

#### 2.9.3 删除紧急联系人

**DELETE** `/user/emergency-contacts/{id}`

---

### 2.10 发送紧急求助

**POST** `/user/emergency/help`

**请求参数**：
```json
{
  "contactId": 1,
  "longitude": 110.359377,
  "latitude": 21.270708,
  "address": "广东省湛江市霞山区xxx",
  "message": "遇到紧急情况，请帮忙联系"
}
```

---

## 3. 摄影师模块 (Photographer)

### 3.1 摄影师列表

**GET** `/photographers`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页数量，默认10 |
| styleIds | string | 否 | 风格ID，多个逗号分隔 |
| serviceTypeId | int | 否 | 服务类型ID |
| minPrice | decimal | 否 | 最低价格 |
| maxPrice | decimal | 否 | 最高价格 |
| minRating | decimal | 否 | 最低评分 |
| gender | int | 否 | 性别：1-男，2-女 |
| universityId | int | 否 | 高校ID |
| keyword | string | 否 | 搜索关键词 |
| sortBy | string | 否 | 排序：rating/price/orders/distance |
| sortOrder | string | 否 | asc/desc |
| longitude | decimal | 否 | 当前经度（距离排序时需要） |
| latitude | decimal | 否 | 当前纬度（距离排序时需要） |

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "userId": 10001,
        "displayName": "摄影师小明",
        "avatar": "https://...",
        "bio": "专注人像摄影5年",
        "coverImage": "https://...",
        "avgRating": 4.8,
        "totalOrders": 156,
        "completedOrders": 150,
        "totalFans": 520,
        "minPrice": 199.00,
        "styles": [
          { "id": 1, "name": "日系" },
          { "id": 2, "name": "小清新" }
        ],
        "sampleWorks": [
          "https://sample1.jpg",
          "https://sample2.jpg",
          "https://sample3.jpg"
        ],
        "university": {
          "id": 1,
          "name": "广东海洋大学"
        },
        "isFollowed": false
      }
    ],
    "pagination": {
      "page": 1,
      "pageSize": 10,
      "total": 50,
      "totalPages": 5
    }
  }
}
```

---

### 3.2 摄影师详情

**GET** `/photographers/{id}`

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "userId": 10001,
    "displayName": "摄影师小明",
    "avatar": "https://...",
    "bio": "专注人像摄影5年，擅长日系、小清新风格...",
    "coverImage": "https://...",
    "gender": 1,
    "experienceYears": 5,
    "serviceArea": "湛江市区",
    "avgRating": 4.8,
    "totalOrders": 156,
    "completedOrders": 150,
    "totalFans": 520,
    "totalViews": 12580,
    "minPrice": 199.00,
    "depositRatio": 30,
    "equipment": {
      "camera": ["Sony A7M4", "Canon R6"],
      "lens": ["85mm f/1.4", "35mm f/1.8", "24-70mm f/2.8"]
    },
    "styles": [
      { "id": 1, "name": "日系" },
      { "id": 2, "name": "小清新" }
    ],
    "university": {
      "id": 1,
      "name": "广东海洋大学"
    },
    "isVerified": true,
    "isStudentVerified": true,
    "isFollowed": false,
    "isFavorited": false,
    "createTime": "2025-01-01 10:00:00"
  }
}
```

---

### 3.3 摄影师作品集

**GET** `/photographers/{id}/works`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |
| category | int | 否 | 分类：1-客片，2-创作 |
| styleId | int | 否 | 风格ID |

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "title": "毕业季写真",
        "coverImage": "https://...",
        "category": 1,
        "shootDate": "2026-01-15",
        "location": "海大校园",
        "viewCount": 1256,
        "likeCount": 89,
        "collectCount": 45,
        "isTop": true,
        "styles": [
          { "id": 1, "name": "日系" }
        ],
        "images": [
          { "url": "https://...", "thumbUrl": "https://..." }
        ]
      }
    ],
    "pagination": { }
  }
}
```

---

### 3.4 摄影师服务套餐

**GET** `/photographers/{id}/packages`

**响应数据**：
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "packageName": "基础人像套餐",
      "serviceType": {
        "id": 1,
        "name": "人像写真"
      },
      "description": "适合个人写真、形象照",
      "duration": 60,
      "originalCount": 50,
      "refinedCount": 10,
      "deliveryDays": 5,
      "price": 199.00,
      "originalPrice": 299.00,
      "depositRatio": 30
    },
    {
      "id": 2,
      "packageName": "精选人像套餐",
      "serviceType": {
        "id": 1,
        "name": "人像写真"
      },
      "description": "含化妆造型，适合精致写真",
      "duration": 120,
      "originalCount": 100,
      "refinedCount": 20,
      "deliveryDays": 7,
      "price": 399.00,
      "originalPrice": 499.00,
      "depositRatio": 30
    }
  ]
}
```

---

### 3.5 摄影师档期查询

**GET** `/photographers/{id}/schedules`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| startDate | string | 是 | 开始日期 yyyy-MM-dd |
| endDate | string | 是 | 结束日期 yyyy-MM-dd |

**响应数据**：
```json
{
  "code": 200,
  "data": [
    {
      "date": "2026-02-05",
      "slots": [
        { "timeSlot": "09:00-12:00", "status": 1 },
        { "timeSlot": "14:00-17:00", "status": 2 },
        { "timeSlot": "18:00-21:00", "status": 1 }
      ]
    },
    {
      "date": "2026-02-06",
      "slots": [
        { "timeSlot": "09:00-12:00", "status": 0 },
        { "timeSlot": "14:00-17:00", "status": 1 },
        { "timeSlot": "18:00-21:00", "status": 1 }
      ]
    }
  ]
}
```
> status: 0-不可用, 1-可预约, 2-已预约

---

### 3.6 摄影师评价列表

**GET** `/photographers/{id}/reviews`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |
| rating | int | 否 | 筛选评分：1-5 |
| hasImage | boolean | 否 | 是否有图 |

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "summary": {
      "avgRating": 4.8,
      "totalCount": 150,
      "ratingDistribution": {
        "5": 120,
        "4": 20,
        "3": 8,
        "2": 1,
        "1": 1
      },
      "dimensions": {
        "skill": 4.9,
        "attitude": 4.8,
        "punctuality": 4.7,
        "value": 4.6
      }
    },
    "list": [
      {
        "id": 1,
        "user": {
          "id": 10002,
          "nickname": "用**",
          "avatar": "https://..."
        },
        "overallRating": 5,
        "skillRating": 5,
        "attitudeRating": 5,
        "punctualityRating": 5,
        "valueRating": 5,
        "content": "摄影师很专业，出片效果很好！",
        "images": [
          "https://review1.jpg",
          "https://review2.jpg"
        ],
        "orderInfo": {
          "packageName": "精选人像套餐",
          "shootDate": "2026-01-15"
        },
        "reply": {
          "content": "感谢您的认可，期待下次合作！",
          "replyTime": "2026-01-20 15:00:00"
        },
        "createTime": "2026-01-18 10:00:00"
      }
    ],
    "pagination": { }
  }
}
```

---

## 4. 摄影师端接口 (Photographer Management)

### 4.1 申请成为摄影师

**POST** `/photographer/apply`

**请求参数**：
```json
{
  "displayName": "摄影师昵称",
  "bio": "个人简介...",
  "coverImage": "https://...",
  "experienceYears": 3,
  "serviceArea": "湛江市区",
  "wechat": "wx123456",
  "styleIds": [1, 2, 3],
  "equipment": {
    "camera": ["Sony A7M4"],
    "lens": ["85mm f/1.4"]
  },
  "sampleWorks": [
    "https://work1.jpg",
    "https://work2.jpg",
    "https://work3.jpg"
  ]
}
```

---

### 4.2 更新摄影师信息

**PUT** `/photographer/profile`

**请求参数**：
```json
{
  "displayName": "摄影师昵称",
  "bio": "个人简介...",
  "coverImage": "https://...",
  "experienceYears": 3,
  "serviceArea": "湛江市区",
  "styleIds": [1, 2, 3],
  "equipment": {
    "camera": ["Sony A7M4"],
    "lens": ["85mm f/1.4"]
  },
  "depositRatio": 30,
  "autoAccept": false
}
```

---

### 4.3 服务套餐管理

#### 4.3.1 获取我的套餐列表

**GET** `/photographer/packages`

#### 4.3.2 创建服务套餐

**POST** `/photographer/packages`

**请求参数**：
```json
{
  "packageName": "基础人像套餐",
  "serviceTypeId": 1,
  "description": "套餐描述...",
  "duration": 60,
  "originalCount": 50,
  "refinedCount": 10,
  "deliveryDays": 5,
  "price": 199.00,
  "originalPrice": 299.00,
  "depositRatio": 30
}
```

#### 4.3.3 更新服务套餐

**PUT** `/photographer/packages/{id}`

#### 4.3.4 删除服务套餐

**DELETE** `/photographer/packages/{id}`

#### 4.3.5 调整套餐状态

**PUT** `/photographer/packages/{id}/status`

**请求参数**：
```json
{
  "status": 1
}
```

---

### 4.4 作品集管理

#### 4.4.1 获取我的作品列表

**GET** `/photographer/works`

#### 4.4.2 上传作品

**POST** `/photographer/works`

**请求参数**：
```json
{
  "title": "作品标题",
  "description": "作品描述",
  "coverImage": "https://...",
  "category": 1,
  "shootDate": "2026-01-15",
  "location": "拍摄地点",
  "styleIds": [1, 2],
  "images": [
    { "url": "https://...", "sortOrder": 1 },
    { "url": "https://...", "sortOrder": 2 }
  ]
}
```

#### 4.4.3 更新作品

**PUT** `/photographer/works/{id}`

#### 4.4.4 删除作品

**DELETE** `/photographer/works/{id}`

#### 4.4.5 设置作品置顶

**PUT** `/photographer/works/{id}/top`

**请求参数**：
```json
{
  "isTop": true
}
```

---

### 4.5 档期管理

#### 4.5.1 获取我的档期

**GET** `/photographer/schedules`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| startDate | string | 是 | 开始日期 |
| endDate | string | 是 | 结束日期 |

#### 4.5.2 设置档期

**POST** `/photographer/schedules`

**请求参数**：
```json
{
  "schedules": [
    {
      "date": "2026-02-05",
      "timeSlot": "09:00-12:00",
      "status": 1
    },
    {
      "date": "2026-02-05",
      "timeSlot": "14:00-17:00",
      "status": 0
    }
  ]
}
```

#### 4.5.3 批量设置档期

**POST** `/photographer/schedules/batch`

**请求参数**：
```json
{
  "startDate": "2026-02-01",
  "endDate": "2026-02-28",
  "weekdays": [1, 2, 3, 4, 5],
  "timeSlots": ["09:00-12:00", "14:00-17:00"],
  "status": 1
}
```

---

### 4.6 需求广场

#### 4.6.1 浏览需求列表

**GET** `/photographer/demands`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |
| serviceTypeId | int | 否 | 服务类型 |
| styleIds | string | 否 | 风格ID |
| minBudget | decimal | 否 | 最低预算 |
| maxBudget | decimal | 否 | 最高预算 |
| shootDate | string | 否 | 拍摄日期 |

#### 4.6.2 报名需求

**POST** `/photographer/demands/{id}/apply`

**请求参数**：
```json
{
  "quotePrice": 299.00,
  "message": "您好，我对这个需求很感兴趣..."
}
```

---

### 4.7 数据看板

**GET** `/photographer/dashboard`

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "overview": {
      "totalIncome": 12580.00,
      "monthIncome": 2350.00,
      "totalOrders": 156,
      "monthOrders": 12,
      "avgRating": 4.8,
      "totalFans": 520
    },
    "todayStats": {
      "views": 58,
      "newFans": 3,
      "newOrders": 2,
      "income": 598.00
    },
    "weekTrend": {
      "views": [45, 52, 38, 61, 55, 48, 58],
      "orders": [1, 2, 0, 1, 2, 1, 2],
      "income": [299, 598, 0, 299, 598, 299, 598]
    },
    "hotWorks": [
      {
        "id": 1,
        "title": "毕业季写真",
        "coverImage": "https://...",
        "viewCount": 1256,
        "collectCount": 89
      }
    ]
  }
}
```

---

## 5. 需求模块 (Demand)

### 5.1 发布需求

**POST** `/demands`

**请求参数**：
```json
{
  "title": "寻找毕业照摄影师",
  "serviceTypeId": 2,
  "description": "毕业季拍摄，希望找一个有经验的摄影师...",
  "shootDate": "2026-06-20",
  "shootTimeSlot": "09:00-12:00",
  "duration": 180,
  "location": "广东海洋大学",
  "longitude": 110.359377,
  "latitude": 21.270708,
  "budgetMin": 200.00,
  "budgetMax": 500.00,
  "styleIds": [1, 4],
  "genderRequire": 0,
  "peopleCount": 1,
  "images": ["https://ref1.jpg", "https://ref2.jpg"]
}
```

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "demandId": 1001,
    "demandNo": "D202602040001"
  }
}
```

---

### 5.2 获取我的需求列表

**GET** `/demands/mine`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |
| status | int | 否 | 状态筛选 |

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1001,
        "demandNo": "D202602040001",
        "title": "寻找毕业照摄影师",
        "serviceType": { "id": 2, "name": "毕业照" },
        "shootDate": "2026-06-20",
        "budgetMin": 200.00,
        "budgetMax": 500.00,
        "status": 0,
        "viewCount": 56,
        "applyCount": 5,
        "createTime": "2026-02-04 10:00:00"
      }
    ],
    "pagination": { }
  }
}
```

---

### 5.3 获取需求详情

**GET** `/demands/{id}`

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "id": 1001,
    "demandNo": "D202602040001",
    "title": "寻找毕业照摄影师",
    "serviceType": { "id": 2, "name": "毕业照" },
    "description": "毕业季拍摄...",
    "shootDate": "2026-06-20",
    "shootTimeSlot": "09:00-12:00",
    "duration": 180,
    "location": "广东海洋大学",
    "longitude": 110.359377,
    "latitude": 21.270708,
    "budgetMin": 200.00,
    "budgetMax": 500.00,
    "styles": [
      { "id": 1, "name": "日系" },
      { "id": 4, "name": "小清新" }
    ],
    "genderRequire": 0,
    "peopleCount": 1,
    "images": ["https://ref1.jpg"],
    "status": 0,
    "viewCount": 56,
    "applyCount": 5,
    "expireTime": "2026-02-11 10:00:00",
    "createTime": "2026-02-04 10:00:00",
    "user": {
      "id": 10001,
      "nickname": "用户昵称",
      "avatar": "https://..."
    }
  }
}
```

---

### 5.4 获取需求报名列表

**GET** `/demands/{id}/applies`

**响应数据**：
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "photographer": {
        "id": 1,
        "displayName": "摄影师小明",
        "avatar": "https://...",
        "avgRating": 4.8,
        "completedOrders": 150
      },
      "quotePrice": 350.00,
      "message": "您好，我有丰富的毕业照拍摄经验...",
      "status": 0,
      "createTime": "2026-02-04 12:00:00"
    }
  ]
}
```

---

### 5.5 选择摄影师

**POST** `/demands/{id}/select`

**请求参数**：
```json
{
  "applyId": 1,
  "photographerId": 1
}
```

---

### 5.6 取消需求

**POST** `/demands/{id}/cancel`

---

## 6. 订单模块 (Order)

### 6.1 创建订单

**POST** `/orders`

**请求参数**：
```json
{
  "photographerId": 1,
  "packageId": 2,
  "demandId": null,
  "shootDate": "2026-02-20",
  "shootTimeSlot": "14:00-17:00",
  "location": "湛江市霞山区xxx",
  "longitude": 110.359377,
  "latitude": 21.270708,
  "remark": "希望拍日系风格",
  "couponId": null
}
```

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "orderId": 2001,
    "orderNo": "O202602040001",
    "totalAmount": 399.00,
    "depositAmount": 119.70,
    "discountAmount": 0,
    "payAmount": 119.70
  }
}
```

---

### 6.2 获取订单列表

**GET** `/orders`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |
| status | int | 否 | 订单状态 |
| role | string | 否 | 角色：customer/photographer |

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 2001,
        "orderNo": "O202602040001",
        "orderTitle": "精选人像套餐",
        "photographer": {
          "id": 1,
          "displayName": "摄影师小明",
          "avatar": "https://..."
        },
        "shootDate": "2026-02-20",
        "shootTimeSlot": "14:00-17:00",
        "location": "湛江市霞山区xxx",
        "totalAmount": 399.00,
        "actualAmount": 119.70,
        "status": 10,
        "statusText": "待接单",
        "createTime": "2026-02-04 15:00:00"
      }
    ],
    "pagination": { }
  }
}
```

---

### 6.3 获取订单详情

**GET** `/orders/{id}`

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "id": 2001,
    "orderNo": "O202602040001",
    "orderTitle": "精选人像套餐",
    "user": {
      "id": 10001,
      "nickname": "用户昵称",
      "avatar": "https://...",
      "phone": "138****8888"
    },
    "photographer": {
      "id": 1,
      "userId": 10002,
      "displayName": "摄影师小明",
      "avatar": "https://...",
      "phone": "139****9999"
    },
    "package": {
      "id": 2,
      "packageName": "精选人像套餐",
      "duration": 120,
      "originalCount": 100,
      "refinedCount": 20,
      "deliveryDays": 7
    },
    "serviceType": { "id": 1, "name": "人像写真" },
    "shootDate": "2026-02-20",
    "shootTimeSlot": "14:00-17:00",
    "location": "湛江市霞山区xxx",
    "longitude": 110.359377,
    "latitude": 21.270708,
    "totalAmount": 399.00,
    "depositAmount": 119.70,
    "finalAmount": 279.30,
    "actualAmount": 119.70,
    "discountAmount": 0,
    "status": 10,
    "statusText": "待接单",
    "payStatus": 1,
    "remark": "希望拍日系风格",
    "acceptTime": null,
    "deliveryDeadline": null,
    "createTime": "2026-02-04 15:00:00",
    "statusLogs": [
      {
        "fromStatus": 0,
        "toStatus": 10,
        "remark": "支付定金成功",
        "createTime": "2026-02-04 15:05:00"
      }
    ]
  }
}
```

---

### 6.4 订单状态操作

#### 6.4.1 取消订单（客户）

**POST** `/orders/{id}/cancel`

**请求参数**：
```json
{
  "reason": "时间有变动"
}
```

#### 6.4.2 接受订单（摄影师）

**POST** `/orders/{id}/accept`

#### 6.4.3 拒绝订单（摄影师）

**POST** `/orders/{id}/reject`

**请求参数**：
```json
{
  "reason": "档期已满"
}
```

#### 6.4.4 开始拍摄（摄影师）

**POST** `/orders/{id}/start-shooting`

#### 6.4.5 完成拍摄（摄影师）

**POST** `/orders/{id}/finish-shooting`

#### 6.4.6 确认收货（客户）

**POST** `/orders/{id}/confirm`

---

### 6.5 订单改期

**POST** `/orders/{id}/reschedule`

**请求参数**：
```json
{
  "newShootDate": "2026-02-25",
  "newTimeSlot": "09:00-12:00",
  "reason": "个人原因需要改期"
}
```

---

### 6.6 上传原片（摄影师）

**POST** `/orders/{id}/originals`

**请求参数**：
```json
{
  "images": [
    { "url": "https://...", "thumbUrl": "https://...", "watermarkUrl": "https://..." },
    { "url": "https://...", "thumbUrl": "https://...", "watermarkUrl": "https://..." }
  ]
}
```

---

### 6.7 选片（客户）

**POST** `/orders/{id}/select-photos`

**请求参数**：
```json
{
  "selectedIds": [1, 2, 3, 5, 8, 10, 12, 15, 18, 20]
}
```

---

### 6.8 上传精修片（摄影师）

**POST** `/orders/{id}/refined`

**请求参数**：
```json
{
  "images": [
    { "url": "https://...", "thumbUrl": "https://..." }
  ]
}
```

---

### 6.9 获取订单作品

**GET** `/orders/{id}/works`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| type | int | 否 | 类型：1-原片，2-精修 |

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "originals": [
      {
        "id": 1,
        "imageUrl": "https://...",
        "thumbUrl": "https://...",
        "watermarkUrl": "https://...",
        "isSelected": true
      }
    ],
    "refined": [
      {
        "id": 101,
        "imageUrl": "https://...",
        "thumbUrl": "https://..."
      }
    ],
    "selectedCount": 10,
    "maxSelectCount": 20
  }
}
```

---

### 6.10 订单评价

**POST** `/orders/{id}/review`

**请求参数**：
```json
{
  "overallRating": 5,
  "skillRating": 5,
  "attitudeRating": 5,
  "punctualityRating": 5,
  "valueRating": 5,
  "content": "摄影师很专业，拍摄效果很满意！",
  "images": ["https://review1.jpg", "https://review2.jpg"],
  "isAnonymous": false
}
```

---

### 6.11 追加评价

**POST** `/orders/{id}/review/append`

**请求参数**：
```json
{
  "content": "照片越看越喜欢，强烈推荐！"
}
```

---

### 6.12 摄影师回复评价

**POST** `/orders/{id}/review/reply`

**请求参数**：
```json
{
  "content": "感谢您的认可，期待下次合作！"
}
```

---

## 7. 支付模块 (Payment)

### 7.1 发起支付

**POST** `/payments/create`

**请求参数**：
```json
{
  "orderId": 2001,
  "paymentType": 1
}
```
> paymentType: 1-定金, 2-尾款, 3-全款

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "paymentNo": "P202602040001",
    "amount": 119.70,
    "wxPayParams": {
      "appId": "wx...",
      "timeStamp": "1706972400",
      "nonceStr": "xxx",
      "package": "prepay_id=xxx",
      "signType": "RSA",
      "paySign": "xxx"
    }
  }
}
```

---

### 7.2 支付结果查询

**GET** `/payments/{paymentNo}/status`

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "paymentNo": "P202602040001",
    "status": 1,
    "statusText": "支付成功",
    "amount": 119.70,
    "payTime": "2026-02-04 15:05:00"
  }
}
```

---

### 7.3 申请退款

**POST** `/payments/refund`

**请求参数**：
```json
{
  "orderId": 2001,
  "reason": "摄影师取消订单"
}
```

---

### 7.4 钱包信息（摄影师）

**GET** `/wallet`

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "totalIncome": 12580.00,
    "balance": 3560.00,
    "frozenAmount": 598.00,
    "withdrawnAmount": 8422.00
  }
}
```

---

### 7.5 钱包流水（摄影师）

**GET** `/wallet/logs`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |
| type | int | 否 | 类型：1-收入，2-提现，3-退款 |
| startDate | string | 否 | 开始日期 |
| endDate | string | 否 | 结束日期 |

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "type": 1,
        "typeText": "订单收入",
        "amount": 359.10,
        "orderNo": "O202602040001",
        "balanceAfter": 3560.00,
        "remark": "订单完成收入",
        "createTime": "2026-02-04 18:00:00"
      }
    ],
    "pagination": { }
  }
}
```

---

### 7.6 申请提现（摄影师）

**POST** `/wallet/withdraw`

**请求参数**：
```json
{
  "amount": 500.00,
  "withdrawType": 1
}
```
> withdrawType: 1-微信零钱, 2-银行卡

---

### 7.7 提现记录（摄影师）

**GET** `/wallet/withdrawals`

---

## 8. 消息模块 (Message)

### 8.1 获取会话列表

**GET** `/conversations`

**响应数据**：
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "targetUser": {
        "id": 10002,
        "nickname": "摄影师小明",
        "avatar": "https://..."
      },
      "lastMessage": {
        "content": "好的，那我们约周六下午",
        "type": 1,
        "time": "2026-02-04 16:30:00"
      },
      "unreadCount": 2,
      "isTop": false
    }
  ]
}
```

---

### 8.2 获取消息列表

**GET** `/conversations/{id}/messages`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| lastId | long | 否 | 上一页最后一条消息ID |
| limit | int | 否 | 数量限制，默认20 |

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 101,
        "senderId": 10001,
        "receiverId": 10002,
        "messageType": 1,
        "content": "您好，想咨询一下拍摄",
        "isRead": true,
        "createTime": "2026-02-04 16:00:00"
      },
      {
        "id": 102,
        "senderId": 10002,
        "receiverId": 10001,
        "messageType": 1,
        "content": "您好，请问想拍什么类型的呢？",
        "isRead": true,
        "createTime": "2026-02-04 16:05:00"
      }
    ],
    "hasMore": true
  }
}
```

---

### 8.3 发送消息

**POST** `/conversations/{id}/messages`

**请求参数**：
```json
{
  "messageType": 1,
  "content": "您好，想咨询一下拍摄"
}
```

或发送图片：
```json
{
  "messageType": 2,
  "mediaUrl": "https://..."
}
```

或发送位置：
```json
{
  "messageType": 4,
  "content": "湛江市霞山区xxx",
  "extra": {
    "longitude": 110.359377,
    "latitude": 21.270708
  }
}
```

或发送订单卡片：
```json
{
  "messageType": 5,
  "extra": {
    "orderId": 2001
  }
}
```

---

### 8.4 标记已读

**POST** `/conversations/{id}/read`

---

### 8.5 删除会话

**DELETE** `/conversations/{id}`

---

### 8.6 获取系统通知

**GET** `/notifications`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |
| type | int | 否 | 通知类型 |

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "title": "订单已接单",
        "content": "您的订单O202602040001已被摄影师接单",
        "notifyType": 2,
        "linkType": "order",
        "linkId": 2001,
        "isRead": false,
        "createTime": "2026-02-04 16:00:00"
      }
    ],
    "pagination": { },
    "unreadCount": 5
  }
}
```

---

### 8.7 标记通知已读

**POST** `/notifications/read`

**请求参数**：
```json
{
  "ids": [1, 2, 3]
}
```

或全部标记已读：
```json
{
  "all": true
}
```

---

## 9. 社区模块 (Community)

### 9.1 获取动态列表

**GET** `/posts`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |
| type | int | 否 | 类型：1-动态，2-官方，3-攻略 |
| topicId | int | 否 | 话题ID |
| userId | int | 否 | 用户ID（查看某人动态） |
| tab | string | 否 | recommend/latest/following |

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "user": {
          "id": 10001,
          "nickname": "用户昵称",
          "avatar": "https://...",
          "isPhotographer": true
        },
        "content": "今天的拍摄太开心了！分享一组毕业照~",
        "images": [
          { "url": "https://...", "thumbUrl": "https://..." }
        ],
        "topic": {
          "id": 1,
          "name": "毕业季"
        },
        "location": "广东海洋大学",
        "viewCount": 256,
        "likeCount": 89,
        "commentCount": 12,
        "collectCount": 23,
        "isLiked": false,
        "isCollected": false,
        "createTime": "2026-02-04 18:00:00"
      }
    ],
    "pagination": { }
  }
}
```

---

### 9.2 获取动态详情

**GET** `/posts/{id}`

---

### 9.3 发布动态

**POST** `/posts`

**请求参数**：
```json
{
  "content": "今天的拍摄太开心了！分享一组毕业照~",
  "images": [
    { "url": "https://...", "thumbUrl": "https://..." }
  ],
  "topicId": 1,
  "location": "广东海洋大学",
  "orderId": null
}
```

---

### 9.4 删除动态

**DELETE** `/posts/{id}`

---

### 9.5 点赞/取消点赞

**POST** `/posts/{id}/like`

---

### 9.6 收藏/取消收藏

**POST** `/posts/{id}/collect`

---

### 9.7 获取评论列表

**GET** `/posts/{id}/comments`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "user": {
          "id": 10002,
          "nickname": "评论用户",
          "avatar": "https://..."
        },
        "content": "拍得真好看！",
        "likeCount": 5,
        "isLiked": false,
        "replies": [
          {
            "id": 2,
            "user": {
              "id": 10001,
              "nickname": "楼主",
              "avatar": "https://..."
            },
            "replyUser": {
              "id": 10002,
              "nickname": "评论用户"
            },
            "content": "谢谢！",
            "createTime": "2026-02-04 19:00:00"
          }
        ],
        "createTime": "2026-02-04 18:30:00"
      }
    ],
    "pagination": { }
  }
}
```

---

### 9.8 发表评论

**POST** `/posts/{id}/comments`

**请求参数**：
```json
{
  "content": "拍得真好看！",
  "parentId": null,
  "replyUserId": null
}
```

---

### 9.9 删除评论

**DELETE** `/comments/{id}`

---

### 9.10 获取话题列表

**GET** `/topics`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| hot | boolean | 否 | 是否只获取热门 |

**响应数据**：
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "topicName": "毕业季",
      "description": "记录最美毕业时光",
      "coverImage": "https://...",
      "postCount": 1256,
      "viewCount": 58960,
      "isHot": true,
      "isOfficial": true
    }
  ]
}
```

---

### 9.11 获取话题详情

**GET** `/topics/{id}`

---

## 10. 互动模块 (Interaction)

### 10.1 关注/取消关注

**POST** `/users/{id}/follow`

---

### 10.2 获取关注列表

**GET** `/users/following`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

---

### 10.3 获取粉丝列表

**GET** `/users/followers`

---

### 10.4 收藏摄影师

**POST** `/photographers/{id}/favorite`

---

### 10.5 收藏作品

**POST** `/works/{id}/favorite`

---

### 10.6 获取收藏列表

**GET** `/favorites`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| type | int | 是 | 类型：1-摄影师，2-作品，3-动态 |
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |
| folderId | int | 否 | 收藏夹ID |

---

### 10.7 收藏夹管理

#### 10.7.1 获取收藏夹列表

**GET** `/favorite-folders`

#### 10.7.2 创建收藏夹

**POST** `/favorite-folders`

**请求参数**：
```json
{
  "folderName": "毕业照灵感"
}
```

#### 10.7.3 删除收藏夹

**DELETE** `/favorite-folders/{id}`

---

## 11. 基础数据模块 (Base)

### 11.1 获取高校列表

**GET** `/base/universities`

**响应数据**：
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "universityName": "广东海洋大学",
      "shortName": "海大",
      "city": "湛江"
    },
    {
      "id": 2,
      "universityName": "广东医科大学",
      "shortName": "广医",
      "city": "湛江"
    }
  ]
}
```

---

### 11.2 获取风格标签

**GET** `/base/style-tags`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| type | int | 否 | 类型：1-拍摄风格，2-场景标签 |

**响应数据**：
```json
{
  "code": 200,
  "data": [
    { "id": 1, "tagName": "日系", "tagType": 1 },
    { "id": 2, "tagName": "韩系", "tagType": 1 },
    { "id": 11, "tagName": "校园", "tagType": 2 },
    { "id": 12, "tagName": "海边", "tagType": 2 }
  ]
}
```

---

### 11.3 获取服务类型

**GET** `/base/service-types`

**响应数据**：
```json
{
  "code": 200,
  "data": [
    { "id": 1, "typeName": "人像写真", "icon": "https://..." },
    { "id": 2, "typeName": "毕业照", "icon": "https://..." }
  ]
}
```

---

### 11.4 获取轮播图

**GET** `/base/banners`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| position | string | 否 | 位置：home/community |

**响应数据**：
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "title": "毕业季特惠",
      "imageUrl": "https://...",
      "linkType": 4,
      "linkUrl": "1"
    }
  ]
}
```

---

### 11.5 获取系统配置

**GET** `/base/configs`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| keys | string | 是 | 配置键，多个逗号分隔 |

---

## 12. 文件上传 (Upload)

### 12.1 获取上传凭证

**GET** `/upload/token`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| type | string | 是 | 文件类型：avatar/works/post/review/id_card |
| count | int | 否 | 文件数量，默认1 |

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "uploadUrl": "https://oss.example.com",
    "tokens": [
      {
        "key": "avatar/10001/20260204150000_abc123.jpg",
        "token": "xxx",
        "expires": 3600
      }
    ]
  }
}
```

---

### 12.2 上传完成回调

**POST** `/upload/callback`

**请求参数**：
```json
{
  "key": "avatar/10001/20260204150000_abc123.jpg",
  "size": 102400,
  "mimeType": "image/jpeg"
}
```

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "url": "https://cdn.example.com/avatar/10001/20260204150000_abc123.jpg",
    "thumbUrl": "https://cdn.example.com/avatar/10001/20260204150000_abc123_thumb.jpg"
  }
}
```

---

## 13. 优惠券模块 (Coupon)

### 13.1 获取可领取优惠券

**GET** `/coupons/available`

**响应数据**：
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "couponName": "新人专享券",
      "couponType": 1,
      "discountAmount": 30.00,
      "minAmount": 200.00,
      "validDays": 30,
      "description": "满200减30，新用户专享",
      "remainCount": 100
    }
  ]
}
```

---

### 13.2 领取优惠券

**POST** `/coupons/{id}/receive`

---

### 13.3 获取我的优惠券

**GET** `/coupons/mine`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| status | int | 否 | 状态：0-未使用，1-已使用，2-已过期 |

**响应数据**：
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "couponCode": "NEWUSER202602040001",
      "couponName": "新人专享券",
      "couponType": 1,
      "discountAmount": 30.00,
      "minAmount": 200.00,
      "status": 0,
      "expireTime": "2026-03-04 23:59:59"
    }
  ]
}
```

---

### 13.4 获取订单可用优惠券

**GET** `/coupons/order-available`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| amount | decimal | 是 | 订单金额 |

---

## 14. 管理端接口 (Admin)

### 14.1 管理员登录

**POST** `/admin/login`

**请求参数**：
```json
{
  "username": "admin",
  "password": "xxx"
}
```

---

### 14.2 用户管理

#### 14.2.1 用户列表

**GET** `/admin/users`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |
| keyword | string | 否 | 搜索关键词 |
| userType | int | 否 | 用户类型 |
| status | int | 否 | 状态 |
| universityId | int | 否 | 高校ID |

#### 14.2.2 用户详情

**GET** `/admin/users/{id}`

#### 14.2.3 禁用/启用用户

**PUT** `/admin/users/{id}/status`

**请求参数**：
```json
{
  "status": 0,
  "reason": "违规操作"
}
```

---

### 14.3 认证审核

#### 14.3.1 认证列表

**GET** `/admin/verifications`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| type | int | 否 | 类型：1-实名，2-学生 |
| status | int | 否 | 状态 |

#### 14.3.2 审核认证

**PUT** `/admin/verifications/{id}/audit`

**请求参数**：
```json
{
  "status": 1,
  "rejectReason": null
}
```

---

### 14.4 摄影师审核

#### 14.4.1 摄影师申请列表

**GET** `/admin/photographers/pending`

#### 14.4.2 审核摄影师

**PUT** `/admin/photographers/{id}/audit`

**请求参数**：
```json
{
  "auditStatus": 1,
  "rejectReason": null
}
```

---

### 14.5 内容审核

#### 14.5.1 作品审核列表

**GET** `/admin/works/pending`

#### 14.5.2 审核作品

**PUT** `/admin/works/{id}/audit`

**请求参数**：
```json
{
  "status": 1,
  "rejectReason": null
}
```

#### 14.5.3 动态审核列表

**GET** `/admin/posts/pending`

#### 14.5.4 审核动态

**PUT** `/admin/posts/{id}/audit`

---

### 14.6 订单管理

#### 14.6.1 订单列表

**GET** `/admin/orders`

**请求参数**：
| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |
| orderNo | string | 否 | 订单号 |
| status | int | 否 | 状态 |
| startDate | string | 否 | 开始日期 |
| endDate | string | 否 | 结束日期 |

#### 14.6.2 订单详情

**GET** `/admin/orders/{id}`

#### 14.6.3 订单介入

**POST** `/admin/orders/{id}/intervene`

**请求参数**：
```json
{
  "action": "refund",
  "amount": 119.70,
  "reason": "客户投诉，协商退款"
}
```

---

### 14.7 退款审核

#### 14.7.1 退款列表

**GET** `/admin/refunds`

#### 14.7.2 审核退款

**PUT** `/admin/refunds/{id}/audit`

**请求参数**：
```json
{
  "auditStatus": 1,
  "auditRemark": "同意退款"
}
```

---

### 14.8 提现审核

#### 14.8.1 提现列表

**GET** `/admin/withdrawals`

#### 14.8.2 审核提现

**PUT** `/admin/withdrawals/{id}/audit`

**请求参数**：
```json
{
  "status": 1,
  "rejectReason": null
}
```

---

### 14.9 紧急求助处理

#### 14.9.1 求助列表

**GET** `/admin/emergency-helps`

#### 14.9.2 处理求助

**PUT** `/admin/emergency-helps/{id}/handle`

**请求参数**：
```json
{
  "status": 2,
  "handleResult": "已联系用户，情况已解决"
}
```

---

### 14.10 社区运营

#### 14.10.1 话题管理

**GET** `/admin/topics`

**POST** `/admin/topics`

**PUT** `/admin/topics/{id}`

**DELETE** `/admin/topics/{id}`

#### 14.10.2 轮播图管理

**GET** `/admin/banners`

**POST** `/admin/banners`

**PUT** `/admin/banners/{id}`

**DELETE** `/admin/banners/{id}`

---

### 14.11 数据统计

#### 14.11.1 总览数据

**GET** `/admin/statistics/overview`

**响应数据**：
```json
{
  "code": 200,
  "data": {
    "today": {
      "newUsers": 56,
      "newOrders": 23,
      "orderAmount": 6890.00,
      "newPhotographers": 3
    },
    "total": {
      "totalUsers": 15680,
      "totalPhotographers": 256,
      "totalOrders": 8956,
      "totalAmount": 1256890.00
    },
    "weekTrend": {
      "dates": ["02-01", "02-02", "02-03", "02-04"],
      "users": [45, 52, 48, 56],
      "orders": [18, 22, 20, 23],
      "amount": [5400, 6600, 6000, 6890]
    }
  }
}
```

#### 14.11.2 用户统计

**GET** `/admin/statistics/users`

#### 14.11.3 订单统计

**GET** `/admin/statistics/orders`

#### 14.11.4 高校统计

**GET** `/admin/statistics/universities`

---

### 14.12 系统配置

#### 14.12.1 获取配置列表

**GET** `/admin/configs`

#### 14.12.2 更新配置

**PUT** `/admin/configs/{key}`

**请求参数**：
```json
{
  "configValue": "0.1"
}
```

---

### 14.13 敏感词管理

#### 14.13.1 敏感词列表

**GET** `/admin/sensitive-words`

#### 14.13.2 添加敏感词

**POST** `/admin/sensitive-words`

**请求参数**：
```json
{
  "word": "敏感词",
  "category": "广告",
  "level": 2,
  "action": 1,
  "replaceWord": "***"
}
```

#### 14.13.3 删除敏感词

**DELETE** `/admin/sensitive-words/{id}`

---

### 14.14 系统通知

#### 14.14.1 发送系统通知

**POST** `/admin/notifications`

**请求参数**：
```json
{
  "title": "系统维护通知",
  "content": "平台将于xxx进行系统维护...",
  "notifyType": 1,
  "targetType": 0,
  "userIds": null
}
```

---

## 附录

### A. 订单状态码

| 状态码 | 说明 |
|-------|------|
| 0 | 待支付 |
| 10 | 待接单 |
| 20 | 待拍摄 |
| 30 | 拍摄中 |
| 40 | 待选片 |
| 50 | 待交付 |
| 60 | 待确认 |
| 70 | 已完成 |
| 80 | 已取消 |
| 90 | 售后中 |

### B. 消息类型

| 类型码 | 说明 |
|-------|------|
| 1 | 文字 |
| 2 | 图片 |
| 3 | 语音 |
| 4 | 位置 |
| 5 | 订单卡片 |

### C. 通知类型

| 类型码 | 说明 |
|-------|------|
| 1 | 系统公告 |
| 2 | 订单通知 |
| 3 | 审核通知 |
| 4 | 活动通知 |
