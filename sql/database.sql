-- ============================================
-- 1. 用户服务数据库
-- ============================================
CREATE DATABASE IF NOT EXISTS elm_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE elm_user;

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(32) NOT NULL,
                       phone VARCHAR(20) NOT NULL UNIQUE,
                       password VARCHAR(128) NOT NULL COMMENT 'BCrypt密文',
                       role VARCHAR(32) DEFAULT 'USER' COMMENT 'USER/ADMIN',
                       avatar VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE address (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         contact_name VARCHAR(50) NOT NULL COMMENT '联系人',
                         contact_phone VARCHAR(20) NOT NULL COMMENT '联系人电话',
                         province VARCHAR(50) NOT NULL COMMENT '省',
                         city VARCHAR(50) NOT NULL COMMENT '市',
                         district VARCHAR(50) NOT NULL COMMENT '区',
                         detail VARCHAR(255) NOT NULL COMMENT '详细地址',
                         label VARCHAR(50) DEFAULT NULL COMMENT '家/公司/学校',
                         is_default TINYINT DEFAULT 0 COMMENT '是否默认',
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

CREATE TABLE operate_log (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             operate_user VARCHAR(50) DEFAULT NULL,
                             operate_time VARCHAR(50) DEFAULT NULL,
                             class_name VARCHAR(255) DEFAULT NULL,
                             method_name VARCHAR(100) DEFAULT NULL,
                             method_params TEXT DEFAULT NULL,
                             return_value TEXT DEFAULT NULL,
                             cost_time BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';


-- ============================================
-- 2. 商品服务数据库
-- ============================================
CREATE DATABASE IF NOT EXISTS elm_goods DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE elm_goods;

CREATE TABLE category (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(50) NOT NULL COMMENT '分类名',
                          icon_url VARCHAR(255) DEFAULT NULL,
                          sort_order INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

CREATE TABLE merchant (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          shop_name VARCHAR(255) NOT NULL,
                          category_id INT NOT NULL,
                          rating FLOAT DEFAULT 5.0,
                          min_price FLOAT DEFAULT 0 COMMENT '起送价',
                          delivery_fee FLOAT DEFAULT 0 COMMENT '配送费',
                          estimated_min_time INT NOT NULL COMMENT '最短送达分钟',
                          estimated_max_time INT NOT NULL COMMENT '最长送达分钟',
                          monthly_sales INT DEFAULT 0,
                          image_url VARCHAR(255) NOT NULL,
                          announcement VARCHAR(255) DEFAULT NULL,
                          address VARCHAR(255) NOT NULL,
                          status VARCHAR(20) DEFAULT 'OPEN' COMMENT 'OPEN/CLOSED',
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          INDEX idx_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家表';

CREATE TABLE goods (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       shop_id INT NOT NULL,
                       category_id INT DEFAULT NULL COMMENT '店内分类',
                       price FLOAT NOT NULL,
                       estimated_price FLOAT NOT NULL COMMENT '折扣价',
                       positive_rate INT DEFAULT 100 COMMENT '好评率，95即95%',
                       monthly_sales INT DEFAULT 0,
                       description TEXT DEFAULT NULL,
                       ingredients VARCHAR(255) DEFAULT NULL,
                       portion VARCHAR(50) DEFAULT NULL,
                       image_url VARCHAR(255) NOT NULL,
                       sizes JSON NOT NULL COMMENT '规格',
                       temperatures JSON NOT NULL COMMENT '温度选项',
                       sweetness JSON NOT NULL COMMENT '甜度选项',
                       status VARCHAR(20) DEFAULT 'ON_SALE' COMMENT 'ON_SALE/SOLD_OUT/OFF_SHELF',
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       INDEX idx_shop_id (shop_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

CREATE TABLE comment (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         shop_id INT NOT NULL,
                         goods_id INT DEFAULT NULL,
                         order_id INT DEFAULT NULL,
                         rating INT NOT NULL COMMENT '1-5星',
                         content TEXT NOT NULL,
                         pic_url VARCHAR(255) DEFAULT NULL,
                         likes INT DEFAULT 0,
                         comment_date DATE NOT NULL,
                         INDEX idx_shop_id (shop_id),
                         INDEX idx_goods_id (goods_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

CREATE TABLE operate_log (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             operate_user VARCHAR(50) DEFAULT NULL,
                             operate_time VARCHAR(50) DEFAULT NULL,
                             class_name VARCHAR(255) DEFAULT NULL,
                             method_name VARCHAR(100) DEFAULT NULL,
                             method_params TEXT DEFAULT NULL,
                             return_value TEXT DEFAULT NULL,
                             cost_time BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';


-- ============================================
-- 3. 订单服务数据库
-- ============================================
CREATE DATABASE IF NOT EXISTS elm_order DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE elm_order;

CREATE TABLE cart (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      user_id BIGINT NOT NULL,
                      shop_id INT NOT NULL,
                      goods_id INT NOT NULL,
                      goods_name VARCHAR(255) NOT NULL,
                      goods_image VARCHAR(255) DEFAULT NULL,
                      selected_size VARCHAR(50) DEFAULT NULL,
                      selected_temperature VARCHAR(50) DEFAULT NULL,
                      selected_sweetness VARCHAR(50) DEFAULT NULL,
                      price FLOAT NOT NULL COMMENT '加入时单价',
                      quantity INT NOT NULL DEFAULT 1,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

CREATE TABLE orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        shop_id INT NOT NULL,
                        shop_name VARCHAR(255) NOT NULL,
                        address_id BIGINT NOT NULL,
                        address_snapshot VARCHAR(500) NOT NULL COMMENT '地址快照',
                        total_price FLOAT NOT NULL COMMENT '原价总价',
                        total_discount_price FLOAT NOT NULL COMMENT '折扣总价',
                        delivery_fee FLOAT DEFAULT 0,
                        actual_pay FLOAT NOT NULL COMMENT '实付金额',
                        status VARCHAR(20) DEFAULT 'UNPAID' COMMENT 'UNPAID/PAID/DELIVERING/DELIVERED/CANCELLED',
                        items JSON NOT NULL COMMENT '订单商品明细',
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        paid_at DATETIME DEFAULT NULL,
                        delivered_at DATETIME DEFAULT NULL,
                        INDEX idx_user_id (user_id),
                        INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE operate_log (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             operate_user VARCHAR(50) DEFAULT NULL,
                             operate_time VARCHAR(50) DEFAULT NULL,
                             class_name VARCHAR(255) DEFAULT NULL,
                             method_name VARCHAR(100) DEFAULT NULL,
                             method_params TEXT DEFAULT NULL,
                             return_value TEXT DEFAULT NULL,
                             cost_time BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';


-- ============================================
-- 4. 支付服务数据库
-- ============================================
CREATE DATABASE IF NOT EXISTS elm_payment DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE elm_payment;

CREATE TABLE payment (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         order_id INT NOT NULL UNIQUE,
                         user_id BIGINT NOT NULL,
                         amount FLOAT NOT NULL,
                         pay_method VARCHAR(20) NOT NULL COMMENT 'ALIPAY/WECHAT',
                         pay_status VARCHAR(20) DEFAULT 'UNPAID' COMMENT 'UNPAID/PAID/REFUNDED',
                         transaction_id VARCHAR(100) DEFAULT NULL COMMENT '第三方流水号',
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         paid_at DATETIME DEFAULT NULL,
                         INDEX idx_order_id (order_id),
                         INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付表';

CREATE TABLE operate_log (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             operate_user VARCHAR(50) DEFAULT NULL,
                             operate_time VARCHAR(50) DEFAULT NULL,
                             class_name VARCHAR(255) DEFAULT NULL,
                             method_name VARCHAR(100) DEFAULT NULL,
                             method_params TEXT DEFAULT NULL,
                             return_value TEXT DEFAULT NULL,
                             cost_time BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';