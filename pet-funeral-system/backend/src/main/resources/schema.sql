-- =============================================
-- 宠物殡葬管理系统 - 数据库初始化脚本
-- 数据库: MySQL 8.0+
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS pet_funeral DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE pet_funeral;

-- =============================================
-- 系统用户表 (管理员)
-- =============================================
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像',
    role_id BIGINT COMMENT '角色ID',
    status TINYINT DEFAULT 1 COMMENT '状态 1-启用 0-禁用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- =============================================
-- 客户表
-- =============================================
CREATE TABLE IF NOT EXISTS customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像',
    openid VARCHAR(64) COMMENT '微信OpenID',
    member_level TINYINT DEFAULT 0 COMMENT '会员等级 0-普通 1-银卡 2-金卡 3-钻石',
    points INT DEFAULT 0 COMMENT '积分',
    status TINYINT DEFAULT 1 COMMENT '状态 1-正常 0-禁用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- =============================================
-- 宠物档案表
-- =============================================
CREATE TABLE IF NOT EXISTS pet (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    name VARCHAR(50) NOT NULL COMMENT '宠物名字',
    species VARCHAR(20) COMMENT '物种 dog/cat/rabbit/bird/other',
    breed VARCHAR(50) COMMENT '品种',
    gender TINYINT COMMENT '性别 1-公 2-母',
    birthday DATE COMMENT '生日',
    weight DECIMAL(5,2) COMMENT '体重(kg)',
    avatar VARCHAR(255) COMMENT '头像',
    bio TEXT COMMENT '生平简介',
    death_date DATE COMMENT '离世日期',
    death_reason VARCHAR(255) COMMENT '离世原因',
    status TINYINT DEFAULT 1 COMMENT '状态 1-在世 2-已离世',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_customer_id (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物档案表';

-- =============================================
-- 服务套餐表
-- =============================================
CREATE TABLE IF NOT EXISTS service_package (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '套餐名称',
    description TEXT COMMENT '套餐描述',
    base_price DECIMAL(10,2) NOT NULL COMMENT '基础价格',
    weight_range VARCHAR(50) COMMENT '适用体重范围',
    includes JSON COMMENT '包含的服务项',
    icon VARCHAR(50) COMMENT '套餐图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 1-上架 0-下架',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务套餐表';

-- =============================================
-- 服务订单表
-- =============================================
CREATE TABLE IF NOT EXISTS service_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单编号',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    pet_id BIGINT COMMENT '宠物ID',
    package_id BIGINT COMMENT '套餐ID',
    total_amount DECIMAL(10,2) COMMENT '订单总金额',
    paid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '已支付金额',
    status TINYINT DEFAULT 0 COMMENT '订单状态 0-待确认 1-已确认 2-待接运 3-服务中 4-已完成 5-已取消',
    pay_status TINYINT DEFAULT 0 COMMENT '支付状态 0-未支付 1-部分支付 2-已支付',
    appointment_time DATETIME COMMENT '预约时间',
    pickup_address VARCHAR(255) COMMENT '接运地址',
    contact_name VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    remark TEXT COMMENT '备注',
    staff_id BIGINT COMMENT '处理员工ID',
    confirmed_at DATETIME COMMENT '确认时间',
    completed_at DATETIME COMMENT '完成时间',
    cancel_reason VARCHAR(255) COMMENT '取消原因',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_customer_id (customer_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务订单表';

-- =============================================
-- 纪念馆表
-- =============================================
CREATE TABLE IF NOT EXISTS memorial (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    pet_id BIGINT NOT NULL UNIQUE COMMENT '宠物ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    url_key VARCHAR(20) NOT NULL UNIQUE COMMENT '纪念馆URL标识',
    template_id INT DEFAULT 1 COMMENT '主题模板ID',
    background_music VARCHAR(255) COMMENT '背景音乐',
    visit_count INT DEFAULT 0 COMMENT '访问次数',
    candle_count INT DEFAULT 0 COMMENT '点烛次数',
    flower_count INT DEFAULT 0 COMMENT '献花次数',
    privacy TINYINT DEFAULT 1 COMMENT '隐私设置 1-公开 2-好友可见 3-私密',
    qr_code VARCHAR(255) COMMENT '二维码图片',
    status TINYINT DEFAULT 1 COMMENT '状态 1-正常 0-关闭',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_customer_id (customer_id),
    INDEX idx_url_key (url_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='纪念馆表';

-- =============================================
-- 纪念馆留言表
-- =============================================
CREATE TABLE IF NOT EXISTS memorial_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    memorial_id BIGINT NOT NULL COMMENT '纪念馆ID',
    user_id BIGINT COMMENT '留言用户ID',
    nickname VARCHAR(50) COMMENT '昵称',
    content TEXT NOT NULL COMMENT '留言内容',
    candle_count INT DEFAULT 0 COMMENT '点烛数',
    status TINYINT DEFAULT 1 COMMENT '状态 1-显示 0-隐藏',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_memorial_id (memorial_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='纪念馆留言表';

-- =============================================
-- 纪念馆相册表
-- =============================================
CREATE TABLE IF NOT EXISTS memorial_album (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    memorial_id BIGINT NOT NULL COMMENT '纪念馆ID',
    media_type TINYINT DEFAULT 1 COMMENT '媒体类型 1-图片 2-视频',
    url VARCHAR(255) NOT NULL COMMENT '文件URL',
    thumb_url VARCHAR(255) COMMENT '缩略图URL',
    description VARCHAR(255) COMMENT '描述',
    taken_at DATE COMMENT '拍摄日期',
    sort_order INT DEFAULT 0 COMMENT '排序',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_memorial_id (memorial_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='纪念馆相册表';

-- =============================================
-- 骨灰寄存表
-- =============================================
CREATE TABLE IF NOT EXISTS ash_storage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT COMMENT '订单ID',
    pet_id BIGINT COMMENT '宠物ID',
    customer_id BIGINT COMMENT '客户ID',
    cabinet_no VARCHAR(20) COMMENT '柜位编号',
    location VARCHAR(100) COMMENT '存放位置描述',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '到期日期',
    status TINYINT DEFAULT 1 COMMENT '状态 1-寄存中 2-已取走 3-已到期',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_customer_id (customer_id),
    INDEX idx_end_date (end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='骨灰寄存表';

-- =============================================
-- 告别仪式表
-- =============================================
CREATE TABLE IF NOT EXISTS ceremony (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT COMMENT '订单ID',
    pet_id BIGINT COMMENT '宠物ID',
    customer_id BIGINT COMMENT '客户ID',
    ceremony_type TINYINT DEFAULT 1 COMMENT '仪式类型 1-简约告别 2-温馨告别 3-尊享告别',
    hall_no VARCHAR(20) COMMENT '仪式厅编号',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    attendee_count INT COMMENT '参加人数',
    host VARCHAR(50) COMMENT '主持人',
    staff_id BIGINT COMMENT '负责员工ID',
    special_requests TEXT COMMENT '特殊要求',
    remark TEXT COMMENT '备注',
    status TINYINT DEFAULT 0 COMMENT '状态 0-待安排 1-已安排 2-进行中 3-已完成 4-已取消',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_order_id (order_id),
    INDEX idx_start_time (start_time),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告别仪式表';

-- =============================================
-- 上传文件表
-- =============================================
CREATE TABLE IF NOT EXISTS upload_file (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    original_name VARCHAR(255) COMMENT '原始文件名',
    storage_name VARCHAR(255) COMMENT '存储文件名',
    file_path VARCHAR(500) COMMENT '文件路径',
    file_url VARCHAR(500) COMMENT '文件URL',
    file_type VARCHAR(20) COMMENT '文件类型 image/video/document',
    mime_type VARCHAR(100) COMMENT 'MIME类型',
    file_size BIGINT COMMENT '文件大小(字节)',
    thumb_url VARCHAR(500) COMMENT '缩略图URL',
    module VARCHAR(50) COMMENT '所属模块',
    biz_id BIGINT COMMENT '关联业务ID',
    upload_user_id BIGINT COMMENT '上传用户ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_module_biz (module, biz_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='上传文件表';

-- =============================================
-- 初始化数据 (所有表创建完成后执行)
-- =============================================

-- 初始化管理员 (密码: 123456)
INSERT INTO sys_user (username, password, real_name, status) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '系统管理员', 1);

-- 初始化服务套餐
INSERT INTO service_package (name, description, base_price, icon, sort_order, status) VALUES 
('基础告别', '集体火化，不保留骨灰', 1280.00, '🌿', 1, 1),
('温馨告别', '含告别仪式+单独火化+骨灰盒', 2980.00, '🌸', 2, 1),
('尊享告别', 'VIP全套服务+纪念相册+AI纪念视频', 5980.00, '💫', 3, 1);

-- 初始化测试客户
INSERT INTO customer (phone, nickname, member_level, points, status) VALUES 
('13800138001', '豆豆麻麻', 2, 2580, 1),
('13800138002', '咪咪粑粑', 1, 890, 1);

-- 初始化测试宠物
INSERT INTO pet (customer_id, name, species, breed, gender, birthday, weight, status, death_date, death_reason) VALUES 
(1, '豆豆', 'dog', '金毛寻回犬', 1, '2018-03-15', 32.00, 2, '2025-12-20', '器官衰竭'),
(2, '咪咪', 'cat', '英国短毛猫', 2, '2020-06-10', 5.50, 2, '2026-01-25', '自然老去');
