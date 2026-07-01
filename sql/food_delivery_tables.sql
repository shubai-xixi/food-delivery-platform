-- 创建数据库
CREATE DATABASE IF NOT EXISTS `user_service` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `user_service`;

-- 用户表
CREATE TABLE `users` (
                         `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                         `username` VARCHAR(32) NOT NULL COMMENT '用户名',
                         `phone` VARCHAR(20) NOT NULL COMMENT '手机号，登录用',
                         `password` VARCHAR(128) NOT NULL COMMENT 'BCrypt加密密文',
                         `role` VARCHAR(32) NOT NULL DEFAULT 'USER' COMMENT '角色：USER/ADMIN',
                         `avatar` VARCHAR(255) NULL COMMENT '头像URL',
                         `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- users 表增加 gender 字段
ALTER TABLE `users` ADD COLUMN `gender` TINYINT NULL COMMENT '性别：0-未知 1-男 2-女' AFTER `username`;

-- 收货地址表
CREATE TABLE `address` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '地址ID',
                           `user_id` BIGINT NOT NULL COMMENT '用户ID',
                           `contact_name` VARCHAR(50) NOT NULL COMMENT '联系人姓名',
                           `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系人手机号',
                           `province` VARCHAR(50) NOT NULL COMMENT '省',
                           `city` VARCHAR(50) NOT NULL COMMENT '市',
                           `district` VARCHAR(50) NOT NULL COMMENT '区',
                           `detail` VARCHAR(255) NOT NULL COMMENT '详细地址（门牌号）',
                           `label` VARCHAR(50) NULL COMMENT '标签（如：家、公司、学校）',
                           `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认地址',
                           `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           PRIMARY KEY (`id`),
                           INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收货地址表';

ALTER TABLE `address`
    ADD CONSTRAINT `fk_address_user_id`
        FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- 操作日志表
CREATE TABLE `operate_log` (
                               `id` INT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
                               `operate_user` VARCHAR(50) NULL COMMENT '操作人',
                               `operate_time` VARCHAR(50) NULL COMMENT '操作时间',
                               `class_name` VARCHAR(255) NULL COMMENT '类名',
                               `method_name` VARCHAR(100) NULL COMMENT '方法名',
                               `method_params` TEXT NULL COMMENT '方法参数',
                               `return_value` TEXT NULL COMMENT '返回值',
                               `cost_time` BIGINT NULL COMMENT '耗时（毫秒）',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `goods_service` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `goods_service`;

-- 商品分类表
CREATE TABLE `category` (
                            `id` INT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
                            `name` VARCHAR(50) NOT NULL COMMENT '分类名（如：中餐、西餐、奶茶）',
                            `icon_url` VARCHAR(255) NULL COMMENT '分类图标',
                            `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表';

-- 商家表
CREATE TABLE `merchant` (
                            `id` INT NOT NULL AUTO_INCREMENT COMMENT '商家ID',
                            `shop_name` VARCHAR(255) NOT NULL COMMENT '店铺名称',
                            `category_id` INT NOT NULL COMMENT '所属分类ID',
                            `rating` FLOAT NOT NULL DEFAULT 5.0 COMMENT '评分',
                            `min_price` FLOAT NOT NULL DEFAULT 0 COMMENT '起送价',
                            `delivery_fee` FLOAT NOT NULL DEFAULT 0 COMMENT '配送费',
                            `estimated_min_time` INT NOT NULL COMMENT '预计最短送达时间（分钟）',
                            `estimated_max_time` INT NOT NULL COMMENT '预计最长送达时间（分钟）',
                            `monthly_sales` INT NOT NULL DEFAULT 0 COMMENT '月销量',
                            `image_url` VARCHAR(255) NOT NULL COMMENT '店铺图片',
                            `address` VARCHAR(255) NOT NULL COMMENT '商家地址',
                            `status` VARCHAR(20) NOT NULL DEFAULT 'OPEN' COMMENT '营业状态：OPEN/CLOSED',
                            `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入驻时间',
                            PRIMARY KEY (`id`),
                            INDEX `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家表';

-- 商品表
CREATE TABLE `goods` (
                         `id` INT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
                         `name` VARCHAR(255) NOT NULL COMMENT '商品名',
                         `shop_id` INT NOT NULL COMMENT '所属商家ID',
                         `description` TEXT NULL COMMENT '商品描述',
                         `image_url` VARCHAR(255) NOT NULL COMMENT '商品图片',
                         `options` JSON NULL COMMENT '口味偏好模板（温度、甜度、小料等，仅用于前端渲染）',
                         `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         PRIMARY KEY (`id`),
                         INDEX `idx_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- 商品SKU表（新增）
CREATE TABLE `goods_sku` (
                             `id` INT NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
                             `goods_id` INT NOT NULL COMMENT '关联商品ID',
                             `spec_name` VARCHAR(100) NOT NULL COMMENT '规格名（如大杯、中杯、小份、大份+饮料）',
                             `price` FLOAT NOT NULL COMMENT '单价',
                             `stock` INT NOT NULL DEFAULT 0 COMMENT '库存',
                             `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认选中（0否，1是）',
                             PRIMARY KEY (`id`),
                             INDEX `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品SKU表';

ALTER TABLE `goods` ADD COLUMN `status` VARCHAR(20) DEFAULT 'ON' COMMENT '商品状态：ON-上架 OFF-下架';

-- 评价表
CREATE TABLE `comment` (
                           `id` INT NOT NULL AUTO_INCREMENT COMMENT '评价ID',
                           `user_id` BIGINT NOT NULL COMMENT '评价用户ID',
                           `shop_id` INT NOT NULL COMMENT '商家ID',
                           `goods_id` INT NULL COMMENT '商品ID（可为空，直接评店铺时为空）',
                           `order_id` INT NULL COMMENT '关联订单ID',
                           `parent_id` INT NULL COMMENT '父评论ID（NULL是一级评论，有值是回复）',
                           `rating` INT NOT NULL COMMENT '评分（1-5星，回复时可为0或不展示）',
                           `content` TEXT NOT NULL COMMENT '评价内容',
                           `pic_urls` JSON NULL COMMENT '图片列表，最多9张',
                           `likes` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
                           `is_anonymous` TINYINT NOT NULL DEFAULT 0 COMMENT '是否匿名（0否，1是）',
                           `status` VARCHAR(20) NOT NULL DEFAULT 'SHOW' COMMENT '状态：SHOW / HIDE / AUDIT',
                           `comment_date` DATE NOT NULL COMMENT '评价日期',
                           `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           PRIMARY KEY (`id`),
                           INDEX `idx_user_id` (`user_id`),
                           INDEX `idx_shop_id` (`shop_id`),
                           INDEX `idx_goods_id` (`goods_id`),
                           INDEX `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评价表';

-- 操作日志表
CREATE TABLE `operate_log` (
                               `id` INT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
                               `operate_user` VARCHAR(50) NULL COMMENT '操作人',
                               `operate_time` VARCHAR(50) NULL COMMENT '操作时间',
                               `class_name` VARCHAR(255) NULL COMMENT '类名',
                               `method_name` VARCHAR(100) NULL COMMENT '方法名',
                               `method_params` TEXT NULL COMMENT '方法参数',
                               `return_value` TEXT NULL COMMENT '返回值',
                               `cost_time` BIGINT NULL COMMENT '耗时（毫秒）',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `order_service` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `order_service`;

-- 购物车表
CREATE TABLE `cart` (
                        `id` INT NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
                        `user_id` BIGINT NOT NULL COMMENT '用户ID',
                        `shop_id` INT NOT NULL COMMENT '商家ID',
                        `goods_id` INT NOT NULL COMMENT '商品ID',
                        `specification` JSON NOT NULL COMMENT '用户选择的规格',
                        `price` FLOAT NOT NULL COMMENT '单价（快照）',
                        `quantity` INT NOT NULL COMMENT '数量',
                        `is_checked` TINYINT NOT NULL DEFAULT 1 COMMENT '是否勾选（1勾选，0未勾选）',
                        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                        `updated_at` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        INDEX `idx_user_id` (`user_id`),
                        INDEX `idx_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

-- 删掉 price 字段
ALTER TABLE cart DROP COLUMN price;

-- goods_id 改 sku_id
ALTER TABLE cart CHANGE goods_id sku_id INT NOT NULL COMMENT 'SKU ID';

-- specification 改 preferences
ALTER TABLE cart CHANGE specification preferences JSON NULL COMMENT '口味偏好';

ALTER TABLE cart ADD COLUMN goods_id INT NOT NULL COMMENT '商品ID' AFTER shop_id;

-- 订单表
CREATE TABLE `orders` (
                          `id` INT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
                          `user_id` BIGINT NOT NULL COMMENT '用户ID',
                          `shop_id` INT NOT NULL COMMENT '商家ID',
                          `shop_name` VARCHAR(255) NOT NULL COMMENT '商家名称（冗余）',
                          `address_id` BIGINT NOT NULL COMMENT '收货地址ID',
                          `address_snapshot` VARCHAR(500) NOT NULL COMMENT '地址快照（JSON）',
                          `total_price` FLOAT NOT NULL COMMENT '原价总价（可为负）',
                          `total_discount_price` FLOAT NOT NULL COMMENT '折扣后总价（可为负）',
                          `delivery_fee` FLOAT NOT NULL DEFAULT 0 COMMENT '配送费（可为负）',
                          `actual_pay` FLOAT NOT NULL COMMENT '实付金额（正数为收款，负数为退款）',
                          `order_type` VARCHAR(20) NOT NULL DEFAULT 'NORMAL' COMMENT '订单类型：NORMAL / REFUND',
                          `ref_original_order_id` INT NULL COMMENT '关联的原订单ID（REFUND 类型时必填）',
                          `pay_status` VARCHAR(20) NOT NULL DEFAULT 'UNPAID' COMMENT '支付状态',
                          `pay_method` VARCHAR(20) NULL COMMENT '支付方式',
                          `pay_time` DATETIME NULL COMMENT '支付/退款完成时间',
                          `pay_transaction_no` VARCHAR(100) NULL COMMENT '支付/退款网关流水号',
                          `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '订单状态：PENDING / CONFIRMED / COMPLETED / CANCELLED',
                          `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
                          `completed_at` DATETIME NULL COMMENT '完成时间',
                          `cancelled_at` DATETIME NULL COMMENT '取消时间',
                          PRIMARY KEY (`id`),
                          INDEX `idx_user_id` (`user_id`),
                          INDEX `idx_shop_id` (`shop_id`),
                          INDEX `idx_ref_original_order_id` (`ref_original_order_id`),
                          INDEX `idx_pay_status` (`pay_status`),
                          INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 订单详情表
CREATE TABLE `order_item` (
                              `id` INT NOT NULL AUTO_INCREMENT COMMENT '明细ID',
                              `order_id` INT NOT NULL COMMENT '订单ID',
                              `goods_id` INT NOT NULL COMMENT '商品ID',
                              `goods_name` VARCHAR(255) NOT NULL COMMENT '商品名（快照）',
                              `sku_id` INT NOT NULL COMMENT 'SKU ID',
                              `spec_name` VARCHAR(100) NOT NULL COMMENT '规格名（快照，如大杯）',
                              `base_price` FLOAT NOT NULL COMMENT '基础单价（快照）',
                              `preferences` JSON NULL COMMENT '口味偏好快照（如温度、甜度、小料）',
                              `addon_price` FLOAT NOT NULL DEFAULT 0 COMMENT '小料加价总和',
                              `final_price` FLOAT NOT NULL COMMENT '最终单价（base_price + addon_price）',
                              `quantity` INT NOT NULL COMMENT '数量',
                              PRIMARY KEY (`id`),
                              INDEX `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单详情表';

-- 操作日志表
CREATE TABLE `operate_log` (
                               `id` INT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
                               `operate_user` VARCHAR(50) NULL COMMENT '操作人',
                               `operate_time` VARCHAR(50) NULL COMMENT '操作时间',
                               `class_name` VARCHAR(255) NULL COMMENT '类名',
                               `method_name` VARCHAR(100) NULL COMMENT '方法名',
                               `method_params` TEXT NULL COMMENT '方法参数',
                               `return_value` TEXT NULL COMMENT '返回值',
                               `cost_time` BIGINT NULL COMMENT '耗时（毫秒）',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';