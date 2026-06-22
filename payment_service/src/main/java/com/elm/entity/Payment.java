// Payment.java
package com.elm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("payment")
public class Payment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    private Long userId;
    private Float amount;
    private String payMethod;
    private String payStatus;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
}