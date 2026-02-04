-- =====================================================
-- 湛江高校大学生约拍平台 - 数据库建表脚本
-- 数据库类型：MySQL 8.0+
-- 字符集：utf8mb4
-- 创建日期：2026-02-04
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `zj_photography` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `zj_photography`;

-- =====================================================
-- 用户模块
-- =====================================================

-- 用户表
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `openid` VARCHAR(64) NOT NULL COMMENT '微信openid',
  `union_id` VARCHAR(64) DEFAULT NULL COMMENT '微信union_id',
  `nickname` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '昵称',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `gender` TINYINT(1) DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
  `id_card` VARCHAR(50) DEFAULT NULL COMMENT '身份证号（加密存储）',
  `user_type` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '用户类型：1-普通用户，2-摄影师，3-管理员',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常，2-禁言',
  `is_verified` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否实名认证：0-否，1-是',
  `is_student_verified` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否学生认证：0-否，1-是',
  `university_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '所属高校ID',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_openid` (`openid`),
  KEY `idx_user_phone` (`phone`),
  KEY `idx_user_university` (`university_id`),
  KEY `idx_user_type` (`user_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户认证表
DROP TABLE IF EXISTS `t_user_verification`;
CREATE TABLE `t_user_verification` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `verify_type` TINYINT(1) NOT NULL COMMENT '认证类型：1-实名认证，2-学生认证',
  `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
  `id_card` VARCHAR(50) DEFAULT NULL COMMENT '身份证号（加密）',
  `id_card_front` VARCHAR(500) DEFAULT NULL COMMENT '身份证正面图片',
  `id_card_back` VARCHAR(500) DEFAULT NULL COMMENT '身份证背面图片',
  `student_id` VARCHAR(50) DEFAULT NULL COMMENT '学号',
  `university_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '学校ID',
  `student_card_img` VARCHAR(500) DEFAULT NULL COMMENT '学生证图片',
  `enrollment_year` INT DEFAULT NULL COMMENT '入学年份',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核，1-通过，2-拒绝',
  `reject_reason` VARCHAR(200) DEFAULT NULL COMMENT '拒绝原因',
  `auditor_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '审核人ID',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_verification_user` (`user_id`),
  KEY `idx_verification_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户认证表';

-- 用户紧急联系人表
DROP TABLE IF EXISTS `t_user_emergency_contact`;
CREATE TABLE `t_user_emergency_contact` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `contact_name` VARCHAR(50) NOT NULL COMMENT '联系人姓名',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系人电话',
  `relationship` VARCHAR(20) DEFAULT NULL COMMENT '关系',
  `is_default` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否默认联系人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_emergency_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户紧急联系人表';

-- 紧急求助记录表
DROP TABLE IF EXISTS `t_emergency_help`;
CREATE TABLE `t_emergency_help` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `contact_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '紧急联系人ID',
  `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '详细地址',
  `message` VARCHAR(500) DEFAULT NULL COMMENT '求助信息',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-待处理，1-处理中，2-已处理',
  `handler_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '处理人ID',
  `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
  `handle_result` VARCHAR(500) DEFAULT NULL COMMENT '处理结果',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_emergency_user` (`user_id`),
  KEY `idx_emergency_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='紧急求助记录表';

-- =====================================================
-- 摄影师模块
-- =====================================================

-- 摄影师信息表
DROP TABLE IF EXISTS `t_photographer`;
CREATE TABLE `t_photographer` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '关联用户ID',
  `display_name` VARCHAR(50) NOT NULL COMMENT '展示名称/艺名',
  `bio` VARCHAR(500) DEFAULT NULL COMMENT '个人简介',
  `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '主页封面图',
  `experience_years` TINYINT DEFAULT 0 COMMENT '从业年限',
  `service_area` VARCHAR(200) DEFAULT NULL COMMENT '服务区域',
  `wechat` VARCHAR(50) DEFAULT NULL COMMENT '微信号（加密存储）',
  `equipment` TEXT DEFAULT NULL COMMENT '设备信息（JSON格式）',
  `avg_rating` DECIMAL(2,1) DEFAULT 5.0 COMMENT '平均评分',
  `total_orders` INT DEFAULT 0 COMMENT '总订单数',
  `completed_orders` INT DEFAULT 0 COMMENT '完成订单数',
  `total_fans` INT DEFAULT 0 COMMENT '粉丝数',
  `total_views` INT DEFAULT 0 COMMENT '主页访问量',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-待审核，1-正常，2-休息中，3-封禁',
  `audit_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核，1-通过，2-拒绝',
  `reject_reason` VARCHAR(200) DEFAULT NULL COMMENT '拒绝原因',
  `min_price` DECIMAL(10,2) DEFAULT NULL COMMENT '起步价',
  `deposit_ratio` INT DEFAULT 30 COMMENT '定金比例（%）',
  `auto_accept` TINYINT(1) DEFAULT 0 COMMENT '是否自动接单',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_photographer_user` (`user_id`),
  KEY `idx_photographer_status` (`status`),
  KEY `idx_photographer_rating` (`avg_rating`),
  KEY `idx_photographer_price` (`min_price`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='摄影师信息表';

-- 摄影师风格标签关联表
DROP TABLE IF EXISTS `t_photographer_style`;
CREATE TABLE `t_photographer_style` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `photographer_id` BIGINT UNSIGNED NOT NULL COMMENT '摄影师ID',
  `style_id` BIGINT UNSIGNED NOT NULL COMMENT '风格ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_photographer_style` (`photographer_id`, `style_id`),
  KEY `idx_style_id` (`style_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='摄影师风格标签关联表';

-- 服务套餐表
DROP TABLE IF EXISTS `t_service_package`;
CREATE TABLE `t_service_package` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '套餐ID',
  `photographer_id` BIGINT UNSIGNED NOT NULL COMMENT '摄影师ID',
  `package_name` VARCHAR(100) NOT NULL COMMENT '套餐名称',
  `service_type_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '服务类型ID',
  `description` TEXT DEFAULT NULL COMMENT '套餐描述',
  `duration` INT DEFAULT 60 COMMENT '服务时长（分钟）',
  `original_count` INT DEFAULT 50 COMMENT '原片数量',
  `refined_count` INT DEFAULT 10 COMMENT '精修数量',
  `delivery_days` INT DEFAULT 7 COMMENT '交付天数',
  `price` DECIMAL(10,2) NOT NULL COMMENT '套餐价格',
  `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
  `deposit_ratio` INT DEFAULT 30 COMMENT '定金比例（%）',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_package_photographer` (`photographer_id`),
  KEY `idx_package_type` (`service_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务套餐表';

-- 摄影师档期表
DROP TABLE IF EXISTS `t_photographer_schedule`;
CREATE TABLE `t_photographer_schedule` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `photographer_id` BIGINT UNSIGNED NOT NULL COMMENT '摄影师ID',
  `schedule_date` DATE NOT NULL COMMENT '日期',
  `time_slot` VARCHAR(20) NOT NULL COMMENT '时段（如 09:00-12:00）',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-不可用，1-可预约，2-已预约',
  `order_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '关联订单ID',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_schedule_photographer_date` (`photographer_id`, `schedule_date`),
  KEY `idx_schedule_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='摄影师档期表';

-- =====================================================
-- 作品模块
-- =====================================================

-- 作品表
DROP TABLE IF EXISTS `t_works`;
CREATE TABLE `t_works` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '作品ID',
  `photographer_id` BIGINT UNSIGNED NOT NULL COMMENT '摄影师ID',
  `title` VARCHAR(100) DEFAULT NULL COMMENT '作品标题',
  `description` TEXT DEFAULT NULL COMMENT '作品描述',
  `cover_image` VARCHAR(500) NOT NULL COMMENT '封面图片',
  `category` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '分类：1-客片，2-个人创作',
  `shoot_date` DATE DEFAULT NULL COMMENT '拍摄日期',
  `location` VARCHAR(200) DEFAULT NULL COMMENT '拍摄地点',
  `view_count` INT DEFAULT 0 COMMENT '浏览量',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `collect_count` INT DEFAULT 0 COMMENT '收藏数',
  `is_top` TINYINT(1) DEFAULT 0 COMMENT '是否置顶',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已发布，2-已下架，3-审核拒绝',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `auditor_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '审核人ID',
  `reject_reason` VARCHAR(200) DEFAULT NULL COMMENT '拒绝原因',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_works_photographer` (`photographer_id`),
  KEY `idx_works_category` (`category`),
  KEY `idx_works_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作品表';

-- 作品图片表
DROP TABLE IF EXISTS `t_works_image`;
CREATE TABLE `t_works_image` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `works_id` BIGINT UNSIGNED NOT NULL COMMENT '作品ID',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `thumb_url` VARCHAR(500) DEFAULT NULL COMMENT '缩略图URL',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_image_works` (`works_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作品图片表';

-- 作品风格标签关联表
DROP TABLE IF EXISTS `t_works_style`;
CREATE TABLE `t_works_style` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `works_id` BIGINT UNSIGNED NOT NULL COMMENT '作品ID',
  `style_id` BIGINT UNSIGNED NOT NULL COMMENT '风格ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_works_style` (`works_id`, `style_id`),
  KEY `idx_style_works` (`style_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作品风格标签关联表';

-- =====================================================
-- 需求模块
-- =====================================================

-- 约拍需求表
DROP TABLE IF EXISTS `t_demand`;
CREATE TABLE `t_demand` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '需求ID',
  `demand_no` VARCHAR(32) NOT NULL COMMENT '需求编号',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '发布用户ID',
  `title` VARCHAR(100) NOT NULL COMMENT '需求标题',
  `service_type_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '服务类型ID',
  `description` TEXT DEFAULT NULL COMMENT '详细描述',
  `shoot_date` DATE DEFAULT NULL COMMENT '期望拍摄日期',
  `shoot_time_slot` VARCHAR(50) DEFAULT NULL COMMENT '期望时段',
  `duration` INT DEFAULT NULL COMMENT '期望时长（分钟）',
  `location` VARCHAR(200) DEFAULT NULL COMMENT '拍摄地点',
  `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
  `budget_min` DECIMAL(10,2) DEFAULT NULL COMMENT '预算最低',
  `budget_max` DECIMAL(10,2) DEFAULT NULL COMMENT '预算最高',
  `gender_require` TINYINT(1) DEFAULT 0 COMMENT '摄影师性别要求：0-不限，1-男，2-女',
  `people_count` INT DEFAULT 1 COMMENT '拍摄人数',
  `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-征集中，1-已确定，2-已完成，3-已取消，4-已过期',
  `view_count` INT DEFAULT 0 COMMENT '浏览量',
  `apply_count` INT DEFAULT 0 COMMENT '报名人数',
  `selected_photographer_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '选定的摄影师ID',
  `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_demand_no` (`demand_no`),
  KEY `idx_demand_user` (`user_id`),
  KEY `idx_demand_status` (`status`),
  KEY `idx_demand_date` (`shoot_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='约拍需求表';

-- 需求风格标签关联表
DROP TABLE IF EXISTS `t_demand_style`;
CREATE TABLE `t_demand_style` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `demand_id` BIGINT UNSIGNED NOT NULL COMMENT '需求ID',
  `style_id` BIGINT UNSIGNED NOT NULL COMMENT '风格ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_demand_style` (`demand_id`, `style_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求风格标签关联表';

-- 需求参考图片表
DROP TABLE IF EXISTS `t_demand_image`;
CREATE TABLE `t_demand_image` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `demand_id` BIGINT UNSIGNED NOT NULL COMMENT '需求ID',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_demand_image` (`demand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求参考图片表';

-- 需求报名表
DROP TABLE IF EXISTS `t_demand_apply`;
CREATE TABLE `t_demand_apply` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `demand_id` BIGINT UNSIGNED NOT NULL COMMENT '需求ID',
  `photographer_id` BIGINT UNSIGNED NOT NULL COMMENT '摄影师ID',
  `quote_price` DECIMAL(10,2) DEFAULT NULL COMMENT '报价',
  `message` VARCHAR(500) DEFAULT NULL COMMENT '留言',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-待处理，1-已选中，2-未选中',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_demand_photographer` (`demand_id`, `photographer_id`),
  KEY `idx_apply_photographer` (`photographer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求报名表';

-- =====================================================
-- 订单模块
-- =====================================================

-- 订单表
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '客户用户ID',
  `photographer_id` BIGINT UNSIGNED NOT NULL COMMENT '摄影师ID',
  `demand_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '关联需求ID',
  `package_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '套餐ID',
  `service_type_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '服务类型ID',
  `order_title` VARCHAR(200) DEFAULT NULL COMMENT '订单标题',
  `shoot_date` DATE NOT NULL COMMENT '拍摄日期',
  `shoot_time_slot` VARCHAR(50) DEFAULT NULL COMMENT '拍摄时段',
  `duration` INT DEFAULT 60 COMMENT '预计时长（分钟）',
  `location` VARCHAR(200) DEFAULT NULL COMMENT '拍摄地点',
  `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
  `original_count` INT DEFAULT 0 COMMENT '原片数量',
  `refined_count` INT DEFAULT 0 COMMENT '精修数量',
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
  `deposit_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '定金金额',
  `final_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '尾款金额',
  `actual_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '实付金额',
  `discount_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '优惠金额',
  `coupon_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '优惠券ID',
  `platform_fee` DECIMAL(10,2) DEFAULT 0 COMMENT '平台服务费',
  `photographer_income` DECIMAL(10,2) DEFAULT 0 COMMENT '摄影师收入',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '订单状态：0-待支付，10-待接单，20-待拍摄，30-拍摄中，40-待选片，50-待交付，60-待确认，70-已完成，80-已取消，90-售后中',
  `pay_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '支付状态：0-未支付，1-已付定金，2-已付全款',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '订单备注',
  `cancel_reason` VARCHAR(200) DEFAULT NULL COMMENT '取消原因',
  `cancel_time` DATETIME DEFAULT NULL COMMENT '取消时间',
  `cancel_by` TINYINT(1) DEFAULT NULL COMMENT '取消方：1-客户，2-摄影师，3-系统',
  `accept_time` DATETIME DEFAULT NULL COMMENT '接单时间',
  `shoot_time` DATETIME DEFAULT NULL COMMENT '实际拍摄时间',
  `complete_time` DATETIME DEFAULT NULL COMMENT '完成时间',
  `delivery_deadline` DATETIME DEFAULT NULL COMMENT '交付截止时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_order_user` (`user_id`),
  KEY `idx_order_photographer` (`photographer_id`),
  KEY `idx_order_status` (`status`),
  KEY `idx_order_date` (`shoot_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单状态变更记录表
DROP TABLE IF EXISTS `t_order_status_log`;
CREATE TABLE `t_order_status_log` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `from_status` TINYINT(1) DEFAULT NULL COMMENT '原状态',
  `to_status` TINYINT(1) NOT NULL COMMENT '新状态',
  `operator_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '操作人ID',
  `operator_type` TINYINT(1) DEFAULT NULL COMMENT '操作人类型：1-客户，2-摄影师，3-系统，4-管理员',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_log_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单状态变更记录表';

-- 订单作品表
DROP TABLE IF EXISTS `t_order_works`;
CREATE TABLE `t_order_works` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `thumb_url` VARCHAR(500) DEFAULT NULL COMMENT '缩略图URL',
  `watermark_url` VARCHAR(500) DEFAULT NULL COMMENT '带水印图片URL',
  `image_type` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '类型：1-原片，2-精修片',
  `is_selected` TINYINT(1) DEFAULT 0 COMMENT '是否被选中（原片选片用）',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `upload_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_works` (`order_id`),
  KEY `idx_works_type` (`image_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单作品表';

-- =====================================================
-- 支付模块
-- =====================================================

-- 支付记录表
DROP TABLE IF EXISTS `t_payment`;
CREATE TABLE `t_payment` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '支付ID',
  `payment_no` VARCHAR(32) NOT NULL COMMENT '支付单号',
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '支付用户ID',
  `payment_type` TINYINT(1) NOT NULL COMMENT '支付类型：1-定金，2-尾款，3-全款',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '支付金额',
  `pay_method` TINYINT(1) DEFAULT 1 COMMENT '支付方式：1-微信支付',
  `transaction_id` VARCHAR(64) DEFAULT NULL COMMENT '微信支付交易号',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-待支付，1-支付成功，2-支付失败，3-已退款',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付成功时间',
  `expire_time` DATETIME DEFAULT NULL COMMENT '支付过期时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_no` (`payment_no`),
  KEY `idx_payment_order` (`order_id`),
  KEY `idx_payment_user` (`user_id`),
  KEY `idx_payment_transaction` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- 退款记录表
DROP TABLE IF EXISTS `t_refund`;
CREATE TABLE `t_refund` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '退款ID',
  `refund_no` VARCHAR(32) NOT NULL COMMENT '退款单号',
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `payment_id` BIGINT UNSIGNED NOT NULL COMMENT '原支付ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `refund_amount` DECIMAL(10,2) NOT NULL COMMENT '退款金额',
  `refund_reason` VARCHAR(200) DEFAULT NULL COMMENT '退款原因',
  `refund_type` TINYINT(1) NOT NULL COMMENT '退款类型：1-用户取消，2-摄影师取消，3-售后退款',
  `wx_refund_id` VARCHAR(64) DEFAULT NULL COMMENT '微信退款单号',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-待处理，1-退款中，2-退款成功，3-退款失败',
  `audit_status` TINYINT(1) DEFAULT 0 COMMENT '审核状态：0-待审核，1-通过，2-拒绝',
  `auditor_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '审核人ID',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `audit_remark` VARCHAR(200) DEFAULT NULL COMMENT '审核备注',
  `refund_time` DATETIME DEFAULT NULL COMMENT '退款成功时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_refund_no` (`refund_no`),
  KEY `idx_refund_order` (`order_id`),
  KEY `idx_refund_payment` (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款记录表';

-- 摄影师钱包表
DROP TABLE IF EXISTS `t_wallet`;
CREATE TABLE `t_wallet` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '钱包ID',
  `photographer_id` BIGINT UNSIGNED NOT NULL COMMENT '摄影师ID',
  `total_income` DECIMAL(12,2) DEFAULT 0 COMMENT '累计收入',
  `balance` DECIMAL(12,2) DEFAULT 0 COMMENT '可用余额',
  `frozen_amount` DECIMAL(12,2) DEFAULT 0 COMMENT '冻结金额',
  `withdrawn_amount` DECIMAL(12,2) DEFAULT 0 COMMENT '已提现金额',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_wallet_photographer` (`photographer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='摄影师钱包表';

-- 钱包流水表
DROP TABLE IF EXISTS `t_wallet_log`;
CREATE TABLE `t_wallet_log` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `wallet_id` BIGINT UNSIGNED NOT NULL COMMENT '钱包ID',
  `photographer_id` BIGINT UNSIGNED NOT NULL COMMENT '摄影师ID',
  `order_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '关联订单ID',
  `type` TINYINT(1) NOT NULL COMMENT '类型：1-订单收入，2-提现，3-退款扣除，4-平台补贴',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '金额（正数为入账，负数为出账）',
  `balance_before` DECIMAL(12,2) NOT NULL COMMENT '变动前余额',
  `balance_after` DECIMAL(12,2) NOT NULL COMMENT '变动后余额',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_log_wallet` (`wallet_id`),
  KEY `idx_log_photographer` (`photographer_id`),
  KEY `idx_log_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包流水表';

-- 提现申请表
DROP TABLE IF EXISTS `t_withdrawal`;
CREATE TABLE `t_withdrawal` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '提现ID',
  `withdrawal_no` VARCHAR(32) NOT NULL COMMENT '提现单号',
  `photographer_id` BIGINT UNSIGNED NOT NULL COMMENT '摄影师ID',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '提现金额',
  `fee` DECIMAL(10,2) DEFAULT 0 COMMENT '手续费',
  `actual_amount` DECIMAL(10,2) NOT NULL COMMENT '实际到账金额',
  `withdraw_type` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '提现方式：1-微信零钱，2-银行卡',
  `bank_name` VARCHAR(50) DEFAULT NULL COMMENT '银行名称',
  `bank_account` VARCHAR(50) DEFAULT NULL COMMENT '银行账号',
  `account_name` VARCHAR(50) DEFAULT NULL COMMENT '账户姓名',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-待审核，1-处理中，2-已完成，3-已拒绝',
  `reject_reason` VARCHAR(200) DEFAULT NULL COMMENT '拒绝原因',
  `auditor_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '审核人ID',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `complete_time` DATETIME DEFAULT NULL COMMENT '完成时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_withdrawal_no` (`withdrawal_no`),
  KEY `idx_withdrawal_photographer` (`photographer_id`),
  KEY `idx_withdrawal_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现申请表';

-- =====================================================
-- 消息模块
-- =====================================================

-- 会话表
DROP TABLE IF EXISTS `t_conversation`;
CREATE TABLE `t_conversation` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `target_user_id` BIGINT UNSIGNED NOT NULL COMMENT '对方用户ID',
  `last_message_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '最后一条消息ID',
  `last_message_time` DATETIME DEFAULT NULL COMMENT '最后消息时间',
  `last_message_content` VARCHAR(200) DEFAULT NULL COMMENT '最后消息内容摘要',
  `unread_count` INT DEFAULT 0 COMMENT '未读消息数',
  `is_top` TINYINT(1) DEFAULT 0 COMMENT '是否置顶',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-删除，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_conversation_users` (`user_id`, `target_user_id`),
  KEY `idx_conversation_target` (`target_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会话表';

-- 消息表
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `conversation_id` BIGINT UNSIGNED NOT NULL COMMENT '会话ID',
  `sender_id` BIGINT UNSIGNED NOT NULL COMMENT '发送者ID',
  `receiver_id` BIGINT UNSIGNED NOT NULL COMMENT '接收者ID',
  `message_type` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '消息类型：1-文字，2-图片，3-语音，4-位置，5-订单卡片',
  `content` TEXT DEFAULT NULL COMMENT '消息内容',
  `media_url` VARCHAR(500) DEFAULT NULL COMMENT '媒体文件URL',
  `media_duration` INT DEFAULT NULL COMMENT '语音时长（秒）',
  `extra` JSON DEFAULT NULL COMMENT '扩展信息（JSON）',
  `is_read` TINYINT(1) DEFAULT 0 COMMENT '是否已读',
  `read_time` DATETIME DEFAULT NULL COMMENT '已读时间',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-已撤回，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_message_conversation` (`conversation_id`),
  KEY `idx_message_sender` (`sender_id`),
  KEY `idx_message_receiver` (`receiver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- 系统通知表
DROP TABLE IF EXISTS `t_notification`;
CREATE TABLE `t_notification` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `user_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '接收用户ID（NULL为全体）',
  `title` VARCHAR(100) NOT NULL COMMENT '通知标题',
  `content` TEXT NOT NULL COMMENT '通知内容',
  `notify_type` TINYINT(1) NOT NULL COMMENT '通知类型：1-系统公告，2-订单通知，3-审核通知，4-活动通知',
  `target_type` TINYINT(1) DEFAULT 0 COMMENT '接收对象：0-全部，1-客户，2-摄影师',
  `link_type` VARCHAR(50) DEFAULT NULL COMMENT '跳转类型',
  `link_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '跳转目标ID',
  `is_read` TINYINT(1) DEFAULT 0 COMMENT '是否已读',
  `read_time` DATETIME DEFAULT NULL COMMENT '已读时间',
  `send_time` DATETIME DEFAULT NULL COMMENT '发送时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_notification_user` (`user_id`),
  KEY `idx_notification_type` (`notify_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统通知表';

-- =====================================================
-- 社区模块
-- =====================================================

-- 动态表
DROP TABLE IF EXISTS `t_post`;
CREATE TABLE `t_post` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '动态ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '发布用户ID',
  `content` TEXT DEFAULT NULL COMMENT '动态内容',
  `post_type` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '类型：1-用户动态，2-官方内容，3-攻略',
  `topic_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '话题ID',
  `order_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '关联订单ID',
  `location` VARCHAR(100) DEFAULT NULL COMMENT '位置',
  `view_count` INT DEFAULT 0 COMMENT '浏览量',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `comment_count` INT DEFAULT 0 COMMENT '评论数',
  `share_count` INT DEFAULT 0 COMMENT '分享数',
  `collect_count` INT DEFAULT 0 COMMENT '收藏数',
  `is_top` TINYINT(1) DEFAULT 0 COMMENT '是否置顶',
  `is_featured` TINYINT(1) DEFAULT 0 COMMENT '是否精选',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已发布，2-已下架，3-审核拒绝',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `auditor_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '审核人ID',
  `reject_reason` VARCHAR(200) DEFAULT NULL COMMENT '拒绝原因',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_post_user` (`user_id`),
  KEY `idx_post_topic` (`topic_id`),
  KEY `idx_post_status` (`status`),
  KEY `idx_post_type` (`post_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态表';

-- 动态图片表
DROP TABLE IF EXISTS `t_post_image`;
CREATE TABLE `t_post_image` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `post_id` BIGINT UNSIGNED NOT NULL COMMENT '动态ID',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `thumb_url` VARCHAR(500) DEFAULT NULL COMMENT '缩略图URL',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_post_image` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态图片表';

-- 话题表
DROP TABLE IF EXISTS `t_topic`;
CREATE TABLE `t_topic` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '话题ID',
  `topic_name` VARCHAR(50) NOT NULL COMMENT '话题名称',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '话题描述',
  `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '话题封面',
  `post_count` INT DEFAULT 0 COMMENT '动态数',
  `view_count` INT DEFAULT 0 COMMENT '浏览量',
  `is_hot` TINYINT(1) DEFAULT 0 COMMENT '是否热门',
  `is_official` TINYINT(1) DEFAULT 0 COMMENT '是否官方话题',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_topic_name` (`topic_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='话题表';

-- 评论表
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` BIGINT UNSIGNED NOT NULL COMMENT '动态ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '评论用户ID',
  `parent_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '父评论ID（回复）',
  `reply_user_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '回复目标用户ID',
  `content` VARCHAR(500) NOT NULL COMMENT '评论内容',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-已删除，1-正常，2-违规',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_comment_post` (`post_id`),
  KEY `idx_comment_user` (`user_id`),
  KEY `idx_comment_parent` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- =====================================================
-- 评价模块
-- =====================================================

-- 订单评价表
DROP TABLE IF EXISTS `t_review`;
CREATE TABLE `t_review` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '评价用户ID',
  `photographer_id` BIGINT UNSIGNED NOT NULL COMMENT '被评价摄影师ID',
  `overall_rating` TINYINT(1) NOT NULL COMMENT '综合评分（1-5）',
  `skill_rating` TINYINT(1) DEFAULT NULL COMMENT '拍摄技术评分',
  `attitude_rating` TINYINT(1) DEFAULT NULL COMMENT '服务态度评分',
  `punctuality_rating` TINYINT(1) DEFAULT NULL COMMENT '准时程度评分',
  `value_rating` TINYINT(1) DEFAULT NULL COMMENT '性价比评分',
  `content` VARCHAR(500) DEFAULT NULL COMMENT '评价内容',
  `is_anonymous` TINYINT(1) DEFAULT 0 COMMENT '是否匿名',
  `reply_content` VARCHAR(500) DEFAULT NULL COMMENT '摄影师回复',
  `reply_time` DATETIME DEFAULT NULL COMMENT '回复时间',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已发布，2-已屏蔽',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_review_order` (`order_id`),
  KEY `idx_review_user` (`user_id`),
  KEY `idx_review_photographer` (`photographer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单评价表';

-- 评价图片表
DROP TABLE IF EXISTS `t_review_image`;
CREATE TABLE `t_review_image` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `review_id` BIGINT UNSIGNED NOT NULL COMMENT '评价ID',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_review_image` (`review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价图片表';

-- 追加评价表
DROP TABLE IF EXISTS `t_review_append`;
CREATE TABLE `t_review_append` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `review_id` BIGINT UNSIGNED NOT NULL COMMENT '原评价ID',
  `content` VARCHAR(500) NOT NULL COMMENT '追加内容',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已发布',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_append_review` (`review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='追加评价表';

-- =====================================================
-- 互动模块
-- =====================================================

-- 收藏表
DROP TABLE IF EXISTS `t_favorite`;
CREATE TABLE `t_favorite` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `target_type` TINYINT(1) NOT NULL COMMENT '收藏类型：1-摄影师，2-作品，3-动态',
  `target_id` BIGINT UNSIGNED NOT NULL COMMENT '收藏目标ID',
  `folder_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '收藏夹ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_favorite` (`user_id`, `target_type`, `target_id`),
  KEY `idx_favorite_target` (`target_type`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 收藏夹表
DROP TABLE IF EXISTS `t_favorite_folder`;
CREATE TABLE `t_favorite_folder` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '收藏夹ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `folder_name` VARCHAR(50) NOT NULL COMMENT '收藏夹名称',
  `is_default` TINYINT(1) DEFAULT 0 COMMENT '是否默认',
  `item_count` INT DEFAULT 0 COMMENT '内容数量',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_folder_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏夹表';

-- 关注表
DROP TABLE IF EXISTS `t_follow`;
CREATE TABLE `t_follow` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '关注者用户ID',
  `follow_user_id` BIGINT UNSIGNED NOT NULL COMMENT '被关注者用户ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_follow` (`user_id`, `follow_user_id`),
  KEY `idx_follow_target` (`follow_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关注表';

-- 点赞表
DROP TABLE IF EXISTS `t_like`;
CREATE TABLE `t_like` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `target_type` TINYINT(1) NOT NULL COMMENT '点赞类型：1-作品，2-动态，3-评论',
  `target_id` BIGINT UNSIGNED NOT NULL COMMENT '点赞目标ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_like` (`user_id`, `target_type`, `target_id`),
  KEY `idx_like_target` (`target_type`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

-- =====================================================
-- 基础数据模块
-- =====================================================

-- 高校表
DROP TABLE IF EXISTS `t_university`;
CREATE TABLE `t_university` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '高校ID',
  `university_name` VARCHAR(100) NOT NULL COMMENT '高校名称',
  `short_name` VARCHAR(50) DEFAULT NULL COMMENT '简称',
  `province` VARCHAR(50) DEFAULT NULL COMMENT '省份',
  `city` VARCHAR(50) DEFAULT NULL COMMENT '城市',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
  `logo` VARCHAR(500) DEFAULT NULL COMMENT 'Logo图片',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_university_city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='高校表';

-- 风格标签表
DROP TABLE IF EXISTS `t_style_tag`;
CREATE TABLE `t_style_tag` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `tag_name` VARCHAR(50) NOT NULL COMMENT '标签名称',
  `tag_type` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '类型：1-拍摄风格，2-场景标签',
  `icon` VARCHAR(500) DEFAULT NULL COMMENT '图标',
  `color` VARCHAR(20) DEFAULT NULL COMMENT '颜色值',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tag_type` (`tag_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风格标签表';

-- 服务类型表
DROP TABLE IF EXISTS `t_service_type`;
CREATE TABLE `t_service_type` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '服务类型ID',
  `type_name` VARCHAR(50) NOT NULL COMMENT '类型名称',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
  `icon` VARCHAR(500) DEFAULT NULL COMMENT '图标',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务类型表';

-- 轮播图表
DROP TABLE IF EXISTS `t_banner`;
CREATE TABLE `t_banner` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Banner ID',
  `title` VARCHAR(100) DEFAULT NULL COMMENT '标题',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `link_type` TINYINT(1) DEFAULT NULL COMMENT '跳转类型：1-摄影师，2-作品，3-动态，4-话题，5-外链',
  `link_url` VARCHAR(500) DEFAULT NULL COMMENT '跳转链接/ID',
  `position` VARCHAR(50) DEFAULT 'home' COMMENT '展示位置',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_banner_position` (`position`),
  KEY `idx_banner_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- =====================================================
-- 系统管理模块
-- =====================================================

-- 管理员表
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（加密）',
  `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像',
  `role_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '角色ID',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_admin_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 角色表
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_key` VARCHAR(50) NOT NULL COMMENT '角色标识',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `parent_id` BIGINT UNSIGNED DEFAULT 0 COMMENT '父权限ID',
  `permission_name` VARCHAR(50) NOT NULL COMMENT '权限名称',
  `permission_key` VARCHAR(100) NOT NULL COMMENT '权限标识',
  `permission_type` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '类型：1-目录，2-菜单，3-按钮',
  `path` VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
  `icon` VARCHAR(100) DEFAULT NULL COMMENT '图标',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_permission_parent` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色权限关联表
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
  `permission_id` BIGINT UNSIGNED NOT NULL COMMENT '权限ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 敏感词表
DROP TABLE IF EXISTS `t_sensitive_word`;
CREATE TABLE `t_sensitive_word` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `word` VARCHAR(100) NOT NULL COMMENT '敏感词',
  `category` VARCHAR(50) DEFAULT NULL COMMENT '分类',
  `level` TINYINT(1) DEFAULT 1 COMMENT '级别：1-低，2-中，3-高',
  `action` TINYINT(1) DEFAULT 1 COMMENT '处理方式：1-替换，2-拦截',
  `replace_word` VARCHAR(100) DEFAULT '***' COMMENT '替换词',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_word` (`word`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='敏感词表';

-- 操作日志表
DROP TABLE IF EXISTS `t_operation_log`;
CREATE TABLE `t_operation_log` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `admin_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '操作人ID',
  `admin_name` VARCHAR(50) DEFAULT NULL COMMENT '操作人名称',
  `module` VARCHAR(50) DEFAULT NULL COMMENT '操作模块',
  `action` VARCHAR(100) DEFAULT NULL COMMENT '操作行为',
  `method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法',
  `url` VARCHAR(500) DEFAULT NULL COMMENT '请求URL',
  `params` TEXT DEFAULT NULL COMMENT '请求参数',
  `result` TEXT DEFAULT NULL COMMENT '返回结果',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT '操作IP',
  `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
  `duration` INT DEFAULT NULL COMMENT '执行时长（毫秒）',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-失败，1-成功',
  `error_msg` TEXT DEFAULT NULL COMMENT '错误信息',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_log_admin` (`admin_id`),
  KEY `idx_log_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 系统配置表
DROP TABLE IF EXISTS `t_system_config`;
CREATE TABLE `t_system_config` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `config_value` TEXT DEFAULT NULL COMMENT '配置值',
  `config_type` VARCHAR(50) DEFAULT NULL COMMENT '配置类型',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- =====================================================
-- 优惠券模块
-- =====================================================

-- 优惠券模板表
DROP TABLE IF EXISTS `t_coupon_template`;
CREATE TABLE `t_coupon_template` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `coupon_name` VARCHAR(100) NOT NULL COMMENT '优惠券名称',
  `coupon_type` TINYINT(1) NOT NULL COMMENT '类型：1-满减，2-折扣，3-立减',
  `discount_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '优惠金额',
  `discount_rate` DECIMAL(3,2) DEFAULT NULL COMMENT '折扣率（如0.9表示9折）',
  `min_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '最低消费金额',
  `max_discount` DECIMAL(10,2) DEFAULT NULL COMMENT '最高优惠金额',
  `total_count` INT DEFAULT 0 COMMENT '发放总量（0为不限）',
  `received_count` INT DEFAULT 0 COMMENT '已领取数量',
  `used_count` INT DEFAULT 0 COMMENT '已使用数量',
  `per_limit` INT DEFAULT 1 COMMENT '每人限领数量',
  `valid_type` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '有效期类型：1-固定日期，2-领取后N天',
  `valid_days` INT DEFAULT NULL COMMENT '有效天数（valid_type=2时）',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '使用说明',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券模板表';

-- 用户优惠券表
DROP TABLE IF EXISTS `t_user_coupon`;
CREATE TABLE `t_user_coupon` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `template_id` BIGINT UNSIGNED NOT NULL COMMENT '优惠券模板ID',
  `coupon_code` VARCHAR(32) NOT NULL COMMENT '优惠券码',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-未使用，1-已使用，2-已过期',
  `order_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '使用订单ID',
  `use_time` DATETIME DEFAULT NULL COMMENT '使用时间',
  `expire_time` DATETIME NOT NULL COMMENT '过期时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_coupon_code` (`coupon_code`),
  KEY `idx_coupon_user` (`user_id`),
  KEY `idx_coupon_template` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- =====================================================
-- 初始化数据
-- =====================================================

-- 风格标签初始数据
INSERT INTO `t_style_tag` (`tag_name`, `tag_type`, `sort_order`, `status`) VALUES
('日系', 1, 1, 1),
('韩系', 1, 2, 1),
('复古', 1, 3, 1),
('小清新', 1, 4, 1),
('ins风', 1, 5, 1),
('中国风', 1, 6, 1),
('黑白', 1, 7, 1),
('胶片', 1, 8, 1),
('文艺', 1, 9, 1),
('森系', 1, 10, 1),
('校园', 2, 11, 1),
('海边', 2, 12, 1),
('街拍', 2, 13, 1),
('咖啡馆', 2, 14, 1),
('公园', 2, 15, 1),
('图书馆', 2, 16, 1),
('天台', 2, 17, 1),
('室内', 2, 18, 1);

-- 服务类型初始数据
INSERT INTO `t_service_type` (`type_name`, `description`, `sort_order`, `status`) VALUES
('人像写真', '个人形象照、艺术照', 1, 1),
('毕业照', '毕业季纪念照', 2, 1),
('情侣照', '情侣/结婚照', 3, 1),
('闺蜜照', '闺蜜/友情纪念', 4, 1),
('活动跟拍', '活动、演出跟拍', 5, 1),
('证件照', '证件照、形象照', 6, 1),
('旅拍', '旅行跟拍', 7, 1),
('商业摄影', '产品、商业拍摄', 8, 1);

-- 湛江高校初始数据
INSERT INTO `t_university` (`university_name`, `short_name`, `province`, `city`, `sort_order`, `status`) VALUES
('广东海洋大学', '海大', '广东', '湛江', 1, 1),
('广东医科大学', '广医', '广东', '湛江', 2, 1),
('岭南师范学院', '岭师', '广东', '湛江', 3, 1),
('广东海洋大学寸金学院', '寸金', '广东', '湛江', 4, 1),
('湛江幼儿师范专科学校', '湛幼', '广东', '湛江', 5, 1),
('广东文理职业学院', '文理', '广东', '湛江', 6, 1),
('湛江科技学院', '湛科', '广东', '湛江', 7, 1);

-- 初始化角色
INSERT INTO `t_role` (`role_name`, `role_key`, `description`, `status`) VALUES
('超级管理员', 'super_admin', '拥有所有权限', 1),
('运营管理员', 'operator', '负责内容审核和社区运营', 1),
('客服', 'customer_service', '负责订单处理和用户服务', 1);

-- 初始化管理员（密码：admin123，实际使用时需要加密）
INSERT INTO `t_admin` (`username`, `password`, `real_name`, `role_id`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKWb4RgtPqSxn6xABjxOB.E6VCUe', '系统管理员', 1, 1);

-- 初始化系统配置
INSERT INTO `t_system_config` (`config_key`, `config_value`, `config_type`, `description`) VALUES
('platform_fee_rate', '0.1', 'payment', '平台服务费比例（10%）'),
('min_withdrawal_amount', '50', 'payment', '最低提现金额'),
('withdrawal_settle_days', '7', 'payment', '提现结算天数'),
('order_auto_cancel_minutes', '30', 'order', '订单自动取消时间（分钟）'),
('order_auto_complete_days', '7', 'order', '订单自动完成天数'),
('works_cloud_storage_days', '30', 'works', '作品云端存储天数'),
('demand_valid_days', '7', 'demand', '需求默认有效期天数');

-- 初始化热门话题
INSERT INTO `t_topic` (`topic_name`, `description`, `is_hot`, `is_official`, `sort_order`, `status`) VALUES
('毕业季', '记录最美毕业时光', 1, 1, 1, 1),
('情侣日常', '甜蜜瞬间定格', 1, 1, 2, 1),
('校园生活', '青春不散场', 1, 1, 3, 1),
('湛江约拍', '湛江高校约拍交流', 1, 1, 4, 1),
('摄影技巧', '摄影干货分享', 0, 1, 5, 1);

-- =====================================================
-- 结束
-- =====================================================
