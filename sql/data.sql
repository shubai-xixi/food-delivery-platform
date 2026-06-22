-- 切换到商品库
USE elm_goods;

-- 1. 商品分类表
INSERT INTO category (name, icon_url, sort_order) VALUES
                                                      ('奶茶茶饮', 'icon1.png', 1),
                                                      ('果茶系列', 'icon2.png', 2),
                                                      ('咖啡饮品', 'icon3.png', 3),
                                                      ('冰沙奶昔', 'icon4.png', 4);

-- 2. 商家表（严格匹配你的结构）
INSERT INTO merchant (id, shop_name, category_id, rating, min_price, delivery_fee, estimated_min_time, estimated_max_time, monthly_sales, image_url, announcement, address, status) VALUES
                                                                                                                                                                                        (1000, '茶颜悦色（呈贡大学城店）', 1, 4.9, 15, 3, 25, 40, 1230, 'src/assets/img/sj03.png', '欢迎光临茶颜悦色', '云南省昆明市呈贡区', 'OPEN'),
                                                                                                                                                                                        (1001, '喜茶（昆明南屏街店）', 1, 4.8, 20, 4, 20, 35, 1150, 'src/assets/img/sj04.png', '欢迎光临喜茶', '云南省昆明市五华区南屏街', 'OPEN'),
                                                                                                                                                                                        (1002, '奈雪の茶（大理古城店）', 1, 4.7, 18, 3, 30, 50, 980, 'src/assets/img/sj05.png', '欢迎光临奈雪', '云南省大理市大理古城', 'OPEN'),
                                                                                                                                                                                        (1003, '一点点（丽江古城店）', 1, 4.6, 12, 2, 15, 30, 920, 'src/assets/img/sj06.png', '欢迎光临一点点', '云南省丽江市古城区', 'OPEN'),
                                                                                                                                                                                        (1004, 'CoCo都可（昆明金马碧鸡坊店）', 1, 4.5, 10, 1, 10, 25, 870, 'src/assets/img/sj07.png', '欢迎光临CoCo', '云南省昆明市西山区金马碧鸡坊', 'OPEN'),
                                                                                                                                                                                        (1005, '蜜雪冰城（曲靖麒麟店）', 1, 4.4, 8, 0, 8, 20, 810, 'src/assets/img/sj08.png', '欢迎光临蜜雪冰城', '云南省曲靖市麒麟区', 'OPEN'),
                                                                                                                                                                                        (1006, '茶百道（玉溪红塔店）', 2, 4.3, 13, 2, 20, 35, 760, 'src/assets/img/sj09.png', '欢迎光临茶百道', '云南省玉溪市红塔区', 'OPEN'),
                                                                                                                                                                                        (1007, '快乐柠檬（楚雄彝人古镇店）', 3, 4.2, 14, 2, 25, 40, 690, 'src/assets/img/sj10.png', '欢迎光临快乐柠檬', '云南省楚雄市彝人古镇', 'OPEN'),
                                                                                                                                                                                        (1008, '鹿角巷（红河蒙自店）', 2, 4.1, 16, 3, 18, 33, 630, 'src/assets/img/sj11.png', '欢迎光临鹿角巷', '云南省红河州蒙自市', 'OPEN'),
                                                                                                                                                                                        (1009, '茶理宜世（文山三七店）', 2, 4.0, 11, 1, 12, 25, 570, 'src/assets/img/sj12.png', '欢迎光临茶理宜世', '云南省文山州文山市', 'OPEN'),
                                                                                                                                                                                        (1010, '茶颜观色（普洱思茅店）', 1, 3.9, 12, 2, 25, 45, 520, 'src/assets/img/sj13.png', '欢迎光临茶颜观色', '云南省普洱市思茅区', 'OPEN'),
                                                                                                                                                                                        (1011, '茶语时光（西双版纳景洪店）', 1, 3.8, 13, 2, 20, 40, 460, 'src/assets/img/sj14.png', '欢迎光临茶语时光', '云南省西双版纳州景洪市', 'OPEN'),
                                                                                                                                                                                        (1012, '茶里茶气（德宏芒市店）', 1, 3.7, 9, 1, 8, 20, 410, 'src/assets/img/sj15.png', '欢迎光临茶里茶气', '云南省德宏州芒市', 'OPEN'),
                                                                                                                                                                                        (1013, '茶香四溢（保山隆阳店）', 1, 3.6, 10, 1, 10, 25, 360, 'src/assets/img/sj16.png', '欢迎光临茶香四溢', '云南省保山市隆阳区', 'OPEN'),
                                                                                                                                                                                        (1014, '茶语心声（昭通昭阳店）', 1, 3.5, 11, 2, 15, 33, 310, 'src/assets/img/sj17.png', '欢迎光临茶语心声', '云南省昭通市昭阳区', 'OPEN'),
                                                                                                                                                                                        (1015, '茶香物语（临沧临翔店）', 1, 3.4, 12, 2, 25, 45, 260, 'src/assets/img/sj18.png', '欢迎光临茶香物语', '云南省临沧市临翔区', 'OPEN'),
                                                                                                                                                                                        (1016, '茶韵悠然（怒江泸水店）', 1, 3.3, 13, 3, 20, 40, 210, 'src/assets/img/sj19.png', '欢迎光临茶韵悠然', '云南省怒江州泸水市', 'OPEN'),
                                                                                                                                                                                        (1017, '茶香满溢（迪庆香格里拉店）', 1, 3.2, 8, 0, 8, 18, 160, 'src/assets/img/sj20.png', '欢迎光临茶香满溢', '云南省迪庆州香格里拉市', 'OPEN'),
                                                                                                                                                                                        (1018, '茶语清心（丽江束河店）', 1, 3.1, 10, 1, 12, 25, 110, 'src/assets/img/sj21.png', '欢迎光临茶语清心', '云南省丽江市古城区束河古镇', 'OPEN'),
                                                                                                                                                                                        (1019, '茶香悠远（昆明世博园店）', 1, 3.0, 15, 2, 15, 33, 60, 'src/assets/img/sj22.png', '欢迎光临茶香悠远', '云南省昆明市盘龙区世博园', 'OPEN');

-- 3. 商品表（完全适配你的字段，删除多余列）
INSERT INTO goods (id, name, shop_id, category_id, price, estimated_price, positive_rate, monthly_sales, description, ingredients, portion, image_url, sizes, temperatures, sweetness, status) VALUES
                                                                                                                                                                                                   (10000, '玫瑰乌龙（玫瑰茶）', 1000, 1, 20, 18, 93, 390, '这是一款经典的玫瑰乌龙茶', '乌龙茶, 玫瑰', '500ml', 'src/assets/img/dcfl01.png', '[{"name": "大杯", "price": 25}, {"name": "中杯", "price": 20}]', '["常温", "冰", "热"]', '["无糖", "半糖", "全糖"]', 'ON_SALE'),
                                                                                                                                                                                                   (10020, '桂花乌龙', 1000, 1, 22, 20, 92, 380, '桂花香与乌龙茶的完美结合', '乌龙茶, 桂花', '500ml', 'src/assets/img/dcfl02.png', '[{"name": "大杯", "price": 25}, {"name": "中杯", "price": 22}]', '["热", "常温"]', '["无糖", "半糖", "全糖"]', 'ON_SALE'),
                                                                                                                                                                                                   (10021, '蜜桃乌龙', 1000, 1, 20, 18, 91, 370, '蜜桃与乌龙茶的清新口感', '乌龙茶, 蜜桃', '500ml', 'src/assets/img/dcfl03.png', '[{"name": "大杯", "price": 22}, {"name": "中杯", "price": 20}]', '["冰", "常温"]', '["无糖", "半糖", "全糖"]', 'ON_SALE'),
                                                                                                                                                                                                   (10022, '荔枝乌龙', 1000, 1, 24, 22, 90, 360, '荔枝与乌龙茶的甜美结合', '乌龙茶, 荔枝', '500ml', 'src/assets/img/dcfl04.png', '[{"name": "大杯", "price": 28}, {"name": "中杯", "price": 24}]', '["常温", "冰"]', '["无糖", "半糖", "全糖"]', 'ON_SALE'),
                                                                                                                                                                                                   (10023, '柠檬乌龙', 1000, 1, 18, 16, 89, 350, '柠檬与乌龙茶的清新口感', '乌龙茶, 柠檬', '500ml', 'src/assets/img/dcfl05.png', '[{"name": "大杯", "price": 20}, {"name": "中杯", "price": 18}]', '["冰", "常温"]', '["无糖", "半糖", "全糖"]', 'ON_SALE'),
                                                                                                                                                                                                   (10024, '草莓乌龙', 1000, 1, 22, 20, 88, 340, '草莓与乌龙茶的甜美结合', '乌龙茶, 草莓', '500ml', 'src/assets/img/dcfl06.png', '[{"name": "大杯", "price": 25}, {"name": "中杯", "price": 22}]', '["常温", "冰"]', '["无糖", "半糖", "全糖"]', 'ON_SALE'),
                                                                                                                                                                                                   (10025, '芒果乌龙', 1000, 1, 20, 18, 87, 330, '芒果与乌龙茶的清新口感', '乌龙茶, 芒果', '500ml', 'src/assets/img/dcfl07.png', '[{"name": "大杯", "price": 22}, {"name": "中杯", "price": 20}]', '["冰", "常温"]', '["无糖", "半糖", "全糖"]', 'ON_SALE'),
                                                                                                                                                                                                   (10026, '蓝莓乌龙', 1000, 1, 24, 22, 86, 320, '蓝莓与乌龙茶的甜美结合', '乌龙茶, 蓝莓', '500ml', 'src/assets/img/dcfl08.png', '[{"name": "大杯", "price": 28}, {"name": "中杯", "price": 24}]', '["常温", "冰"]', '["无糖", "半糖", "全糖"]', 'ON_SALE'),
                                                                                                                                                                                                   (10027, '菠萝乌龙', 1000, 1, 18, 16, 85, 310, '菠萝与乌龙茶的清新口感', '乌龙茶, 菠萝', '500ml', 'src/assets/img/dcfl09.png', '[{"name": "大杯", "price": 20}, {"name": "中杯", "price": 18}]', '["冰", "常温"]', '["无糖", "半糖", "全糖"]', 'ON_SALE'),
                                                                                                                                                                                                   (10028, '葡萄乌龙', 1000, 1, 22, 20, 84, 300, '葡萄与乌龙茶的甜美结合', '乌龙茶, 葡萄', '500ml', 'src/assets/img/dcfl10.png', '[{"name": "大杯", "price": 25}, {"name": "中杯", "price": 22}]', '["常温", "冰"]', '["无糖", "半糖", "全糖"]', 'ON_SALE'),

                                                                                                                                                                                                   (10001, '柠檬绿茶', 1001, 1, 18, 16, 88, 420, '清新柠檬与绿茶的完美结合', '绿茶, 柠檬', '500ml', 'src/assets/img/sj01.png', '[{"name": "大杯", "price": 22}, {"name": "中杯", "price": 18}]', '["冰", "常温"]', '["无糖", "半糖", "全糖"]', 'ON_SALE'),
                                                                                                                                                                                                   (10029, '蜜桃绿茶', 1001, 1, 20, 18, 87, 410, '蜜桃与绿茶的清新口感', '绿茶, 蜜桃', '500ml', 'src/assets/img/sj02.png', '[{"name": "大杯", "price": 22}, {"name": "中杯", "price": 20}]', '["冰", "常温"]', '["无糖", "半糖", "全糖"]', 'ON_SALE'),
                                                                                                                                                                                                   (10030, '草莓绿茶', 1001, 1, 22, 20, 86, 400, '草莓与绿茶的甜美结合', '绿茶, 草莓', '500ml', 'src/assets/img/sj03.png', '[{"name": "大杯", "price": 25}, {"name": "中杯", "price": 22}]', '["常温", "冰"]', '["无糖", "半糖", "全糖"]', 'ON_SALE');

-- 4. 评价表（严格匹配你的结构，删除 parent_id，加入 shop_id、goods_id、rating）
INSERT INTO comment (user_id, shop_id, goods_id, order_id, rating, content, pic_url, likes, comment_date) VALUES
                                                                                                              ('11122223333', 1000, 10000, 1000, 5, '很不错的饭菜，价格未免也太实惠了吧，味道超级无敌可以的，数量足，快递也贼块，整体还是非常不错的，超级满足，下次再来，实在是太好吃了，这么良心的商家太少有了。', 'src/assets/img/dcfl01.png', 24, '2024-05-19'),
                                                                                                              ('13908549332', 1000, 10000, 1001, 5, '菜品新鲜，味道很好，服务态度也很棒，下次还会再来！', 'src/assets/img/sj01.png', 15, '2024-05-20'),
                                                                                                              ('11451419198', 1000, 10000, 1002, 5, '非常满意的一次用餐体验，推荐给大家！', 'src/assets/img/sp01.png', 30, '2024-05-21'),
                                                                                                              ('11111511111', 1000, 10000, 1003, 5, '味道不错，分量也很足，性价比很高！', 'src/assets/img/dcfl02.png', 12, '2024-05-22'),
                                                                                                              ('11122223333', 1001, 10001, 1004, 5, '环境干净整洁，菜品也很美味，值得推荐！', 'src/assets/img/sj02.png', 20, '2024-05-23'),
                                                                                                              ('13908549332', 1001, 10001, 1005, 5, '服务态度很好，菜品也很新鲜，下次还会再来！', 'src/assets/img/sp02.png', 18, '2024-05-24'),
                                                                                                              ('11451419198', 1001, 10001, 1006, 5, '味道很好，分量也很足，性价比很高！', 'src/assets/img/dcfl03.png', 25, '2024-05-25'),
                                                                                                              ('11111511111', 1001, 10001, 1007, 5, '菜品新鲜，味道很好，服务态度也很棒，下次还会再来！', 'src/assets/img/sj03.png', 10, '2024-05-26');

-- ============================================
-- 用户库：用户 + 地址
-- ============================================
USE elm_user;

-- 用户表（密码是 123456 的 BCrypt 密文）
INSERT INTO users (username, phone, password, role, avatar) VALUES
                                                                ('张三', '11122223333', '$2a$10$FLBcGbv4Vv4y1QpVv4y1QpVv4y1QpVv4y1QpVv4y1QpVv4y1Qp', 'USER', 'avatar1.png'),
                                                                ('李四', '13908549332', '$2a$10$FLBcGbv4Vv4y1QpVv4y1QpVv4y1QpVv4y1QpVv4y1QpVv4y1Qp', 'USER', 'avatar2.png'),
                                                                ('王五', '11451419198', '$2a$10$FLBcGbv4Vv4y1QpVv4y1QpVv4y1QpVv4y1QpVv4y1QpVv4y1Qp', 'USER', 'avatar3.png'),
                                                                ('赵六', '11111511111', '$2a$10$FLBcGbv4Vv4y1QpVv4y1QpVv4y1QpVv4y1QpVv4y1QpVv4y1Qp', 'USER', 'avatar4.png'),
                                                                ('管理员', '13800138000', '$2a$10$FLBcGbv4Vv4y1QpVv4y1QpVv4y1QpVv4y1QpVv4y1QpVv4y1Qp', 'ADMIN', 'admin.png');

-- 地址表
INSERT INTO address (user_id, contact_name, contact_phone, province, city, district, detail, label, is_default) VALUES
                                                                                                                    (1, '张三', '11122223333', '云南省', '昆明市', '呈贡区', '大学城A栋101', '学校', 1),
                                                                                                                    (1, '张三', '11122223333', '云南省', '昆明市', '五华区', '南屏街小区', '家', 0),
                                                                                                                    (2, '李四', '13908549332', '云南省', '大理市', '古城区', '古城客栈', '酒店', 1),
                                                                                                                    (3, '王五', '11451419198', '云南省', '丽江市', '古城区', '束河古镇', '家', 1),
                                                                                                                    (4, '赵六', '11111511111', '云南省', '曲靖市', '麒麟区', '麒麟花园', '公司', 1);

-- ============================================
-- 订单库：购物车 + 订单示例
-- ============================================
USE elm_order;

-- 购物车
INSERT INTO cart (user_id, shop_id, goods_id, goods_name, goods_image, selected_size, selected_temperature, selected_sweetness, price, quantity) VALUES
                                                                                                                                                     (1, 1000, 10000, '玫瑰乌龙', 'src/assets/img/dcfl01.png', '中杯', '冰', '半糖', 18, 1),
                                                                                                                                                     (1, 1001, 10001, '柠檬绿茶', 'src/assets/img/sj01.png', '大杯', '常温', '全糖', 16, 2);

-- 订单
INSERT INTO orders (user_id, shop_id, shop_name, address_id, address_snapshot, total_price, total_discount_price, delivery_fee, actual_pay, status, items) VALUES
                                                                                                                                                               (1, 1000, '茶颜悦色（呈贡大学城店）', 1, '云南省昆明市呈贡区大学城A栋101', 36, 36, 3, 39, 'PAID', '[{"goodsId":10000,"name":"玫瑰乌龙","price":18,"quantity":2}]'),
                                                                                                                                                               (1, 1001, '喜茶（昆明南屏街店）', 2, '云南省昆明市五华区南屏街小区', 32, 30, 4, 34, 'DELIVERED', '[{"goodsId":10001,"name":"柠檬绿茶","price":16,"quantity":2}]');

-- ============================================
-- 支付库：支付记录
-- ============================================
USE elm_payment;

INSERT INTO payment (order_id, user_id, amount, pay_method, pay_status, transaction_id, paid_at) VALUES
                                                                                                     (1, 1, 39, 'WECHAT', 'PAID', 'WX2025010112345678', NOW()),
                                                                                                     (2, 1, 34, 'ALIPAY', 'PAID', 'ALI2025010112345678', NOW());